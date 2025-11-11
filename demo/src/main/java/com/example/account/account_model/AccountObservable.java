package com.example.account.account_model;

public interface AccountObservable {
    void addObserver(AccountObserver observer);
    void removeObserver(AccountObserver observer);
    void notifyObservers();
}
