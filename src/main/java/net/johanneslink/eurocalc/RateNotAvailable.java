package net.johanneslink.eurocalc;

public class RateNotAvailable extends Exception {
	public RateNotAvailable(String unknownCurrency) {
		super(String.format("Currency [%s] is unknown", unknownCurrency));
	}
}
