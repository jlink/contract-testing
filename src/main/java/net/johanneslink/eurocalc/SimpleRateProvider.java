package net.johanneslink.eurocalc;

public class SimpleRateProvider implements RateProvider {
	@Override
	public double rate(String fromCurrency, String toCurrency) throws UnknownCurrency {
		checkCurrencyValid(fromCurrency);
		checkCurrencyValid(toCurrency);
		return 1.0;
	}

	private void checkCurrencyValid(String currency) throws UnknownCurrency {
		switch (currency) {
			case "EUR":
				break;
			case "USD":
				break;
			case "CHF":
				break;
			case "CAD":
				break;
			default: {
				throw new UnknownCurrency(currency);
			}
		}
	}
}
