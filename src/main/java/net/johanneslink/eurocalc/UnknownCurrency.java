package net.johanneslink.eurocalc;

public class UnknownCurrency extends Exception {
	public UnknownCurrency(String unknownCurrency) {
		super(String.format("Currency [%s] is unknown", unknownCurrency));
	}
}
