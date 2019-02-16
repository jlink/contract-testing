package net.johanneslink.eurocalc;

import net.jqwik.contract.*;

// Constraints should be checked during postcondition checking of a contract
class RateConstraint implements Constraint<Double> {

	@Override
	public boolean isValid(Double value) {
		return value > 0.0;
	}
}
