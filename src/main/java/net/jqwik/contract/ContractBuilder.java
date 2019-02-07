package net.jqwik.contract;

import java.lang.reflect.*;
import java.util.*;

class ContractBuilder<T> {

	static <T> ContractBuilder<T> build(Contract<T> contract, Class<T> type) {
		return new ContractBuilder<>(contract, type);
	}

	private final Contract<T> contract;
	private final Class<T> contractType;

	private ContractBuilder(Contract<T> contract, Class<T> contractType) {
		this.contract = contract;
		this.contractType = contractType;
	}

	@SuppressWarnings("unchecked")
	T wrap(T object) {
		return (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{contractType}, createHandler(object));
	}

	private InvocationHandler createHandler(T object) {
		return (proxy, method, args) -> {
			checkPrecondition(method, args);
			Object result = method.invoke(object, args);
			checkPostcondition(method, args, result);
			return result;
		};
	}

	private void checkPostcondition(Method method, Object[] args, Object result) {
		findEnsureMethod(method.getName(), method.getParameterTypes(), method.getReturnType())
			.ifPresent(precondition -> {
				try {
					boolean check = (boolean) precondition.invoke(contract, append(args, result));
					if (!check) {
						throw new ContractViolation();
					}
				} catch (IllegalAccessException e) {
					throw new ContractViolation();
				} catch (InvocationTargetException e) {
					throw new ContractViolation();
				}
			});

	}

	private void checkPrecondition(Method method, Object[] args) {
		findRequireMethod(method.getName(), method.getParameterTypes())
			.ifPresent(precondition -> {
				try {
					boolean check = (boolean) precondition.invoke(contract, args);
					if (!check) {
						throw new ContractViolation();
					}
				} catch (IllegalAccessException e) {
					throw new ContractViolation(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					throw new ContractViolation(e.getCause().getMessage(), e.getCause());
				}
			});
	}

	private Optional<Method> findEnsureMethod(String methodName, Class<?>[] parameterTypes, Class<?> returnType) {
		try {
			Method method = contract.getClass().getDeclaredMethod(methodName, append(parameterTypes, returnType));
			if (method.getAnnotation(Contract.Ensure.class) != null)
				return Optional.of(method);
		} catch (NoSuchMethodException e) {
		}
		return Optional.empty();
	}

	static <T> T[] append(T[] arr, T element) {
		final int N = arr.length;
		arr = Arrays.copyOf(arr, N + 1);
		arr[N] = element;
		return arr;
	}

	private Optional<Method> findRequireMethod(String methodName, Class<?>[] parameterTypes) {
		try {
			Method method = contract.getClass().getDeclaredMethod(methodName, parameterTypes);
			if (method.getAnnotation(Contract.Require.class) != null)
				return Optional.of(method);
		} catch (NoSuchMethodException e) {
		}
		return Optional.empty();
	}
}
