package net.jqwik.contract;

public class ContractViolation extends AssertionError {

	public ContractViolation() {
	}

	public ContractViolation(String message, Throwable cause) {
		super(message, cause);
	}
}
