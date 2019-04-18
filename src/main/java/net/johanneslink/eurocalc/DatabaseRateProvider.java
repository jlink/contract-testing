package net.johanneslink.eurocalc;

import java.util.*;

import org.jetbrains.annotations.*;

public class DatabaseRateProvider implements RateProvider {

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

	private static String[] supportedCurrencies = new String[]{"EUR", "USD", "CAD", "CHF"};

	private static String[] obsoleteCurrencies = new String[]{"DEM"};

	@Override
	public double rate(String fromCurrency, String toCurrency) throws RateNotAvailable {
		checkCurrencyValid(fromCurrency);
		checkCurrencyValid(toCurrency);
		return rates.get(fromCurrency + toCurrency);
	}

	private void checkCurrencyValid(String currency) throws RateNotAvailable {
		if (supportedCurrencies().contains(currency)) {
			return;
		}
		if (obsoleteCurrencies().contains(currency)) {
			throw new RateNotAvailable(currency);
		}
		throw new IllegalArgumentException(currency);
	}

	@NotNull
	private List<String> obsoleteCurrencies() {
		return Arrays.asList(obsoleteCurrencies);
	}

	@NotNull
	private List<String> supportedCurrencies() {
		return Arrays.asList(supportedCurrencies);
	}
}
