package net.johanneslink.eurocalc.pbt;

import net.johanneslink.eurocalc.*;
import org.assertj.core.api.*;

import net.jqwik.api.*;
import net.jqwik.contract.*;

interface RateProviderContract<E extends RateProvider> {

	@Property
	default boolean willReturnRateAboveZeroForValidCurrencies(
			@ForAll("currencies") String from,
			@ForAll("currencies") String to,
			@ForAll("rateProvider") @Contract(RateProviderSupplierContract.class) E provider
	) throws RateNotAvailable {
		Assume.that(!from.equals(to));

		// This should be done automatically:
		RateProvider wrapped = new RateProviderSupplierContract().wrap(provider, RateProvider.class);

		return wrapped.rate(from, to) > 0.0;
	}

	@Property
	default void invalidCurrenciesWillBeRejected(
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
		return Arbitraries.of("A", "", null, "CADCAD");
	}

	@Provide
	default Arbitrary<E> rateProvider() {
		return Arbitraries.create(this::createNewRateProvider);
	}

	abstract E createNewRateProvider();

}
