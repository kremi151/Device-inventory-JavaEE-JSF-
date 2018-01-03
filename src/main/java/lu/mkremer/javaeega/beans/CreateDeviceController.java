package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lu.mkremer.javaeega.consumables.ConsumableType;
import lu.mkremer.javaeega.devices.Device;
import lu.mkremer.javaeega.devices.DeviceType;
import lu.mkremer.javaeega.managers.ConsumableManager;
import lu.mkremer.javaeega.managers.DeviceManager;
import lu.mkremer.javaeega.managers.UserManager;
import lu.mkremer.javaeega.validators.ExistingUsername;

@ManagedBean(name="createdevice")
@ViewScoped
public class CreateDeviceController implements Serializable{

	//TODO: Use the user selector from webprog 2 project (PAY ATTENTION: userId <-> username) & 2 parameters for result listener!!!
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8630636366031507240L;

	@NotNull(message="No device name supplied")
	@Size(min=5, max=100, message="Device name must be between {min} and {max} characters long")
	private String name;
	
	@NotNull(message="No username supplied")
	@ExistingUsername
	private String username;
	
	@NotNull(message="No device type supplied")
	private DeviceType type;

	@EJB private DeviceManager dm;
	@EJB private UserManager um;
	@EJB private ConsumableManager cm;
	
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
	
	public List<DeviceType> getTypes(){
		return dm.getDeviceTypes();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
		this.type = type;
	}

	public String create() {
		if(session.canAddDevices()) {
			Device device = dm.createDevice(name, type, um.findUser(username));
			List<ConsumableType> ctypes = cm.getConsumablesForDeviceType(type);
			for(ConsumableType ctype : ctypes) {
				cm.createConsumableForDevice(ctype, 0, device);
			}
			name = null;
			username = null;
			type = null;
			return "alldevices.xhtml";
		}else {
			FacesMessage msg = new FacesMessage("You are not allowed to do this");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "createdevice.xhtml";
		}
	}
}
