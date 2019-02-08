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
			try {
				checkPrecondition(method, args);
				Object result = method.invoke(object, args);
				checkPostcondition(method, args, result);
				return result;
			} catch (InvocationTargetException t) {
				return throwAsUncheckedException(t.getCause());
			}
		};
	}

	private void checkPostcondition(Method method, Object[] args, Object result) {
		findEnsureMethod(method.getName(), method.getParameterTypes(), method.getReturnType())
			.ifPresent(precondition -> {
				try {
					boolean check = (boolean) precondition.invoke(contract, append(args, result));
					if (!check) {
						throw new PostconditionViolation();
					}
				} catch (InvocationTargetException e) {
					throw new PostconditionViolation(e.getCause().getMessage(), e.getCause());
				} catch (IllegalAccessException e) {
					throw new PostconditionViolation("Illegal Access", e);
				}
			});

	}

	private void checkPrecondition(Method method, Object[] args) {
		findRequireMethod(method.getName(), method.getParameterTypes())
			.ifPresent(precondition -> {
				try {
					boolean check = (boolean) precondition.invoke(contract, args);
					if (!check) {
						throw new PreconditionViolation();
					}
				} catch (InvocationTargetException e) {
					throw new PreconditionViolation(e.getCause().getMessage(), e.getCause());
				} catch (IllegalAccessException e) {
					throw new PreconditionViolation(e.getMessage(), e);
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

	/**
	 * Throw the supplied {@link Throwable}, <em>masked</em> as an
	 * unchecked exception.
	 *
	 * @param t type returns a throwable to make usage simpler
	 */
	public static <T> T throwAsUncheckedException(Throwable t) {
		throwAs(t);

		// Will never get here
		return null;
	}

	@SuppressWarnings("unchecked")
	private static <T extends Throwable> void throwAs(Throwable t) throws T {
		throw (T) t;
	}

}
