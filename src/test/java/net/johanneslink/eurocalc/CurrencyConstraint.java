package net.johanneslink.eurocalc;

import java.util.*;

import net.jqwik.contract.*;

class CurrencyConstraint implements Constraint<String> {

	private final Set<String> currencies = new HashSet<String>() {
		{
			add("EUR");
			add("USD");
			add("CHF");
			add("CAD");
		}
	};

	@Override
	public boolean isValid(String value) {
		return currencies.contains(value);
	}
}
