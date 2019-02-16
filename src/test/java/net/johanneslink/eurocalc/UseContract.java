package net.johanneslink.eurocalc;

import net.jqwik.contract.SupplierContract;

public @interface UseContract {

	Class<? extends SupplierContract> value();

}
