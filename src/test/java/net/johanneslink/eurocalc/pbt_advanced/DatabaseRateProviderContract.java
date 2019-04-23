package net.johanneslink.eurocalc.pbt_advanced;

import net.johanneslink.eurocalc.*;

import net.jqwik.api.*;

@Group
@Label("DatabaseRateProvider")
class DatabaseRateProviderContract implements RateProviderContractProperties<DatabaseRateProvider> {
	@Override
	public DatabaseRateProvider createProvider() {
		return new DatabaseRateProvider();
	}
}
