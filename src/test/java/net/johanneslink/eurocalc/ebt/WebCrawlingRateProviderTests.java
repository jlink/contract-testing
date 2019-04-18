package net.johanneslink.eurocalc.ebt;

import net.johanneslink.eurocalc.*;

import net.jqwik.api.*;

@Label("WebCrawlingRateProvider")
class WebCrawlingRateProviderTests {

	@Group
	class Contract_Tests implements RateProviderContractTests {

		@Override
		public RateProvider createProvider() {
			//TODO: Implement WebCrawlingRateProvider
			return new DatabaseRateProvider();
		}
	}

}
