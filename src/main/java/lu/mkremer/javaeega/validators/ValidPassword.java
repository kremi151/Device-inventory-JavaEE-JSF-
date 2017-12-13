package lu.mkremer.javaeega.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPasswordValidator.class)
@Documented
public @interface ValidPassword {
	
	String message() default "The password has to be between {min} and {max} characters long, have both upper and lower case characters, at least one special character and at least one number in it";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int min() default 8;
    int max() default 32;
    
}
