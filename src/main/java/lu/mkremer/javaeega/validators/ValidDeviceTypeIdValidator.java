package lu.mkremer.javaeega.validators;

import javax.ejb.EJB;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lu.mkremer.javaeega.managers.DeviceManager;

public class ValidDeviceTypeIdValidator implements ConstraintValidator<ValidDeviceTypeId, Object>{
	
	@EJB
	private DeviceManager dm;

	@Override
	public void initialize(ValidDeviceTypeId constraintAnnotation) {}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		System.out.println("Received device id value: " + value);
		long val;
		if(value instanceof Long) {
			val = (Long)value;
		}else {
			try {
				val = Long.parseLong(value.toString());
			}catch(NumberFormatException e) {
				return false;
			}
		}
		return val > 0 && dm.doesDeviceTypeExist(val);
	}

}
