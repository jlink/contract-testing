package net.jqwik.contract;

public class PostconditionViolation extends ContractViolation {
	public PostconditionViolation() {
	}

	public PostconditionViolation(String message, Throwable cause) {
		super(message, cause);
	}
}
