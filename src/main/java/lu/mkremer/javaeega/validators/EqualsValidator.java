package lu.mkremer.javaeega.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value="equalsValidator")
public class EqualsValidator implements Validator{

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		Object otherValue = component.getAttributes().get("other");

        if (value == null || otherValue == null) {
            return;
        }

        if (!value.equals(otherValue)) {
        	String rawMessage = (String)component.getAttributes().get("message");
        	if(rawMessage == null)rawMessage = "Values are not equal.";
        	FacesMessage message = new FacesMessage(rawMessage);
        	message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
	}

}
