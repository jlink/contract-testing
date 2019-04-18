package net.johanneslink.eurocalc.ebt;

import net.johanneslink.eurocalc.*;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("EuroConverter")
class EuroConverterTests {

	@Nested
	class Functional_Tests {
		@Test
		void rates_are_used_to_calculate_euro_amounts() throws RateNotAvailable {
			RateProvider provider = Mockito.mock(RateProvider.class);
			Mockito.when(provider.rate("USD", "EUR")).thenReturn(0.8);

			double euroAmount = new EuroConverter(provider).convert(12.0, "USD");
			Assertions.assertThat(euroAmount).isCloseTo(9.6, Offset.offset(0.01));
		}

		@Test
		void euro_amounts_are_properly_rounded() {
			// TODO
		}

		@Test
		void euro_to_euro_conversions_are_rejected() {
			// TODO
		}
	}

	@Nested
	class Collaboration_Tests {

		@Test
		void calls_RateProvider_with_foreign_currency_first() throws RateNotAvailable {
			RateProvider provider = Mockito.mock(RateProvider.class);
			Mockito.when(provider.rate("USD", "EUR")).thenReturn(0.5);

			double euroAmount = new EuroConverter(provider).convert(10.0, "USD");
			Assertions.assertThat(euroAmount).isGreaterThan(0.0);
			Mockito.verify(provider.rate("USD", "EUR"));
		}

		@Test
		void illegal_currencies_are_not_handed_to_rate_provider() throws RateNotAvailable {
			RateProvider provider = Mockito.mock(RateProvider.class);

			Assertions.assertThatThrownBy(() -> new EuroConverter(provider).convert(8.0, "ab"))
					  .isInstanceOf(IllegalArgumentException.class);

			Mockito.verify(provider, Mockito.never()).rate(Mockito.anyString(), Mockito.anyString());
		}

		@Test
		void can_handle_positive_exchange_rate() {
			RateProvider provider = (fromCurrency, toCurrency) -> 0.8;
			double euroAmount = new EuroConverter(provider).convert(8.0, "USD");
			Assertions.assertThat(euroAmount).isCloseTo(6.4, Offset.offset(0.01));
		}

		@Test
		void can_handle_RateNotAvailableException() {
			RateProvider provider = (fromCurrency, toCurrency) -> { throw new RateNotAvailable("DEM"); };
			double euroAmount = new EuroConverter(provider).convert(8.0, "DEM");
			Assertions.assertThat(euroAmount).isEqualTo(0.0);
		}

		@Test
		void can_handle_IllegalArgumentException() {
			RateProvider provider = (fromCurrency, toCurrency) -> { throw new IllegalArgumentException("XYZ"); };
			Assertions.assertThatThrownBy(() -> new EuroConverter(provider).convert(8.0, "XYZ"))
					  .isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void can_handle_maximum_rate() {
			RateProvider provider = (fromCurrency, toCurrency) -> RateProvider.MAXIMUM_RATE;
			double euroAmount = new EuroConverter(provider).convert(8.0, "USD");
			Assertions.assertThat(euroAmount).isGreaterThanOrEqualTo(0.01);
		}

		@Test
		void can_handle_minimum_rate() {
			RateProvider provider = (fromCurrency, toCurrency) -> RateProvider.MINIMUM_RATE;
			double euroAmount = new EuroConverter(provider).convert(10.0, "USD");
			Assertions.assertThat(euroAmount).isGreaterThanOrEqualTo(0.01);
		}

	}

}
