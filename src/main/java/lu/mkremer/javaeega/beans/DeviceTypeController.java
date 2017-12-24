package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lu.mkremer.javaeega.devices.DeviceProperty;
import lu.mkremer.javaeega.devices.DeviceType;
import lu.mkremer.javaeega.managers.DeviceManager;
import lu.mkremer.javaeega.util.MessageHelper;

@ViewScoped
@ManagedBean(name="devtype")
public class DeviceTypeController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4206845110470003590L;

	@NotNull(message="No device type name supplied")
	@Size(min=5, max=128, message="Device type name must be metween {min} and {max} characters long")
	private String name;
	
	private long parentId;
	
	@EJB
	private DeviceManager dm;
	
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
	
	public List<DeviceType> getDeviceTypes(){
		return dm.getDeviceTypes();
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	
	public void create() {
		if(session.canAddDeviceTypes()) {
			if(parentId > 0) {
				dm.createDeviceType(name, dm.getDeviceTypeById(parentId));
			}else {
				dm.createDeviceType(name);
			}
			parentId = 0;
			name = null;
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
	
	public List<DeviceProperty> fetchProperties(DeviceType type){
		return dm.getPropertiesForDeviceType(type);
	}
	
	public DeviceType fetchDeviceType(long id) {
		return dm.getDeviceTypeById(id);
	}
	
	//TODO: Optimize the UI (perhaps using b:dataTable...
	
}
