package net.johanneslink.banking;

import java.util.*;

import org.checkerframework.checker.nullness.qual.*;

public interface Bank {

	Optional<Account> accountById(@NonNull String id);

	Account createAccount(@NonNull String id) throws AccountOperationFailed;

	List<Account> allAccounts();

	void transfer(@NonNull Account source, @NonNull Account target, int amount) throws TransactionFailed;

	void disableAccount(@NonNull Account account) throws AccountOperationFailed;

	void enableAccount(@NonNull Account account) throws AccountOperationFailed;

	boolean isAccountEnabled(@NonNull Account account);
}
