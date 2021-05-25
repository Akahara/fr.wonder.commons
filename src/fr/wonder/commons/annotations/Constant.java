package fr.wonder.commons.annotations;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

/**
 * <p>
 * Indicates that a function must return a single value through the entire
 * lifetime of the program if it is a static method or the object instance if it
 * is not.
 * 
 * <p>
 * This method may be used to indicate that a cache must be created by the
 * implementation of a method.
 * 
 * <p>
 * This annotation may be used for getters and generally methods of interfaces
 * used to retrieve data.
 * 
 * <p>
 * This method is only used for readability purpose, it is not retained at
 * runtime.
 */
@Documented
@Target(METHOD)
public @interface Constant {

}
