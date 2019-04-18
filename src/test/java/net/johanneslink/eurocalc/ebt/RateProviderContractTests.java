package net.johanneslink.eurocalc.ebt;

import net.johanneslink.eurocalc.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

interface RateProviderContractTests {

	RateProvider createProvider();

	@Test
	default void throws_IllegalArgumentException_for_unknown_currency() {
		RateProvider provider = createProvider();
		Assertions.assertThatThrownBy(() -> provider.rate("XYZ", "USD"))
				  .isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	default void throws_RateNotAvailable_for_obsolete_currency() {
		RateProvider provider = createProvider();
		Assertions.assertThatThrownBy(() -> provider.rate("DEM", "EUR"))
				  .isInstanceOf(RateNotAvailable.class);
	}

	@Test
	default void rates_are_always_between_allowed_min_and_max() throws RateNotAvailable {
		assertRateWithinBounds("USD", "EUR");
		assertRateWithinBounds("EUR", "USD");
		assertRateWithinBounds("CAD", "CHF");
	}

	default void assertRateWithinBounds(String from, String to) throws RateNotAvailable {
		RateProvider provider = createProvider();
		double rate = provider.rate(from, to);
		Assertions.assertThat(rate).isGreaterThanOrEqualTo(RateProvider.MINIMUM_RATE);
		Assertions.assertThat(rate).isLessThanOrEqualTo(RateProvider.MAXIMUM_RATE);
	}

	@Test
	default void inverse_rates_make_money() throws RateNotAvailable {
		assertInverseRatesMakeMoney("USD", "EUR");
		assertInverseRatesMakeMoney("CAD", "CHF");
	}

	default void assertInverseRatesMakeMoney(String currency1, String currency2) throws RateNotAvailable {
		RateProvider provider = createProvider();
		double rate = provider.rate(currency1, currency2);
		double inverse = provider.rate(currency2, currency1);
		double ratio = rate * inverse;
		Assertions.assertThat(ratio).isLessThan(1.0);
	}
}
