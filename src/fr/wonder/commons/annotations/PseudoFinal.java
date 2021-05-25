package fr.wonder.commons.annotations;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

/**
 * <p>
 * Indicates that a value will only be affected once even though it is not
 * marked as {@code final}. It should be affected in an initializer method or
 * like.
 * 
 * <p>
 * This annotation may be used when a constructor with no arguments is required
 * and it must be clear that its fields must not be modified.
 * 
 * <p>
 * This method is only used for readability purpose, it is not retained at
 * runtime.
 */
@Target({ FIELD, METHOD, PARAMETER })
@Documented
public @interface PseudoFinal {

}
