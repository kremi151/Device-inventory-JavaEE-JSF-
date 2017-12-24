package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.Min;
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
	
	@ManagedProperty("#{usession}")
	private UserSession session;
	
	public void setSession(UserSession session) {
		this.session = session;
	}

	@NotNull(message="No consumable type name supplied")
	@Size(min=3, max=128, message="Consumable type name must be between {min} and {max} characters long")
	private String name;
	
	private long parentId;
	
	@Min(value=0, message="The critical limit cannot be negative")
	private int criticalLimit = 5;
	
	@NotNull
	private long typeId;
	
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
	
	public int getCriticalLimit() {
		return criticalLimit;
	}

	public void setCriticalLimit(int criticalLimit) {
		this.criticalLimit = criticalLimit;
	}

	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public void create() {
		if(session.canAddDeviceTypes()) {
			cm.createConsumableType(name, criticalLimit, (parentId > 0) ? dm.getDeviceTypeById(parentId) : null);
			parentId = 0;
			criticalLimit = 5;
			name = null;
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
	
	public void modify() {
		if(session.canModifyConsumableTypes()) {
			ConsumableType type = cm.getConsumableTypeById(typeId);
			if(type != null) {
				type.setCritical(criticalLimit);
				cm.update(type);
				criticalLimit = 5;
			}else{
				MessageHelper.throwDangerMessage("Consumable type was not found");
			}
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
	
	public void delete() {
		if(session.canRemoveConsumableTypes()) {
			ConsumableType type = cm.getConsumableTypeById(typeId);
			if(type != null) {
				cm.deleteConsumableTypeById(typeId);
			}else{
				MessageHelper.throwDangerMessage("Consumable type was not found");
			}
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
}
