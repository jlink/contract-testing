package net.jqwik.contract;

import net.jqwik.api.Example;
import org.assertj.core.api.Assertions;

class ContractBuilderTests {

	@Example
	void preconditionViolated() {

		MyInterface impl = new MyInterface() {
			@Override
			public int countLetters(String word) {
				return word.length();
			}
		};

		MyInterface contractedImpl = new MyContract().wrap(impl);

		Assertions.assertThat(contractedImpl.countLetters("1")).isEqualTo(1);
		Assertions.assertThat(contractedImpl.countLetters("123456789")).isEqualTo(9);

		Assertions.assertThatThrownBy(() -> contractedImpl.countLetters(""))
				  .isInstanceOf(PreconditionViolation.class);

	}

	@Example
	void preconditionViolatedWithAsserts() {

		MyInterface impl = new MyInterface() {
			@Override
			public int countLetters(String word) {
				return word.length();
			}
		};

		MyInterface contractedImpl = new MyContractWithAsserts().wrap(impl);

		Assertions.assertThat(contractedImpl.countLetters("1")).isEqualTo(1);
		Assertions.assertThat(contractedImpl.countLetters("123456789")).isEqualTo(9);

		Assertions.assertThatThrownBy(() -> contractedImpl.countLetters(""))
				  .isInstanceOf(PreconditionViolation.class);

	}

	@Example
	void postconditionViolated() {

		MyInterface impl = new MyInterface() {
			@Override
			public int countLetters(String word) {
				return 0;
			}
		};

		MyInterface contractedImpl = new MyContract().wrap(impl);

		Assertions.assertThatThrownBy(() -> contractedImpl.countLetters("abc"))
				  .isInstanceOf(PostconditionViolation.class);

	}

	@Example
	void postconditionViolatedWithAsserts() {

		MyInterface impl = new MyInterface() {
			@Override
			public int countLetters(String word) {
				return 0;
			}
		};

		MyInterface contractedImpl = new MyContractWithAsserts().wrap(impl);

		Assertions.assertThatThrownBy(() -> contractedImpl.countLetters("abc"))
				  .isInstanceOf(PostconditionViolation.class);

	}

	@Example
	void invariantViolated() {

		MyInterface impl = new MyInterface() {
			@Override
			public int countLetters(String word) {
				return word.length();
			}

			@Override
			public boolean anInvariant() {
				return false;
			}
		};

		MyInterface contractedImpl = new MyContract().wrap(impl);

		Assertions.assertThatThrownBy(() -> contractedImpl.countLetters("abc"))
				  .isInstanceOf(InvariantViolation.class);

	}

	@Example
	void invariantViolatedWithAsserts() {

		MyInterface impl = new MyInterface() {
			@Override
			public int countLetters(String word) {
				return word.length();
			}

			@Override
			public boolean anInvariant() {
				return false;
			}
		};

		MyInterface contractedImpl = new MyContractWithAsserts().wrap(impl);

		Assertions.assertThatThrownBy(() -> contractedImpl.countLetters("abc"))
				  .isInstanceOf(InvariantViolation.class);

	}

}

interface MyInterface {
	int countLetters(String word);

	default boolean anInvariant() {
		return true;
	}
}

class MyContract implements SupplierContract<MyInterface> {

	@Override
	public Class<MyInterface> supplierType() {
		return MyInterface.class;
	}

	@Require
	boolean countLetters(String word) {
		return word.length() > 0 && word.length() < 10;
	}

	@Ensure
	boolean countLetters(String word, Result<Integer> result) {
		return result.value().map(value -> value > 0 && value < 10).orElse(false);
	}

	@Invariant
	boolean invariant(MyInterface my) {
		return my.anInvariant();
	}

}

class MyContractWithAsserts implements SupplierContract<MyInterface> {

	@Override
	public Class<MyInterface> supplierType() {
		return MyInterface.class;
	}

	@Require
	void countLetters(String word) {

		Assertions.assertThat(word.length()).isGreaterThan(0);
		Assertions.assertThat(word.length()).isLessThan(10);
	}

	@Ensure
	void countLetters(String word, Result<Integer> result) {
		result.onValue(value -> {
			Assertions.assertThat(value).isGreaterThan(0);
			Assertions.assertThat(value).isLessThan(10);
		}).onThrowableFail();
	}

	@Invariant
	void invariant(MyInterface my) {
		Assertions.assertThat(my.anInvariant()).isTrue();
	}

}

