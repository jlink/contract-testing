package net.johanneslink.eurocalc.pbt_advanced;

import net.johanneslink.eurocalc.*;

import net.jqwik.api.*;

@Group
@Label("WebCrawlingRateProvider")
class WebCrawlingRateProviderContract implements RateProviderContractProperties<WebCrawlingRateProvider> {
	@Override
	public WebCrawlingRateProvider createProvider() {
		return new WebCrawlingRateProvider();
	}
}
