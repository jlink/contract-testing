package net.johanneslink.banking;

public final class Transaction {

	private final TransactionType type;
	private final int amount;

	public enum TransactionType { WITHDRAWAL, DEPOSITION }

	public Transaction(TransactionType type, int amount) {
		this.type = type;
		this.amount = amount;
	}

	public TransactionType type() {
		return type;
	}

	public int amount() {
		return amount;
	}
}
