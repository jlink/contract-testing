package net.johanneslink.eurocalc.pbt;

import net.johanneslink.eurocalc.*;
import net.johanneslink.eurocalc.ebt.*;
import org.junit.jupiter.api.*;

import net.jqwik.api.*;

@Label("DatabaseRateProvider")
class DatabaseRateProviderProperties {

	@Group
	class Contract_Properties implements RateProviderContractProperties<DatabaseRateProvider> {

		@Override
		public DatabaseRateProvider createProvider() {
			return new DatabaseRateProvider();
		}
	}

}
