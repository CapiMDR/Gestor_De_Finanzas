package accounts.account_model;

import java.util.List;

public interface AccountObserver {
    void onNotify(List<Account> accountsList);
}
