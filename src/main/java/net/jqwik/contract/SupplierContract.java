package net.jqwik.contract;

import java.lang.annotation.*;
import java.util.*;
import java.util.function.*;

public interface SupplierContract<T> {

	Class<T> supplierType();

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

	default T wrap(T object) {
		return ContractBuilder.build(this, supplierType()).wrap(object);
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

		public Optional<Throwable> throwable() {
			return Optional.ofNullable(throwable);
		}

		public Optional<T> value() {
			if (throwable != null) {
				return Optional.empty();
			}
			return Optional.of(result);
		}

		public Result<T> onValue(Consumer<T> asserter) {
			value().ifPresent(v -> asserter.accept(v));
			return this;
		}

		public Result<T> onThrowableFail() {
			return onThrowable(t -> {throw t;});
		}

		public Result<T> onThrowable(ThrowingConsumer<Throwable> throwableConsumer) {
			if (throwable != null) {
				try {
					throwableConsumer.accept(throwable);
				} catch (Throwable t) {
					throwAsUncheckedException(t);
				}
			}
			return this;
		}

		/**
		 * Throw the supplied {@link Throwable}, <em>masked</em> as an
		 * unchecked exception.
		 *
		 * @param t   the Throwable to be wrapped
		 * @param <T> type of the value to return
		 * @return Fake return to make using the method a bit simpler
		 */
		public static <T> T throwAsUncheckedException(Throwable t) {
			Result.throwAs(t);

			// Will never get here
			return null;
		}

		@SuppressWarnings("unchecked")
		private static <T extends Throwable> void throwAs(Throwable t) throws T {
			throw (T) t;
		}
	}

	@FunctionalInterface
	interface ThrowingConsumer<T> {
		void accept(T value) throws Throwable;
	}
}
