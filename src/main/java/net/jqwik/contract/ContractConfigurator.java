package net.jqwik.contract;

import net.jqwik.api.*;
import net.jqwik.api.configurators.*;
import net.jqwik.api.providers.*;

public class ContractConfigurator implements ArbitraryConfigurator {

	@Override
	public <T> Arbitrary<T> configure(Arbitrary<T> arbitrary, TypeUsage targetType) {
		if (!targetType.isAnnotated(Contract.class)) {
			return arbitrary;
		}

		Arbitrary[] a = new Arbitrary[]{arbitrary};

		targetType.findAnnotation(Contract.class).ifPresent(contract -> {
			Class<? extends SupplierContract> supplierContractClass = contract.value();
			try {
				SupplierContract supplierContract = supplierContractClass.newInstance();
				a[0] = a[0].map(supplierContract::wrap);
			} catch (Throwable e) {
				e.printStackTrace();
			}

		});
		return a[0];
	}
}
