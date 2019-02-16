package net.johanneslink.eurocalc;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER, ElementType.TYPE_USE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstrainedBy {

	Class<?> value();
}
