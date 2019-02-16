package net.johanneslink.banking;

public class TransactionFailed extends Exception {

	public TransactionFailed(String message) {
		super(message);
	}
}
