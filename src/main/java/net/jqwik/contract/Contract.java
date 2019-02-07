package net.jqwik.contract;

import java.lang.annotation.*;

public interface Contract<T> {

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

}
