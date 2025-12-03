package accounts.account_model;

import java.util.List;

public interface AccountObserver {
    public void onNotify(List<Account> accountsList);
}
