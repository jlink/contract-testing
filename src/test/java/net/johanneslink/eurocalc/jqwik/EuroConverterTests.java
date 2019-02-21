package net.johanneslink.eurocalc.jqwik;

import net.johanneslink.eurocalc.*;
import org.assertj.core.api.*;
import org.assertj.core.data.*;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;

@Group
class EuroConverterTests {

	@Group
	class FunctionalProperties {
		@Property
		boolean knownCurrenciesAreAlwaysConvertedToPositiveEuroAmount(
				@ForAll("nonEuroCurrencies") String from,
				@ForAll @DoubleRange(min = 0.01, max = 1000000.0) double amount,
				@ForAll("rateProvider") RateProvider provider
		) {

			double euroAmount = new EuroConverter(provider).convert(amount, from);
			return euroAmount > 0.0;
		}
	}

	@Group
	@Label("Collaboration with RateProvider")
	class CollaborationWithRateProvider {

		@Example
		void willCorrectlyUseExchangeRate() {
			RateProvider provider = (fromCurrency, toCurrency) -> 0.8;
			double euroAmount = new EuroConverter(provider).convert(8.0, "USD");
			Assertions.assertThat(euroAmount).isCloseTo(6.4, Offset.offset(0.01));
		}

		@Example
		@Disabled("not implemented yet")
		void canHandleRateNotAvailableException() {
			Assertions.fail("not implemented yet");
		}

	}

	@Provide
	Arbitrary<String> nonEuroCurrencies() {
		return Arbitraries.of("USD", "CHF", "CAD");
	}

	@Provide
	Arbitrary<RateProvider> rateProvider() {
		DoubleArbitrary rate = Arbitraries.doubles().between(0.1, 10.0);
		return rate.map(exchangeRate -> (fromCurrency, toCurrency) -> {
			Assertions.assertThat(fromCurrency).isNotEqualTo(toCurrency);
			return exchangeRate;
		});
	}

}
