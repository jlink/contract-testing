package net.johanneslink.eurocalc;

public interface RateProvider {
	double MAXIMUM_RATE = 1000.0;
	double MINIMUM_RATE = 0.001;

	double rate(String fromCurrency, String toCurrency) throws RateNotAvailable;
}
