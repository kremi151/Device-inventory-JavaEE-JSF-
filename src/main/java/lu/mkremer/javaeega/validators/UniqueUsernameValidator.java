package lu.mkremer.javaeega.validators;

import javax.ejb.EJB;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lu.mkremer.javaeega.managers.UserManager;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String>{
	
	@EJB
	private UserManager um;

	@Override
	public void initialize(UniqueUsername constraintAnnotation) {}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return !um.doesUsernameExist(value);
	}

}
