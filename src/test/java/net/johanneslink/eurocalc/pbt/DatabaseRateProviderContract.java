package net.johanneslink.eurocalc.pbt;

import net.johanneslink.eurocalc.*;

import net.jqwik.api.*;

@Group
@Label("DatabaseRateProvider")
class DatabaseRateProviderContract implements RateProviderContract<DatabaseRateProvider> {
	@Override
	public DatabaseRateProvider createNewRateProvider() {
		return new DatabaseRateProvider();
	}
}
