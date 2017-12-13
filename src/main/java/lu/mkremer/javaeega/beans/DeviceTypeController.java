package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
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

	@NotNull
	@Size(min=5, max=128)
	private String name;
	
	private long parentId;
	
	@EJB
	private DeviceManager dm;

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
		if(UserSession.getCurrentSession().canAddDeviceTypes()) {
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
	
}
