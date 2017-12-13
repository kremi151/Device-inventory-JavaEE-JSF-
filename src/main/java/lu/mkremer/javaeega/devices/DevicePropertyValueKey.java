package lu.mkremer.javaeega.devices;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Embeddable
public class DevicePropertyValueKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7755748318630034825L;

	@ManyToOne(optional=false)
	@OnDelete(action=OnDeleteAction.CASCADE)//TODO: Cascading
	private Device device;

	@ManyToOne(optional=false)
	@OnDelete(action=OnDeleteAction.CASCADE)//TODO: Cascading
	private DeviceProperty property;
	
	public DevicePropertyValueKey() {}
	
	public DevicePropertyValueKey(Device device, DeviceProperty property) {
		this.device = device;
		this.property = property;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public DeviceProperty getProperty() {
		return property;
	}

	public void setProperty(DeviceProperty property) {
		this.property = property;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}else if(obj == null || obj.getClass() != DevicePropertyValueKey.class) {
			return false;
		}else {
			return ((DevicePropertyValueKey)obj).device.getId() == device.getId() && ((DevicePropertyValueKey)obj).property.getId() == property.getId();
		}
	}
	
	@Override
	public int hashCode() {
		return (int)(device.getId() ^ property.getId());
	}
	
}
