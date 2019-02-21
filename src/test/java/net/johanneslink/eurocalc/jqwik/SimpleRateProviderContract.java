package net.johanneslink.eurocalc.jqwik;

import net.johanneslink.eurocalc.*;

import net.jqwik.api.*;

@Group
@Label("SimpleRateProvider")
class SimpleRateProviderContract implements RateProviderContract<SimpleRateProvider> {
	@Override
	public SimpleRateProvider createNewRateProvider() {
		return new SimpleRateProvider();
	}
}
