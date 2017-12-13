package lu.mkremer.javaeega.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String>{
	
	private int min, max;
	private String message;

	@Override
	public void initialize(ValidPassword constraintAnnotation) {
		this.min = constraintAnnotation.min();
		this.max = constraintAnnotation.max();
		this.message = constraintAnnotation.message();
	}

	private boolean checkPassword(String pwd) {
		boolean lowerCase = false;
		boolean upperCase = false;
		boolean specialChar = false;
		boolean number = false;
		for(int i = 0 ; i < pwd.length() ; i++) {
			char c = pwd.charAt(i);
			if(c >= 65 && c <= 90) {
				upperCase = true;
			}else if(c >= 97 && c <= 122) {
				lowerCase = true;
			}else if((c >= 33 && c <= 47) || (c >= 58 && c <= 64) || (c >= 91 && c <= 96) || (c >= 123 && c <= 126)) {
				specialChar = true;
			}else if(c >= 48 && c <= 57) {
				number = true;
			}else {
				return false;
			}
		}
		return lowerCase && upperCase && specialChar && number;
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean valid = value != null && value.length() >= min && value.length() <= max && checkPassword(value);
		if(!valid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
		}
		return valid;
	}

}
