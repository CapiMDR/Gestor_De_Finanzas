package com.example.account.account_model;
import java.math.BigDecimal;

public class Account {

    public enum AccountType {
        CASH,
        DIGITAL
    }

    public enum Coin {
        USD,
        MXN
    }

    private int id;
    private String name;
    private AccountType type;
    private Coin coin;
    private BigDecimal balance;

    public Account(int id, String name, AccountType type, Coin coin, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.coin = coin;
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }
    
    public void updateBalance(BigDecimal amount){
        balance = balance.add(amount);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AccountType getType() {
        return type;
    }

    public Coin getCoin() {
        return coin;
    }

    @Override
    public String toString() {
        return "Account [id=" + id + ", name=" + name + ", type=" + type + ", coin=" + coin + ", balance=" + balance
                + "]";
    }
    
    
}
