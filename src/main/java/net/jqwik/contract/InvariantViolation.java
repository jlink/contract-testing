package net.jqwik.contract;

public class InvariantViolation extends PostconditionViolation {
	public InvariantViolation() {
	}

	public InvariantViolation(String message, Throwable cause) {
		super(message, cause);
	}
}
