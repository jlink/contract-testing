package net.johanneslink.banking;

public class AccountOperationFailed extends Exception {

	public AccountOperationFailed(String message) {
		super(message);
	}
}
