package net.johanneslink.eurocalc.pbt_advanced;

import org.checkerframework.checker.nullness.qual.*;

import net.jqwik.contract.*;

class CurrencyConstraint implements Constraint<String> {

	@Override
	//TODO Use cherframework annotations
	public boolean isValid(@NonNull String value) {
		if (value == null) {
			return false;
		}
		return value.length() == 3;
	}
}
