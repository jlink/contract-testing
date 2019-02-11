package net.johanneslink.eurocalc;

public class EuroConverter {
	private final RateProvider rateProvider;

	public EuroConverter(RateProvider rateProvider) {
		this.rateProvider = rateProvider;
	}

	public double convert(double amount, String fromCurrency) {
		return rateProvider.rate(fromCurrency, "EUR") * amount;
	}
}