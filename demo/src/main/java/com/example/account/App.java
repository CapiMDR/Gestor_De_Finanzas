package com.example.account;

import java.math.BigDecimal;

import com.example.account.account_cotroller.AccountController;
import com.example.account.account_model.Account;
import com.example.account.account_model.AccountManager;
import com.example.account.account_model.AccountManagerSubject;
import com.example.account.account_view.AccountView;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        AccountManagerSubject subject = new AccountManagerSubject();
        AccountManager model = new AccountManager(subject);
        AccountView view = new AccountView();
        AccountController controller = new AccountController(model, view);

        String name = "Cuenta principal";
        String typeString = "CASH";
        String coinString = "USD";
        int balanceInt = 1000;

        Account.AccountType type = Account.AccountType.valueOf(typeString.toUpperCase());
        Account.Coin coin = Account.Coin.valueOf(coinString.toUpperCase());
        BigDecimal balance = BigDecimal.valueOf(balanceInt);

        controller.addAccount(name, type, coin, balance);

        controller.showAccounts();
    }
}
