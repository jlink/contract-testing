package net.johanneslink.eurocalc.pbt;

import net.johanneslink.eurocalc.*;

import net.jqwik.api.*;

@Group
@Label("SimpleRateProvider")
class SimpleRateProviderContract implements RateProviderContract<DatabaseRateProvider> {
	@Override
	public DatabaseRateProvider createNewRateProvider() {
		return new DatabaseRateProvider();
	}
}
