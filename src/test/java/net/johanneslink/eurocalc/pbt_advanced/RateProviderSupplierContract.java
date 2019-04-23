package net.johanneslink.eurocalc.pbt_advanced;

import net.johanneslink.eurocalc.*;

import net.jqwik.contract.*;

public class RateProviderSupplierContract implements SupplierContract<RateProvider> {
	@Require
	public boolean rate(
			@ConstrainedBy(CurrencyConstraint.class) String fromCurrency,
			@ConstrainedBy(CurrencyConstraint.class) String toCurrency
	) {
		return !fromCurrency.equals(toCurrency);
	}

	@Ensure
	public boolean rate(String fromCurrency, String toCurrency, Result<Double> result) {
		if (result.value().isPresent()) {
			Double rate = result.value().get();
			return rate >= RateProvider.MINIMUM_RATE && rate <= RateProvider.MAXIMUM_RATE;
		}
		return result.throwable()
					 .map(throwable ->  throwable instanceof RateNotAvailable ||
										throwable instanceof IllegalArgumentException)
					 .orElse(false);
	}

	@Invariant
	public boolean anInvariant(RateProvider instance) {
		return true;
	}
}
