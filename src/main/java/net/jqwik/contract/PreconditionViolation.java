package net.jqwik.contract;

public class PreconditionViolation extends ContractViolation {
	public PreconditionViolation() {
	}

	public PreconditionViolation(String message, Throwable cause) {
		super(message, cause);
	}
}
