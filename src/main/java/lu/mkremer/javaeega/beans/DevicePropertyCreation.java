package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
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

	@NotNull
	@Size(min=5, max=32)
	private String name;
	
	@NotNull
	private DevicePropertyType type;
	
	@EJB
	private DeviceManager dm;

	
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


	public void createProperty() {
		if(UserSession.getCurrentSession().canModifyDeviceType()) {
			long deviceTypeId = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
			DeviceType devType = dm.getDeviceTypeById(deviceTypeId);
			dm.createDeviceProperty(name, type, devType);
			name = null;
			type = null;
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}


	public List<DevicePropertyType> getPropertyTypes(){
		return Arrays.asList(DevicePropertyType.values());
	}
	
}
