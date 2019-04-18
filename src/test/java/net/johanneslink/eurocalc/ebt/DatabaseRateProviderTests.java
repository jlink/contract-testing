package net.johanneslink.eurocalc.ebt;

import net.johanneslink.eurocalc.*;
import org.junit.jupiter.api.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DatabaseRateProviderTests {

	@Nested
	class Functional_Tests {
		@Test
		void rates_data_is_loaded_on_startup() {
		}

		@Test
		void rates_data_can_be_refreshed() {
		}

		@Test
		void supported_rates_can_be_configured() {
		}
	}

	@Nested
	class Contract_Tests implements RateProviderContractTests {

		@Override
		public RateProvider createProvider() {
			return new DatabaseRateProvider();
		}
	}

}
