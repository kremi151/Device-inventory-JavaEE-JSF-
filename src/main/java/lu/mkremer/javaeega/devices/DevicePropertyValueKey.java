package lu.mkremer.javaeega.devices;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DevicePropertyValueKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7755748318630034825L;

	@Column(name="device_id")
	private long deviceId;

	@Column(name="property_id")
	private long propertyId;
	
	public DevicePropertyValueKey() {}
	
	public DevicePropertyValueKey(long device_id, long property_id) {
		this.deviceId = device_id;
		this.propertyId = property_id;
	}
	
	public DevicePropertyValueKey(Device device, DeviceProperty property) {
		this(device.getId(), property.getId());
	}
	
	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}

	public long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(long propertyId) {
		this.propertyId = propertyId;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}else if(obj == null || obj.getClass() != DevicePropertyValueKey.class) {
			return false;
		}else {
			return ((DevicePropertyValueKey)obj).deviceId == deviceId && ((DevicePropertyValueKey)obj).propertyId == propertyId;
		}
	}
	
	@Override
	public int hashCode() {
		return (int)(deviceId ^ propertyId);
	}
	
}
