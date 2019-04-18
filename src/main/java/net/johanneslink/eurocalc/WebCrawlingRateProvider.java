package net.johanneslink.eurocalc;

import java.util.*;

// TODO: Provide a different implementation from DatabaseRateProvider
public class WebCrawlingRateProvider implements RateProvider {

	private static Map<String, Double> rates = new HashMap<>();

	static {
		rates.put("USDEUR", 0.8);
		rates.put("EURUSD", 1.2);
		rates.put("CHFEUR", 0.9);
		rates.put("EURCHF", 1.1);
		rates.put("CADEUR", 0.8);
		rates.put("EURCAD", 1.3);
		rates.put("USDCHF", 0.9);
		rates.put("CHFUSD", 1.1);
		rates.put("USDCAD", 1.1);
		rates.put("CADUSD", 0.9);
		rates.put("CHFCAD", 1.1);
		rates.put("CADCHF", 0.9);
	}

	@Override
	public double rate(String fromCurrency, String toCurrency) throws RateNotAvailable {
		checkCurrencyValid(fromCurrency);
		checkCurrencyValid(toCurrency);
		return rates.get(fromCurrency + toCurrency);
	}

	private void checkCurrencyValid(String currency) throws RateNotAvailable {
		switch (currency) {
			case "EUR":
				break;
			case "USD":
				break;
			case "CHF":
				break;
			case "CAD":
				break;
			case "DEM":
				throw new RateNotAvailable(currency);
			default: {
				throw new IllegalArgumentException(currency);
			}
		}
	}
}
