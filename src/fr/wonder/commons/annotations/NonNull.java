package fr.wonder.commons.annotations;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

/**
 * <p>
 * Indicates that a value may not be {@code null}. Methods with this annotation
 * must never return {@code null}.
 * 
 * <p>
 * This method is only used for readability purpose, it is not retained at
 * runtime and no check is ever made to assert that the value is not null.
 */
@Target({ FIELD, METHOD, PARAMETER })
@Documented
public @interface NonNull {
}
