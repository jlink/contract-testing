package net.johanneslink.eurocalc;

import net.jqwik.contract.Contract;

public @interface UseContract {

	Class<? extends Contract> value();

}
