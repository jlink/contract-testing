package net.johanneslink.eurocalc.pbt;

import net.johanneslink.eurocalc.*;
import org.assertj.core.api.*;
import org.assertj.core.data.*;
import org.mockito.*;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import static net.johanneslink.eurocalc.RateProvider.*;

@Group
@Label("EuroConverter")
class EuroConverterProperties {

	@Group
	class Functional_Tests {
		@Property
		void rates_are_used_to_calculate_euro_amounts(
				@ForAll("nonEuroCurrencies") String nonEuroCurrency,
				@ForAll @DoubleRange(min = MINIMUM_RATE, max = MAXIMUM_RATE) double exchangeRate,
				@ForAll @DoubleRange(min = 0.01, max = 1_000_000.0) double amount
		) throws RateNotAvailable {
			RateProvider provider = Mockito.mock(RateProvider.class);
			Mockito.when(provider.rate(nonEuroCurrency, "EUR")).thenReturn(exchangeRate);

			double euroAmount = new EuroConverter(provider).convert(amount, nonEuroCurrency);
			double expected = amount * exchangeRate;
			Assertions.assertThat(euroAmount).isCloseTo(expected, Offset.offset(0.01));
		}

		@Example
		void euro_amounts_are_properly_rounded() {
			// TODO
		}

		@Example
		void euro_to_euro_conversions_are_rejected() {
			// TODO
		}
	}

	@Provide
	Arbitrary<String> nonEuroCurrencies() {
		return Arbitraries.of("USD", "CHF", "CAD");
	}

	@Provide
	Arbitrary<String> illegalCurrencies() {
		return Arbitraries.strings().alpha()
						  .ofMaxLength(10)
						  .map(String::toUpperCase)
						  .filter(currency -> currency.length() != 3);
	}

	@Group
	class Collaboration_Tests {

		@Property
		void calls_RateProvider_with_foreign_currency_first(
				@ForAll("nonEuroCurrencies") String nonEuroCurrency
		) throws RateNotAvailable {
			RateProvider provider = Mockito.mock(RateProvider.class);
			Mockito.when(provider.rate(nonEuroCurrency, "EUR")).thenReturn(0.5);

			double euroAmount = new EuroConverter(provider).convert(10.0, nonEuroCurrency);
			Assertions.assertThat(euroAmount).isGreaterThan(0.0);

			Mockito.verify(provider).rate(nonEuroCurrency, "EUR");
		}

		@Property
		void illegal_currencies_are_not_handed_to_rate_provider(
				@ForAll("illegalCurrencies") String illegalCurrency
		) throws RateNotAvailable {
			RateProvider provider = Mockito.mock(RateProvider.class);

			Assertions.assertThatThrownBy(() -> new EuroConverter(provider).convert(8.0, illegalCurrency))
					  .isInstanceOf(IllegalArgumentException.class);

			Mockito.verify(provider, Mockito.never()).rate(Mockito.anyString(), Mockito.anyString());
		}

		@Property
		void can_handle_any_exchange_rate(
				@ForAll @DoubleRange(min = MINIMUM_RATE, max = MAXIMUM_RATE) double exchangeRate,
				@ForAll @DoubleRange(min = 0.01, max = 1_000_000.0) double amount
		) {
			String collector = exchangeRate == MINIMUM_RATE ? "min"
									   : exchangeRate == MAXIMUM_RATE ? "max" : "other";
			Statistics.collect(collector);
			RateProvider provider = (fromCurrency, toCurrency) -> exchangeRate;
			double euroAmount = new EuroConverter(provider).convert(amount, "USD");
			Assertions.assertThat(euroAmount).isGreaterThan(0.0);
		}

		@Example
		@Disabled("Not needed any more")
		void can_handle_maximum_rate() {
			RateProvider provider = (fromCurrency, toCurrency) -> MAXIMUM_RATE;
			double euroAmount = new EuroConverter(provider).convert(8.0, "USD");
			Assertions.assertThat(euroAmount).isGreaterThanOrEqualTo(0.01);
		}

		@Example
		@Disabled("Not needed any more")
		void can_handle_minimum_rate() {
			RateProvider provider = (fromCurrency, toCurrency) -> MINIMUM_RATE;
			double euroAmount = new EuroConverter(provider).convert(10.0, "USD");
			Assertions.assertThat(euroAmount).isGreaterThanOrEqualTo(0.01);
		}

		@Example
		void can_handle_RateNotAvailableException() {
			RateProvider provider = (fromCurrency, toCurrency) -> { throw new RateNotAvailable("DEM"); };
			double euroAmount = new EuroConverter(provider).convert(8.0, "DEM");
			Assertions.assertThat(euroAmount).isEqualTo(0.0);
		}

		@Example
		void can_handle_IllegalArgumentException() {
			RateProvider provider = (fromCurrency, toCurrency) -> { throw new IllegalArgumentException("XYZ"); };
			Assertions.assertThatThrownBy(() -> new EuroConverter(provider).convert(8.0, "XYZ"))
					  .isInstanceOf(IllegalArgumentException.class);
		}

	}

}
