package lu.mkremer.javaeega.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String>{

	@Override
	public void initialize(ValidUsername constraintAnnotation) {}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value.matches("^[a-zA-Z0-9\\-\\_]+$");
	}

}
