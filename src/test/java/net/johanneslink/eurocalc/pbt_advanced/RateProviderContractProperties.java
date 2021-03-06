package net.johanneslink.eurocalc.pbt_advanced;

import net.johanneslink.eurocalc.*;

import net.jqwik.api.*;
import net.jqwik.contract.*;

interface RateProviderContractProperties<E extends RateProvider> {

	@Property
	@Report(Reporting.GENERATED)
	default void rate_provider_contract_is_obeyed(
			@ForAll("currencies") String currency1,
			@ForAll("currencies") String currency2,
			@ForAll("rateProvider") @Contract(RateProviderSupplierContract.class) E provider
	) {
		try {
			provider.rate(currency1, currency2);
		} catch (PreconditionViolation ignored) {
		} catch (IllegalArgumentException | RateNotAvailable ignored) { }
	}

	@Provide
	default Arbitrary<String> currencies() {
		return Arbitraries.oneOf(supportedCurrencies(), obsoleteCurrencies(), unknownCurrencies());
	}

	default Arbitrary<String> supportedCurrencies() {
		return Arbitraries.of("USD", "EUR", "CHF", "CAD", "DEM", "FRF");
	}

	default Arbitrary<String> obsoleteCurrencies() {
		return Arbitraries.of("DEM", "FRF");
	}

	default Arbitrary<String> unknownCurrencies() {
		return Arbitraries.strings().alpha().ofLength(3);
	}

	@Provide
	default Arbitrary<RateProvider> rateProvider() {
		return Arbitraries.create(this::createProvider);
	}

	E createProvider();

}
