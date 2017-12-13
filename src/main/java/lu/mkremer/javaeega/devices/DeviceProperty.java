package lu.mkremer.javaeega.devices;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class DeviceProperty implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4479056143450809066L;

	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false)
	private String name;
	
	//@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private DevicePropertyType type;

	@ManyToOne(optional=false)
	@OnDelete(action=OnDeleteAction.CASCADE)//TODO: Cascading
	private DeviceType deviceType;
	
	public DeviceProperty() {}
	
	public DeviceProperty(String name, DevicePropertyType type, DeviceType deviceType) {
		this.name = name;
		this.type = type;
		this.deviceType = deviceType;
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public DevicePropertyType getType() {
		return type;
	}

	public void setType(DevicePropertyType type) {
		this.type = type;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}else if(obj == null || obj.getClass() != DeviceProperty.class) {
			return false;
		}else {
			return ((DeviceProperty)obj).id == this.id;
		}
	}
	
	@Override
	public int hashCode() {
		return (int)id;
	}
}
