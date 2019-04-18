package net.johanneslink.eurocalc.ebt;

import net.johanneslink.eurocalc.*;

import net.jqwik.api.*;

@Label("DatabaseRateProvider")
class DatabaseRateProviderTests {

	@Group
	class Contract_Tests implements RateProviderContractTests {

		@Override
		public RateProvider createProvider() {
			return new DatabaseRateProvider();
		}
	}

}
