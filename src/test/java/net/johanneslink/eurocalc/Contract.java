package net.johanneslink.eurocalc;

import java.lang.annotation.*;

import net.jqwik.contract.SupplierContract;

@Target({ ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Contract {

	Class<? extends SupplierContract> value();

}
