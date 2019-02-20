package net.johanneslink.banking;

import java.util.*;

public interface Account {

	int balance();

	String id();

	String customer();

	List<AccountEntry> history();

	void debit(int amount);

	void credit(int amount);
}
