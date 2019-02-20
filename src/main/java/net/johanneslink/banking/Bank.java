package net.johanneslink.banking;

import java.util.*;

import org.checkerframework.checker.nullness.qual.*;

public interface Bank {

	Optional<Account> accountById(@NonNull String id);

	Account createAccount(@NonNull String customer) throws CannotCreateAccount;

	List<Account> allAccounts();

	void register(TransactionFinishedListener listener);

	void transfer(@NonNull String sourceId, @NonNull String targetId, int amount, @NonNull String transactionId);

	interface TransactionFinishedListener {
		void success(String transactionId);

		void failed(String transactionId, String reason);
	}
}
