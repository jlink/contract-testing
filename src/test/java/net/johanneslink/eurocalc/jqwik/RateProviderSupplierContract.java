package net.johanneslink.eurocalc.jqwik;

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
			return result.value().get() > 0.0;
		}
		return result.throwable().map(throwable -> throwable instanceof RateNotAvailable).orElse(false);
	}

	@Invariant
	public boolean anInvariant(RateProvider instance) {
		return true;
	}
}
