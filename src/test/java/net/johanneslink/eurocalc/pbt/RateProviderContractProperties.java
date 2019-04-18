package net.johanneslink.eurocalc.pbt;

import net.johanneslink.eurocalc.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import net.jqwik.api.*;

interface RateProviderContractProperties<E extends RateProvider> {

	E createProvider();

	@Property
	default void throws_IllegalArgumentException_for_unknown_currency(
			@ForAll("unknownCurrencies") String unknownCurrency,
			@ForAll("knownCurrencies") String knownCurrency
	) {
		RateProvider provider = createProvider();
		Assertions.assertThatThrownBy(() -> provider.rate(unknownCurrency, knownCurrency))
				  .isInstanceOf(IllegalArgumentException.class);
		Assertions.assertThatThrownBy(() -> provider.rate(knownCurrency, unknownCurrency))
				  .isInstanceOf(IllegalArgumentException.class);
	}

	@Provide
	default Arbitrary<String> unknownCurrencies() {
		return Arbitraries.of("ABC", "XYZ", "RRR");
	}

	@Provide
	default Arbitrary<String> knownCurrencies() {
		return Arbitraries.of("USD", "EUR", "CHF", "CAD", "DEM");
	}

	@Property
	default void throws_RateNotAvailable_for_obsolete_currency(
			@ForAll("supportedCurrencies") String supportedCurrency,
			@ForAll("obsoleteCurrencies") String obsoleteCurrency
	) {
		RateProvider provider = createProvider();
		Assertions.assertThatThrownBy(() -> provider.rate(supportedCurrency, obsoleteCurrency))
				  .isInstanceOf(RateNotAvailable.class);
		Assertions.assertThatThrownBy(() -> provider.rate(obsoleteCurrency, supportedCurrency))
				  .isInstanceOf(RateNotAvailable.class);
	}

	@Provide
	default Arbitrary<String> obsoleteCurrencies() {
		return Arbitraries.of("DEM");
	}

	@Provide
	default Arbitrary<String> supportedCurrencies() {
		return Arbitraries.of("USD", "EUR", "CHF", "CAD");
	}

	@Property
	default void rates_are_always_between_allowed_min_and_max(
			@ForAll("supportedCurrencies") String currency1,
			@ForAll("supportedCurrencies") String currency2
	) throws RateNotAvailable {
		Assume.that(!currency1.equals(currency2));
		assertRateWithinBounds(currency1, currency2);
	}

	default void assertRateWithinBounds(String from, String to) throws RateNotAvailable {
		RateProvider provider = createProvider();
		double rate = provider.rate(from, to);
		Assertions.assertThat(rate).isGreaterThanOrEqualTo(RateProvider.MINIMUM_RATE);
		Assertions.assertThat(rate).isLessThanOrEqualTo(RateProvider.MAXIMUM_RATE);
	}

	@Property
	default void inverse_rates_make_money(
			@ForAll("supportedCurrencies") String currency1,
			@ForAll("supportedCurrencies") String currency2
	) throws RateNotAvailable {
		Assume.that(!currency1.equals(currency2));
		assertInverseRatesMakeMoney(currency1, currency2);
	}

	default void assertInverseRatesMakeMoney(String currency1, String currency2) throws RateNotAvailable {
		RateProvider provider = createProvider();
		double rate = provider.rate(currency1, currency2);
		double inverse = provider.rate(currency2, currency1);
		double ratio = rate * inverse;
		Assertions.assertThat(ratio).isLessThan(1.0);
	}
}
