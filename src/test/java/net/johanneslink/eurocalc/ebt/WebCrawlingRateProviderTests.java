package net.johanneslink.eurocalc.ebt;

import net.johanneslink.eurocalc.*;
import org.junit.jupiter.api.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WebCrawlingRateProviderTests {

	@Nested
	class Contract_Tests implements RateProviderContractTests {

		@Override
		public RateProvider createProvider() {
			return new WebCrawlingRateProvider();
		}
	}

}
