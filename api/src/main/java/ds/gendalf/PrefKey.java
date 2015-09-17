package ds.gendalf;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target(value = FIELD)
public @interface PrefKey {
	String value() default "";
}