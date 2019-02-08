package net.jqwik.contract;

abstract class ContractViolation extends AssertionError {

	ContractViolation() {
	}

	ContractViolation(String message, Throwable cause) {
		super(message, cause);
	}
}
