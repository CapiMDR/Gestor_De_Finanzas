package movements.movement_view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.geometry.Pos;

public class MovementCategoriesViewFX {

    @FXML private TextField txtNewNameCategory;
    @FXML private ComboBox<String> cmbCategoryType;
    @FXML private Button btnConfirm;
    @FXML private ListView<String> listCategories;
    @FXML private Button btnDeleteCategory;

    @FXML
    public void initialize() {
        cmbCategoryType.setItems(FXCollections.observableArrayList("Ingreso", "Egreso"));
        cmbCategoryType.getSelectionModel().selectFirst();

        // Limit category name to 24 characters
        txtNewNameCategory.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 24) {
                return null;
            }
            return change;
        }));

        listCategories.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox box = new HBox(10);
                    box.setAlignment(Pos.CENTER_LEFT);

                    Circle indicator = new Circle(5);
                    Label label = new Label();
                    label.setWrapText(true);
                    label.setMaxWidth(Double.MAX_VALUE); // or a fixed width if inside a known container

                    indicator.getStyleClass().removeAll("color-income", "color-expense", "color-neutral");
                    if (item.contains(" - [INCOME]")) {
                        indicator.getStyleClass().add("color-income");
                        label.setText(item.replace(" - [INCOME]", ""));
                    } else if (item.contains(" - [EXPENSE]")) {
                        indicator.getStyleClass().add("color-expense");
                        label.setText(item.replace(" - [EXPENSE]", ""));
                    } else {
                        indicator.getStyleClass().add("color-neutral");
                        label.setText(item);
                    }

                    box.getChildren().addAll(indicator, label);
                    setGraphic(box);
                }
            }
        });
    }

    public void clearFields() {
        txtNewNameCategory.clear();
        cmbCategoryType.getSelectionModel().selectFirst();
    }

    // -- Getters for controller --
    public Button getBtnConfirm() { return btnConfirm; }
    public Button getBtnDeleteCategory() { return btnDeleteCategory; }
    public TextField getTxtNewNameCategory() { return txtNewNameCategory; }
    public ComboBox<String> getCmbCategoryType() { return cmbCategoryType; }
    public ListView<String> getListCategories() { return listCategories; }

    // -- Dialog Helpers --
    public void showInfo(String title, String message) {
        utils.UIUtils.showInfo(title, message);
    }

    public void showError(String title, String message) {
        utils.UIUtils.showError(title, message);
    }

    public void showWarning(String title, String message) {
        utils.UIUtils.showWarning(title, message);
    }

    public boolean showConfirmation(String title, String message) {
        return utils.UIUtils.showConfirm(title, message);
    }
}
