package net.jqwik.contract;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Contract {

	Class<? extends SupplierContract> value();

}
