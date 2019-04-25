package net.johanneslink.eurocalc.pbt_advanced;

import net.johanneslink.eurocalc.*;

import net.jqwik.contract.*;

import static org.assertj.core.api.Assertions.*;

public class RateProviderSupplierContract implements SupplierContract<RateProvider> {
	@Override
	public Class<RateProvider> supplierType() {
		return RateProvider.class;
	}

	@Require
	public boolean rate(
			@ConstrainedBy(CurrencyConstraint.class) String fromCurrency,
			@ConstrainedBy(CurrencyConstraint.class) String toCurrency
	) {
		return !fromCurrency.equals(toCurrency);
	}

	@Ensure
	public void rate(String fromCurrency, String toCurrency, Result<Double> result) {
		result.onValue(value -> {
			assertThat(value).isGreaterThanOrEqualTo(RateProvider.MINIMUM_RATE);
			assertThat(value).isLessThanOrEqualTo(RateProvider.MAXIMUM_RATE);
		}).onThrowable(throwable -> {
			assertThat(throwable).isInstanceOfAny(
					RateNotAvailable.class,
					IllegalArgumentException.class
			);
		});
	}

	@Invariant
	public boolean anInvariant(RateProvider instance) {
		return true;
	}

}
