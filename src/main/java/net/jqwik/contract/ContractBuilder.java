package net.jqwik.contract;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

import org.junit.platform.commons.support.*;

class ContractBuilder<T> {

	static <T> ContractBuilder<T> build(SupplierContract<T> contract, Class<T> type) {
		return new ContractBuilder<>(contract, type);
	}

	private final SupplierContract<T> contract;
	private final Class<T> contractType;

	private ContractBuilder(SupplierContract<T> contract, Class<T> contractType) {
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
			SupplierContract.Result result;
			try {
				result = SupplierContract.Result.success(method.invoke(object, args));
			} catch (InvocationTargetException ite) {
				result = SupplierContract.Result.failure(ite.getTargetException());
			} catch (Throwable throwable) {
				result = SupplierContract.Result.failure(throwable);
			}

			checkPostcondition(method, args, result);
			checkInvariants(object);
			if (result.throwable().isPresent()) {
				throw (Throwable) result.throwable().get();
			}
			return result.value().get();
		};
	}

	private void checkInvariants(T object) {
		for (Method invariant : findInvariantMethods()) {
			try {
				Object result = invariant.invoke(contract, object);
				if (!toBoolean(result)) {
					throw new InvariantViolation();
				}
			} catch (InvocationTargetException e) {
				throw new InvariantViolation(e.getCause().getMessage(), e.getCause());
			} catch (IllegalAccessException e) {
				throw new ContractMethodNotPublic(invariant);
			}
		}

	}

	private List<Method> findInvariantMethods() {
		return Arrays.stream(contract.getClass().getDeclaredMethods())
					 .filter(method -> method.getAnnotation(SupplierContract.Invariant.class) != null)
					 .filter(method -> method.getParameterTypes().length == 1)
					 .filter(method -> method.getParameterTypes()[0].equals(contractType))
					 .collect(Collectors.toList());
	}

	private void checkPostcondition(Method method, Object[] args, SupplierContract.Result result) {
		Optional<Method> ensureMethod = findEnsureMethod(method.getName(), method.getParameterTypes(), method.getReturnType());
		if (ensureMethod.isPresent()) {
			Method postcondition = ensureMethod.get();
			try {
				Object conditionResult = postcondition.invoke(contract, append(args, result));
				if (!toBoolean(conditionResult)) {
					throw new PostconditionViolation();
				}
			} catch (InvocationTargetException e) {
				throw new PostconditionViolation(e.getCause().getMessage(), e.getCause());
			} catch (IllegalAccessException e) {
				throw new ContractMethodNotPublic(postcondition);
			}
		}
	}

	private void checkPrecondition(Method method, Object[] args) {
		Optional<Method> requireMethod = findRequireMethod(method.getName(), method.getParameterTypes());
		if (requireMethod.isPresent()) {
			Method precondition = requireMethod.get();
			try {
				checkConstraints(precondition.getAnnotatedParameterTypes(), args);
				Object result = precondition.invoke(contract, args);
				if (!toBoolean(result)) {
					throw new PreconditionViolation();
				}
			} catch (InvocationTargetException e) {
				throw new PreconditionViolation(e.getCause().getMessage(), e.getCause());
			} catch (IllegalAccessException e) {
				throw new ContractMethodNotPublic(precondition);
			}
		}
	}

	private boolean toBoolean(Object result) {
		if (result == null) {
			return true;
		}
		if (boolean.class.isAssignableFrom(result.getClass())) {
			return (boolean) result;
		}
		if (Boolean.class.isAssignableFrom(result.getClass())) {
			return (boolean) result;
		}
		return true;
	}

	private void checkConstraints(AnnotatedType[] annotatedParameterTypes, Object[] args) {
		for (int i = 0; i < annotatedParameterTypes.length; i++) {
			AnnotatedType type = annotatedParameterTypes[i];
			Object arg = args[i];
			Optional<ConstrainedBy> annotation = AnnotationSupport.findAnnotation(type, ConstrainedBy.class);
			annotation.ifPresent(constrainedBy -> {
				Constraint constraint = ReflectionSupport.newInstance(constrainedBy.value());
				if (!constraint.isValid(arg)) {
					throw new PreconditionViolation();
				}
			});
		}
	}

	private Optional<Method> findEnsureMethod(String methodName, Class<?>[] parameterTypes, Class<?> returnType) {
		try {
			Method method = contract.getClass().getDeclaredMethod(methodName, append(parameterTypes, SupplierContract.Result.class));
			if (method.getAnnotation(SupplierContract.Ensure.class) != null) {
				return Optional.of(method);
			}
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
			if (method.getAnnotation(SupplierContract.Require.class) != null) {
				return Optional.of(method);
			}
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
