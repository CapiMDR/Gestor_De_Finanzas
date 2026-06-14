package movements.movement_controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_model.AccountManagerSubject;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_model.CategoryManager;
import movements.movement_model.CategoryObserver;
import movements.movement_model.MovementCategory.MovementType;
import movements.movement_view.MovementCategoriesViewFX;
import movements.movement_view.MovementsViewFX;

/**
 * Controller in charge of managing the financial movements of an account,
 * as well as category administration. Implements the Observer pattern to
 * update views when changes occur in categories.
 * 
 * @author Martín Jesús Pool Chuc
 */
public class MovementController implements CategoryObserver {
    private CategoryManager model;
    private MovementsViewFX view;
    private MovementCategoriesViewFX categoriesManagerView;
    private Stage categoriesStage;
    private Account currentAccount;

    private static final String ERROR_TITLE = "Error";

    /**
     * Constructor for the movement controller.
     *
     * @param model          the category manager
     * @param view           the JavaFX movement manager view
     * @param currentAccount the currently selected account
     */
    public MovementController(CategoryManager model, MovementsViewFX view,
            Account currentAccount) {
        this.model = model;
        this.view = view;
        this.categoriesManagerView = null;
        this.currentAccount = currentAccount;

        if (this.view != null && this.currentAccount != null) {
            this.view.setAccountName(this.currentAccount.getName());
        }

        model.addObserver(this);
        AssignEvents();
        loadInitialData();
    }

    /**
     * Assigns user interface events to their respective handlers.
     */
    private void AssignEvents() {
        this.view.getBtnAddIncome().setOnAction(e -> handleAddMovement(MovementType.INCOME));
        this.view.getBtnAddExpense().setOnAction(e -> handleAddMovement(MovementType.EXPENSE));
        this.view.getBtnAddCategoryIncome().setOnAction(e -> showCategoriesManagerView());
        this.view.getBtnAddCategoryExpense().setOnAction(e -> showCategoriesManagerView());
    }

    /**
     * Handles the creation of a new income or expense movement.
     *
     * @param type the type of movement (INCOME or EXPENSE)
     */
    private void handleAddMovement(MovementType type) {
        String description;
        String amountStr;
        String categoryName;
        LocalDateTime movementDate;

        if (type == MovementType.INCOME) {
            description  = view.getDescriptionIncome();
            amountStr    = view.getAmountIncomeText();
            categoryName = view.getSelectedCategoryIncome();
            movementDate = view.getIncomeDateAsLocalDateTime();
        } else {
            description  = view.getDescriptionExpense();
            amountStr    = view.getAmountExpenseText();
            categoryName = view.getSelectedCategoryExpense();
            movementDate = view.getExpenseDateAsLocalDateTime();
        }

        try {
            if (description.isEmpty() || amountStr.isEmpty() || categoryName == null) {
                showAlert(AlertType.WARNING, "Error de Validación",
                        "Debe llenar todos los campos y seleccionar una categoría.");
                return;
            }

            BigDecimal amount = new BigDecimal(amountStr);

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                showAlert(AlertType.ERROR, ERROR_TITLE, "El monto debe ser mayor a cero.");
                return;
            }

            MovementCategory category = model.getCategoryByName(categoryName);
            addMovement(description, amount, category, currentAccount, movementDate);
            showAlert(AlertType.INFORMATION, "Éxito", "Movimiento agregado exitosamente a la cuenta.");

            if (type == MovementType.INCOME) {
                view.clearIncomeFields();
            } else {
                view.clearExpenseFields();
            }

        } catch (NumberFormatException ex) {
            showAlert(AlertType.ERROR, "Error de Formato", "El monto debe ser un número válido.");
        } catch (IllegalArgumentException ex) {
            showAlert(AlertType.ERROR, "Error de Validación", ex.getMessage());
        } catch (Exception ex) {
            showAlert(AlertType.ERROR, ERROR_TITLE, "Error al procesar el movimiento: " + ex.getMessage());
        }
    }

    /**
     * Adds a new movement to the given account.
     *
     * @param description the description of the movement
     * @param amount      the amount of the movement
     * @param category    the category of the movement
     * @param account     the account it belongs to
     * @param date        the date of the movement
     */
    public void addMovement(String description, BigDecimal amount, MovementCategory category, Account account,
            LocalDateTime date) {
        Movement movement = new Movement(UUID.randomUUID(), description, amount, category, account, date);

        account.addMovement(movement);
        AccountManager.saveAccountsData();

        AccountManagerSubject.notifyObservers(AccountManager.getAccounts());
        model.notifyObservers();
    }

    /**
     * Loads the initial data into the view, including the account name
     * and the list of available categories.
     */
    private void loadInitialData() {
        view.setAccountName(currentAccount.getName());
        updateCategoriesView(new ArrayList<>(model.getCategories().values()));
    }

    /**
     * Updates the visible category lists in the movements view.
     *
     * @param categories updated list of categories
     */
    private void updateCategoriesView(List<MovementCategory> categories) {
        List<String> incomeNames  = new ArrayList<>();
        List<String> expenseNames = new ArrayList<>();

        for (MovementCategory category : categories) {
            if (category.getType() == MovementType.INCOME) {
                incomeNames.add(category.getName());
            } else {
                expenseNames.add(category.getName());
            }
        }

        view.updateIncomeCategories(incomeNames);
        view.updateExpenseCategories(expenseNames);
    }

    /**
     * Shows the category administration window. If it's already open,
     * simply brings it to the front.
     */
    private void showCategoriesManagerView() {
        if (categoriesStage != null && categoriesStage.isShowing()) {
            categoriesStage.toFront();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/movements/movement_categories.fxml"));
            Parent root = loader.load();

            categoriesManagerView = loader.getController();

            categoriesStage = new Stage();
            categoriesStage.initModality(Modality.APPLICATION_MODAL);
            categoriesStage.setTitle("Gestionar Categorías");
            Scene scene = new Scene(root, 600, 450);
            scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
            categoriesStage.setScene(scene);

            categoriesStage.setOnHidden(e -> {
                categoriesManagerView = null;
                categoriesStage = null;
            });

            updateCategoriesList(categoriesManagerView);

            categoriesManagerView.getBtnConfirm().setOnAction(e -> handleAddCategory(categoriesManagerView));
            categoriesManagerView.getBtnDeleteCategory().setOnAction(e -> handleRemoveCategory(categoriesManagerView));

            categoriesStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, ERROR_TITLE, "No se pudo cargar la vista de categorías.");
        }
    }

    /**
     * Handles the creation of a new category from the administration view.
     *
     * @param categoriesView the category administration view
     */
    private void handleAddCategory(MovementCategoriesViewFX categoriesView) {
        String name = categoriesView.getTxtNewNameCategory().getText().trim();
        String typeStr = categoriesView.getCmbCategoryType().getValue();

        if (name.isEmpty() || typeStr == null) {
            categoriesView.showWarning("Advertencia", "Debe ingresar el nombre y seleccionar el tipo (Ingreso/Gasto)");
            return;
        }

        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

        try {
            String typeEnumStr = "Ingreso".equals(typeStr) ? "INCOME" : "EXPENSE";
            MovementType type = MovementType.valueOf(typeEnumStr);

            if (model.getCategories().containsKey(name)) {
                categoriesView.showError(ERROR_TITLE, "La categoría '" + name + "' ya existe.");
                return;
            }

            MovementCategory newCategory = new MovementCategory(name, type);
            model.addCategory(newCategory);

            categoriesView.clearFields();

        } catch (IllegalArgumentException ex) {
            categoriesView.showError(ERROR_TITLE, "Error al procesar el tipo de categoría.");
        }
    }

    /**
     * Handles the deletion of a selected category.
     *
     * @param categoriesView the category administration view
     */
    private void handleRemoveCategory(MovementCategoriesViewFX categoriesView) {
        String categorySelection = categoriesView.getListCategories().getSelectionModel().getSelectedItem();

        if (categorySelection == null) {
            categoriesView.showWarning("Advertencia", "Debe seleccionar una categoría para eliminar");
            return;
        }

        // Extract the actual name from the formatted string "Name - [TYPE]"
        String categoryName = categorySelection.split(" - ")[0];

        boolean confirmed = categoriesView.showConfirmation("Confirmar Eliminación",
                "¿Está seguro de eliminar la categoría '" + categoryName + "'?");

        if (confirmed) {
            MovementCategory categoryToRemove = model.getCategoryByName(categoryName);

            if (categoryToRemove != null) {
                model.removeCategory(categoryToRemove);
                categoriesView.showInfo("Éxito", "Categoría eliminada: " + categoryName);
            } else {
                categoriesView.showError(ERROR_TITLE, "Error al encontrar la categoría en el modelo");
            }
        }
    }

    /**
     * Updates the category list shown in the administration view.
     *
     * @param categoriesView the view containing the category list
     */
    private void updateCategoriesList(MovementCategoriesViewFX categoriesView) {
        List<String> formattedNames = model.getCategories().values().stream()
                .sorted((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()))
                .map(c -> c.getName() + " - [" + c.getType().name() + "]")
                .collect(Collectors.toList());

        categoriesView.getListCategories().getItems().setAll(formattedNames);
    }

    /**
     * Observer pattern method executed when there are changes in the
     * category list.
     *
     * @param categories the updated list of categories
     */
    @Override
    public void onNotify(List<MovementCategory> categories) {
        updateCategoriesView(categories);

        if (this.categoriesManagerView != null && categoriesStage != null && categoriesStage.isShowing()) {
            updateCategoriesList(this.categoriesManagerView);
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }
}