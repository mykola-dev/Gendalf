package ds.gendalf;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Target(value = TYPE)
public @interface PrefsConfig {
    String value() default ("");

    String filename() default ("gendalf_prefs");

    String cls() default ("Gendalf");
}