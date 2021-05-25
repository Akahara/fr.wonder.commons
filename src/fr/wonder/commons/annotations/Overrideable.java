package fr.wonder.commons.annotations;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

/**
 * <p>
 * Indicates that a method may be overridden by a child class.
 * 
 * <p>
 * This annotation may be used on methods with default implementation or default
 * methods such as {@link Object#toString()}.
 * 
 * <p>
 * This method is only used for readability purpose, it is not retained at
 * runtime.
 */
@Target({ METHOD })
@Documented
public @interface Overrideable {

}
