package net.johanneslink.eurocalc.pbt_advanced;

import net.johanneslink.eurocalc.*;
import org.assertj.core.api.*;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;

@Group
class EuroConverterProperties {

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
	class Collaboration_with_RateProvider {

		@Example
		void calls_RateProvider_with_foreign_currency_first() throws RateNotAvailable {
//			RateProvider provider = Mockito.mock(RateProvider.class);
//			Mockito.when(provider.rate("USD", "EUR")).thenReturn(0.5);
//
//			double euroAmount = new EuroConverter(provider).convert(10.0, "USD");
//			Assertions.assertThat(euroAmount).isCloseTo(5.0, Offset.offset(0.01));
		}

		@Example
		void can_handle_positive_exchange_rate() {
//			RateProvider provider = (fromCurrency, toCurrency) -> 0.8;
//			double euroAmount = new EuroConverter(provider).convert(8.0, "USD");
//			Assertions.assertThat(euroAmount).isCloseTo(6.4, Offset.offset(0.01));
		}

		@Example
		void can_handle_RateNotAvailableException() {
//			RateProvider provider = (fromCurrency, toCurrency) -> { throw new RateNotAvailable("DEM"); };
//			double euroAmount = new EuroConverter(provider).convert(8.0, "DEM");
//			Assertions.assertThat(euroAmount).isEqualTo(0.0);
		}

		@Example
		void can_handle_maximum_rate() {
//			RateProvider provider = (fromCurrency, toCurrency) -> RateProvider.MAXIMUM_RATE;
//			double euroAmount = new EuroConverter(provider).convert(8.0, "USD");
//			Assertions.assertThat(euroAmount).isGreaterThanOrEqualTo(0.01);
		}

		@Example
		void can_handle_minimum_rate() {
//			RateProvider provider = (fromCurrency, toCurrency) -> RateProvider.MINIMUM_RATE;
//			double euroAmount = new EuroConverter(provider).convert(10.0, "USD");
//			Assertions.assertThat(euroAmount).isGreaterThanOrEqualTo(0.01);
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
