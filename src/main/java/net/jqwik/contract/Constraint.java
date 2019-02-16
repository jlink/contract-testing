package net.jqwik.contract;

public interface Constraint<T> {

	boolean isValid(T value);
}
