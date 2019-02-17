package net.johanneslink.eurocalc;

import java.util.*;

import net.jqwik.contract.*;

class CurrencyConstraint implements Constraint<String> {

	@Override
	public boolean isValid(String value) {
		if (value == null) {
			return false;
		}
		return value.trim().length() == 3;
	}
}
