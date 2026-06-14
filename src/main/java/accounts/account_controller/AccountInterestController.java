package accounts.account_controller;

import java.math.BigDecimal;

import accounts.account_model.Account;
import accounts.account_view.AccountInterestViewFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

/**
 * Controller for the Account Interest dialog.
 * Loads the FXML, sets up the view, and handles live compound interest
 * calculation.
 */
public class AccountInterestController {

    private final Account account;
    private final Dialog<ButtonType> dialog;
    private AccountInterestViewFX view;

    public AccountInterestController(Account account) {
        this.account = account;
        this.dialog = new Dialog<>();
        initDialog();
    }

    private void initDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/accounts/account_interest.fxml"));
            DialogPane pane = loader.load();
            this.view = loader.getController();

            dialog.setDialogPane(pane);
            dialog.setTitle("Calculadora de Interés Compuesto");
            dialog.setHeaderText("Cuenta: " + account.getName());

            // Pre-fill initial balance
            view.getTxtInitialBalance().setText(account.getCurrentBalance().toPlainString());

            // Assign events for live calculation
            assignEvents();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void assignEvents() {
        view.getTxtInterestRate().textProperty().addListener((obs, oldVal, newVal) -> calculateInterest());
        view.getTxtTimeYears().textProperty().addListener((obs, oldVal, newVal) -> calculateInterest());
    }

    private void calculateInterest() {
        try {
            String rateStr = view.getTxtInterestRate().getText().trim();
            String yearsStr = view.getTxtTimeYears().getText().trim();

            if (rateStr.isBlank() || yearsStr.isBlank())
                return;

            BigDecimal rate = new BigDecimal(rateStr);
            BigDecimal years = new BigDecimal(yearsStr);
            BigDecimal initial = account.getCurrentBalance();

            if (rate.compareTo(BigDecimal.ZERO) <= 0 || years.compareTo(BigDecimal.ZERO) <= 0) {
                view.getLblFutureBalance().setText("Valores deben ser > 0");
                return;
            }

            BigDecimal r = rate.divide(BigDecimal.valueOf(100));
            BigDecimal onePlusR = r.add(BigDecimal.ONE);
            BigDecimal future = initial.multiply(onePlusR.pow(years.intValue()));

            view.getLblFutureBalance().setText(String.format("$ %.2f", future));
        } catch (NumberFormatException e) {
            view.getLblFutureBalance().setText("Número inválido");
        } catch (Exception e) {
            view.getLblFutureBalance().setText("Error");
        }
    }

    public void show() {
        dialog.showAndWait();
    }
}
