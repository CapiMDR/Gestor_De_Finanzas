package accounts.account_view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * JavaFX View Controller for the Account Interest dialog.
 * Handles pure UI components and provides accessors for the actual Controller.
 */
public class AccountInterestViewFX {

    @FXML private TextField txtInitialBalance;
    @FXML private TextField txtInterestRate;
    @FXML private TextField txtTimeYears;
    @FXML private Label lblFutureBalance;

    // ── Getters for Controller to read/write data ──────────────────────────

    public TextField getTxtInitialBalance() { return txtInitialBalance; }
    public TextField getTxtInterestRate() { return txtInterestRate; }
    public TextField getTxtTimeYears() { return txtTimeYears; }
    public Label getLblFutureBalance() { return lblFutureBalance; }

    public void setAccount(accounts.account_model.Account account) {}
    public void setOnBack(Runnable onBack) {}

    /**
     * Loads the account interest view and returns its root node for embedding
     * inside an {@link AccountShell} tab.
     *
     * @param account the account to calculate interest for
     * @return the root node of the interest view, or {@code null} on error
     */
    public static javafx.scene.Node loadForAccount(accounts.account_model.Account account, Runnable onBack) {
        if (account == null) return null;
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    AccountInterestViewFX.class.getResource("/fxml/accounts/account_interest.fxml"));
            // For a DialogPane to be embedded in a normal layout, we extract its content
            javafx.scene.control.DialogPane pane = loader.load();
            AccountInterestViewFX view = loader.getController();
            view.setAccount(account);
            if (onBack != null) view.setOnBack(onBack);

            // Set up live calculation events
            view.getTxtInitialBalance().setText(account.getCurrentBalance().toPlainString());
            
            javafx.beans.value.ChangeListener<String> listener = (obs, oldVal, newVal) -> {
                try {
                    String rateStr = view.getTxtInterestRate().getText().trim();
                    String yearsStr = view.getTxtTimeYears().getText().trim();

                    if (rateStr.isBlank() || yearsStr.isBlank()) return;

                    java.math.BigDecimal rate = new java.math.BigDecimal(rateStr);
                    java.math.BigDecimal years = new java.math.BigDecimal(yearsStr);
                    java.math.BigDecimal initial = account.getCurrentBalance();

                    if (rate.compareTo(java.math.BigDecimal.ZERO) <= 0 || years.compareTo(java.math.BigDecimal.ZERO) <= 0) {
                        view.getLblFutureBalance().setText("Valores deben ser > 0");
                        return;
                    }

                    java.math.BigDecimal r = rate.divide(java.math.BigDecimal.valueOf(100));
                    java.math.BigDecimal onePlusR = r.add(java.math.BigDecimal.ONE);
                    java.math.BigDecimal future = initial.multiply(onePlusR.pow(years.intValue()));

                    view.getLblFutureBalance().setText(String.format("$ %.2f", future));
                } catch (Exception e) {
                    view.getLblFutureBalance().setText("Inválido");
                }
            };
            
            view.getTxtInterestRate().textProperty().addListener(listener);
            view.getTxtTimeYears().textProperty().addListener(listener);

            return pane.getContent();
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger("GlobalExceptionHandler").error("Excepción detectada", e);
            return null;
        }
    }
}
