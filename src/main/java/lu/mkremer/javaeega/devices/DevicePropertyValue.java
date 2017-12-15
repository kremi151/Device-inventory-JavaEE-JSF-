package lu.mkremer.javaeega.devices;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DevicePropertyValue implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1681552629938448394L;

	@EmbeddedId
	private DevicePropertyValueKey key;
	
	@Column(nullable=false)
	private String value;
	
	@ManyToOne
	@JoinColumn(name="device_id", insertable=false, updatable=false)
	private Device device;
	
	@ManyToOne
	@JoinColumn(name="property_id", insertable=false, updatable=false)
	private DeviceProperty property;
	
	public DevicePropertyValue() {}
	
	public DevicePropertyValue(Device device, DeviceProperty property, String value) {
		this.key = new DevicePropertyValueKey(device, property);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
		this.key.setDeviceId(device.getId());
	}

	public DeviceProperty getProperty() {
		return property;
	}

	public void setProperty(DeviceProperty property) {
		this.property = property;
		this.key.setPropertyId(property.getId());
	}
}
