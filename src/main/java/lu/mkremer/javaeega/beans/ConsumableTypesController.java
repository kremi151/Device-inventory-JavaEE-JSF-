package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lu.mkremer.javaeega.consumables.ConsumableType;
import lu.mkremer.javaeega.devices.DeviceType;
import lu.mkremer.javaeega.managers.ConsumableManager;
import lu.mkremer.javaeega.managers.DeviceManager;
import lu.mkremer.javaeega.util.MessageHelper;

@ManagedBean(name="contypes")
@ViewScoped
public class ConsumableTypesController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3000397119766641415L;

	@NotNull
	@Size(min=3, max=128)
	private String name;
	
	private long parentId;
	
	@EJB private DeviceManager dm;
	@EJB private ConsumableManager cm;
	
	public List<DeviceType> getDeviceTypes(){
		return dm.getDeviceTypes();
	}
	
	public List<ConsumableType> getConsumableTypes(){
		return cm.getConsumableTypes();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	
	public void create() {
		if(UserSession.getCurrentSession().canAddDeviceTypes()) {
			cm.createConsumableType(name, (parentId > 0) ? dm.getDeviceTypeById(parentId) : null);
			parentId = 0;
			name = null;
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
}
