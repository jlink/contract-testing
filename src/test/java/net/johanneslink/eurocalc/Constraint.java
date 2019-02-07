package net.johanneslink.eurocalc;

public interface Constraint<T> {

	boolean isValid(T value);
}
