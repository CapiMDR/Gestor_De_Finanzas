package com.example.account.account_model;

import java.util.ArrayList;
import java.util.List;

public class AccountManagerSubject implements AccountObservable{
    private List<AccountObserver> observers = new ArrayList<>();

    public void addObserver(AccountObserver observer){
        observers.add(observer);
    }
    public void removeObserver(AccountObserver observer){
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (AccountObserver observer : observers){
            observer.update();
        } 
    }
    
}
