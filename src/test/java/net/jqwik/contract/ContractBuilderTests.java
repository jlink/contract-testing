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

		MyInterface contractedImpl = new MyContract().wrap(impl, MyInterface.class);

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

		MyInterface contractedImpl = new MyContract().wrap(impl, MyInterface.class);

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

		MyInterface contractedImpl = new MyContract().wrap(impl, MyInterface.class);

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

class MyContract implements Contract<MyInterface> {

	@Require
	boolean countLetters(String word) {
		return word.length() > 0 && word.length() < 10;
	}

	@Ensure
	boolean countLetters(String word, int result) {
		return result > 0 && result < 10;
	}

	@Invariant
	boolean invariant(MyInterface my) {
		return my.anInvariant();
	}

}

