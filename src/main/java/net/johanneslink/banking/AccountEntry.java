package net.johanneslink.banking;

import java.util.*;

import org.checkerframework.checker.nullness.qual.*;

public final class AccountEntry {

	private final EntryType type;
	private final int amount;
	private final String description;

	public enum EntryType {DEBIT, CREDIT}

	public AccountEntry(@NonNull EntryType type, int amount, @Nullable String description) {
		this.type = type;
		this.amount = amount;
		this.description = description;
	}

	public EntryType type() {
		return type;
	}

	public int amount() {
		return amount;
	}

	public Optional<String> description() {
		return Optional.ofNullable(description);
	}
}
