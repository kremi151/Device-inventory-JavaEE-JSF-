package lu.mkremer.javaeega.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.mindrot.jbcrypt.BCrypt;

import lu.mkremer.javaeega.managers.UserManager;
import lu.mkremer.javaeega.util.MessageHelper;
import lu.mkremer.javaeega.validators.UniqueUsername;
import lu.mkremer.javaeega.validators.ValidPassword;

@ViewScoped
@ManagedBean(name="ucreation")
public class UserCreation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2151015178431675787L;

	@NotNull
	@Size(min=2, max=32) 
	private String firstName;

	@NotNull
	@Size(min=2, max=32) 
	private String lastName;

	@NotNull
	@Size(min=8, max=20)
	@UniqueUsername
	private String username;

	@NotNull
	@ValidPassword
	private String password;

	@NotNull
	private String vpassword;
	
	private String message;
	
	@EJB
	private UserManager um;

	public String getMessage() {
		return message;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getVpassword() {
		return vpassword;
	}

	public void setVpassword(String vpassword) {
		this.vpassword = vpassword;
	}
	
	public void validatePasswords(FacesContext context, UIComponent toValidate, Object value) {
		UIInput passwordField = (UIInput) context.getViewRoot().findComponent("registerForm:password1");
	    if (passwordField == null)
	        throw new IllegalArgumentException(String.format("Unable to find component."));
	    String password = (String) passwordField.getValue();
	    String confirmPassword = (String) value;
		System.out.println("Compare " + confirmPassword + " with " + password);
	    if (!confirmPassword.equals(password)) {
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match!", "Passwords do not match!");
	        throw new ValidatorException(message);
	    }
	}

	public String register() {
		message = null;
		String hashedPwd = BCrypt.hashpw(password, BCrypt.gensalt());
		um.createUser(username, firstName, lastName, hashedPwd, um.getDefaultGroup());
		MessageHelper.throwInfoMessage("Account has been created. You can now log in.");
		return "login";
	}

}
