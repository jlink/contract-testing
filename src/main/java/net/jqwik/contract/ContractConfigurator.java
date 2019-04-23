package net.jqwik.contract;

import net.jqwik.api.*;
import net.jqwik.api.configurators.*;
import net.jqwik.api.providers.*;

public class ContractConfigurator implements ArbitraryConfigurator {

	public Arbitrary configure(Arbitrary arbitrary, Contract contract) throws IllegalAccessException, InstantiationException {
		Class<? extends SupplierContract> supplierContractClass = contract.value();
		SupplierContract supplierContract = supplierContractClass.newInstance();
		return null;
	}

	@Override
	public <T> Arbitrary<T> configure(Arbitrary<T> arbitrary, TypeUsage targetType) {
		if (!targetType.isAnnotated(Contract.class)) {
			return arbitrary;
		}

		Arbitrary a[] = new Arbitrary[] {arbitrary};

		targetType.findAnnotation(Contract.class).ifPresent(contract -> {
			Class<? extends SupplierContract> supplierContractClass = contract.value();
			try {
				SupplierContract supplierContract = supplierContractClass.newInstance();
				a[0] = a[0].map(o -> {
					// TODO: Does not work, since this should be the target interface type
					Class<?> rawType = targetType.getRawType();
					return supplierContract.wrap(o, rawType);
				});
			} catch (Throwable e) {
				e.printStackTrace();
			}

		});
		return a[0];
	}
}
