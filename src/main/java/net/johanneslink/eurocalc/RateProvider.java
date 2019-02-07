package net.johanneslink.eurocalc;

public interface RateProvider {
	double rate(String fromCurrency, String toCurrency);
}
