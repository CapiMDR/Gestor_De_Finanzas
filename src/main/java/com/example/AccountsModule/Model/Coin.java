package com.example.AccountsModule.Model;

/**
 * Enum representing the currency types supported by the system.
 * Stores the currency code and its visual symbol.
 */
public enum Coin {
    MXN("$"),  // Pesos Mexicanos
    USD("$"),  // Dólares Americanos
    EUR("€"),  // Euro
    GBP("£");  // Libra Esterlina

    private final String symbol;

    Coin(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the symbol to be displayed in the UI (e.g. "$").
     */
    @Override
    public String toString() {
        return symbol;
    }
}