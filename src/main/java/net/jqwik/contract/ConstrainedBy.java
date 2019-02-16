package net.jqwik.contract;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER, ElementType.TYPE_USE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstrainedBy {

	Class<? extends Constraint> value();
}
