package net.jqwik.contract;

import java.lang.annotation.*;

public interface SupplierContract<T> {

	@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface Require {

	}

	@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface Ensure {

	}

	@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface Invariant {

	}

	default T wrap(T object, Class<T> type) {
		return ContractBuilder.build(this, type).wrap(object);
	}

	final class Result<T> {
		private T result;
		private Throwable throwable;

		public static <T> Result<T> success(T result) {
			return new Result<>(result, null);
		}

		public static <T> Result<T> failure(Throwable throwable) {
			return new Result<>(null, throwable);
		}

		private Result(T result, Throwable throwable) {
			this.result = result;
			this.throwable = throwable;
		}

		public T get() {
			if (result != null) return result;
			return ContractBuilder.throwAsUncheckedException(throwable);
		}
	}
}
