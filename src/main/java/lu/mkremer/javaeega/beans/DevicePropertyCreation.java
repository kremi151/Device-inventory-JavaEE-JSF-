package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lu.mkremer.javaeega.devices.DevicePropertyType;
import lu.mkremer.javaeega.devices.DeviceType;
import lu.mkremer.javaeega.managers.DeviceManager;
import lu.mkremer.javaeega.util.MessageHelper;

@ViewScoped
@ManagedBean(name="nprop")
public class DevicePropertyCreation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5826688074579095852L;

	@NotNull(message="No property name specified")
	@Size(min=5, max=32, message="Property name must be between {min} and {max} characters long")
	private String name;
	
	@NotNull(message="No property type defined")
	private DevicePropertyType type;
	
	private String tags;
	
	@EJB private DeviceManager dm;
	
	@ManagedProperty("#{usession}")
	private UserSession session;

	
	public void setSession(UserSession session) {
		this.session = session;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public DevicePropertyType getType() {
		return type;
	}


	public void setType(DevicePropertyType type) {
		this.type = type;
	}


	public String getTags() {
		return tags;
	}


	public void setTags(String tags) {
		this.tags = tags;
	}


	public void createProperty() {
		if(session.canModifyDeviceType()) {
			try {
				long deviceTypeId = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
				DeviceType devType = dm.getDeviceTypeById(deviceTypeId);
				dm.createDeviceProperty(name, type, devType, tags);
				name = null;
				type = null;
				tags = null;
			}catch(NumberFormatException | NullPointerException e) {
				MessageHelper.throwWarningMessage("An unexpected error occured");
			}
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}


	public List<DevicePropertyType> getPropertyTypes(){
		return Arrays.asList(DevicePropertyType.values());
	}
	
}
