package net.johanneslink.eurocalc.ebt;

import net.johanneslink.eurocalc.*;
import org.assertj.core.api.*;
import org.assertj.core.data.*;
import org.mockito.*;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;

@Group
@Label("EuroConverter")
class EuroConverterTests {

	@Group
	class Collaboration_Tests {

		@Example
		void calls_RateProvider_with_foreign_currency_first() throws RateNotAvailable {
			RateProvider provider = Mockito.mock(RateProvider.class);
			Mockito.when(provider.rate("USD", "EUR")).thenReturn(0.5);

			double euroAmount = new EuroConverter(provider).convert(10.0, "USD");
			Assertions.assertThat(euroAmount).isCloseTo(5.0, Offset.offset(0.01));
		}

		@Example
		void illegal_currencies_are_not_handed_to_rate_provider() throws RateNotAvailable {
			RateProvider provider = Mockito.mock(RateProvider.class);

			Assertions.assertThatThrownBy(() -> new EuroConverter(provider).convert(8.0, "ab"))
					  .isInstanceOf(IllegalArgumentException.class);

			Mockito.verify(provider, Mockito.never()).rate(Mockito.anyString(), Mockito.anyString());
		}

		@Example
		void can_handle_positive_exchange_rate() {
			RateProvider provider = (fromCurrency, toCurrency) -> 0.8;
			double euroAmount = new EuroConverter(provider).convert(8.0, "USD");
			Assertions.assertThat(euroAmount).isCloseTo(6.4, Offset.offset(0.01));
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

		@Example
		void can_handle_maximum_rate() {
			RateProvider provider = (fromCurrency, toCurrency) -> RateProvider.MAXIMUM_RATE;
			double euroAmount = new EuroConverter(provider).convert(8.0, "USD");
			Assertions.assertThat(euroAmount).isGreaterThanOrEqualTo(0.01);
		}

		@Example
		void can_handle_minimum_rate() {
			RateProvider provider = (fromCurrency, toCurrency) -> RateProvider.MINIMUM_RATE;
			double euroAmount = new EuroConverter(provider).convert(10.0, "USD");
			Assertions.assertThat(euroAmount).isGreaterThanOrEqualTo(0.01);
		}

	}

}
