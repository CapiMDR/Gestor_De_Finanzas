package movements.movement_view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;

public class MovementCategoriesViewFX {

    @FXML private TextField txtNewNameCategory;
    @FXML private ComboBox<String> cmbCategoryType;
    @FXML private Button btnConfirm;
    @FXML private ListView<String> listCategories;
    @FXML private Button btnDeleteCategory;

    @FXML
    public void initialize() {
        cmbCategoryType.setItems(FXCollections.observableArrayList("INCOME", "EXPENSE"));
        cmbCategoryType.getSelectionModel().selectFirst();

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

                    if (item.contains(" - [INCOME]")) {
                        indicator.setFill(Color.web("#3182CE")); // Pastel blue
                        label.setText(item.replace(" - [INCOME]", ""));
                    } else if (item.contains(" - [EXPENSE]")) {
                        indicator.setFill(Color.web("#DD6B20")); // Pastel orange
                        label.setText(item.replace(" - [EXPENSE]", ""));
                    } else {
                        indicator.setFill(Color.GRAY);
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
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }

    public void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }

    public void showWarning(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }

    public boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }
}
