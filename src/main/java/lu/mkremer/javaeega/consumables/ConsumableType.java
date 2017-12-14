package lu.mkremer.javaeega.consumables;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lu.mkremer.javaeega.devices.DeviceType;

@Entity
public class ConsumableType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8501494997384293299L;

	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false)//TODO: Cascading
	private String name;
	
	@OneToOne(optional=true)//TODO: Cascading
	private DeviceType deviceType;
	
	public ConsumableType() {}
	
	public ConsumableType(String name, DeviceType deviceType) {
		this.name = name;
		this.deviceType = deviceType;
	}
	
	public ConsumableType(String name) {
		this(name, null);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
}
