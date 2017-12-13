package lu.mkremer.javaeega.devices;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

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
		return key.getDevice();
	}

	public void setDevice(Device device) {
		key.setDevice(device);
	}

	public DeviceProperty getProperty() {
		return key.getProperty();
	}

	public void setProperty(DeviceProperty property) {
		key.setProperty(property);
	}
}
