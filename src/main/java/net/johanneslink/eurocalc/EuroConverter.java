package net.johanneslink.eurocalc;

public class EuroConverter {
	private final RateProvider rateProvider;

	public EuroConverter(RateProvider rateProvider) {
		this.rateProvider = rateProvider;
	}

	public double convert(double amount, String fromCurrency) {
		if (fromCurrency.length() != 3) {
			throw new IllegalArgumentException(fromCurrency);
		}
		try {
			return rateProvider.rate(fromCurrency, "EUR") * amount;
		} catch (RateNotAvailable unknownCurrency) {
			return 0.0;
		}
	}
}
