package net.johanneslink.eurocalc;

import org.assertj.core.api.*;
import org.assertj.core.data.*;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;
import net.jqwik.contract.*;

@Group
@Label("Contract: RateProvider")
class RateProviderContractProperties {

	public static class RateProviderSupplierContract implements SupplierContract<RateProvider> {
		@Require
		public boolean rate(
				@ConstrainedBy(CurrencyConstraint.class) String fromCurrency,
				@ConstrainedBy(CurrencyConstraint.class) String toCurrency
		) {
			return !fromCurrency.equals(toCurrency);
		}

		@Ensure
		public boolean rate(String fromCurrency, String toCurrency, Result<@ConstrainedBy(RateConstraint.class) Double> result) {
			return result.get() > 0.0;
		}

		@Invariant
		public boolean anInvariant(RateProvider instance) {
			return true;
		}
	}

	interface RateProviderContract<E extends RateProvider> {

		@Property
		default boolean willReturnRateAboveZeroForValidCurrencies(
				@ForAll("currencies") String from,
				@ForAll("currencies") String to,
				@ForAll("rateProvider") @Contract(RateProviderSupplierContract.class) E provider
		) {
			Assume.that(!from.equals(to));

			// This should be done automatically:
			provider = (E) new RateProviderSupplierContract().wrap(provider, RateProvider.class);

			return provider.rate(from, to) > 0.0;
		}

		@Property
		default void willThrowExceptionsForInvalidCurrencies(
				@ForAll("currencies") String valid,
				@ForAll("invalid") String invalid,
				@ForAll("rateProvider") @Contract(RateProviderSupplierContract.class) E provider
		) {
			// This should be done automatically:
			RateProvider wrapped = new RateProviderSupplierContract().wrap(provider, RateProvider.class);

			Assertions.assertThatThrownBy(() -> wrapped.rate(valid, invalid)).isInstanceOf(PreconditionViolation.class);
			Assertions.assertThatThrownBy(() -> wrapped.rate(invalid, valid)).isInstanceOf(PreconditionViolation.class);
		}

		@Provide
		default Arbitrary<String> currencies() {
			return Arbitraries.of("EUR", "USD", "CHF", "CAD");
		}

		@Provide
		default Arbitrary<String> invalid() {
			return Arbitraries.of("A", "", "XXX", "CADCAD");
		}

	}

	@Group
	@Label("SimpleRateProvider")
	class SimpleRateProviderTests implements RateProviderContract<SimpleRateProvider> {
		@Provide
		Arbitrary<SimpleRateProvider> rateProvider() {
			return Arbitraries.constant(new SimpleRateProvider());
		}
	}

	@Group
	@Label("Collaborator: EuroConverter")
	class EuroConverterCollaborationTests {

		@Property
		boolean willAlwaysConvertToPositiveEuroAmount(
				@ForAll("nonEuroCurrencies") String from,
				@ForAll @DoubleRange(min = 0.01, max = 1000000.0) double amount,
				@ForAll("rateProvider") RateProvider provider
		) {

			double euroAmount = new EuroConverter(provider).convert(amount, from);
			return euroAmount > 0.0;
		}

		@Example
		void willCorrectlyUseExchangeRate() {
			RateProvider provider = (fromCurrency, toCurrency) -> 0.8;
			double euroAmount = new EuroConverter(provider).convert(8.0, "USD");
			Assertions.assertThat(euroAmount).isCloseTo(6.4, Offset.offset(0.01));
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

}
