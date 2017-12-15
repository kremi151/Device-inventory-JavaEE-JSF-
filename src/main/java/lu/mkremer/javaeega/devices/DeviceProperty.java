package lu.mkremer.javaeega.devices;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
	
	@Enumerated(EnumType.STRING)
	private DevicePropertyType type;

	@ManyToOne(optional=false)
	private DeviceType deviceType;
	

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="property")
	private List<DevicePropertyValue> values;
	
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
