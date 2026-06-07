package accounts.account_model;

import java.util.List;

/**
 * Interface that defines the observer for account changes.
 */
public interface AccountObserver {
    /**
     * Method called when the account list is updated.
     *
     * @param accountsList the updated list of accounts
     */
    public void onNotify(List<Account> accountsList);
}
