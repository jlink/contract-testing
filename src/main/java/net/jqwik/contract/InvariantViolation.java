package net.jqwik.contract;

public class InvariantViolation extends ContractViolation {
	public InvariantViolation() {
	}

	public InvariantViolation(String message, Throwable cause) {
		super(message, cause);
	}
}
