package net.jqwik.contract;

import java.lang.reflect.*;

class ContractMethodNotPublic extends RuntimeException {
	ContractMethodNotPublic(Method precondition) {
		super(String.format("Contract method [%s] must be public", precondition));
	}
}
