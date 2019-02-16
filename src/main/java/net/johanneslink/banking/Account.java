package net.johanneslink.banking;

import java.util.*;

public interface Account {

	int balance();

	void withdraw(int amount) throws TransactionFailed;

	void deposit(int amount)  throws TransactionFailed;

	List<Transaction> history();
}
