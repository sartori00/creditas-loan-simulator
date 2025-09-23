package br.com.creditas.loansimulator.domain.model.enums;

public enum Currency {
    BRL("R$"),
    USD("$"),
    EUR("€"),
    GBP("£"),
    CNY("¥");

    private final String symbol;

    Currency(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
