package lu.mkremer.javaeega.consumables;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
	
	@Column(nullable=false)
	private String name;
	
	@ManyToOne(optional=true)
	private DeviceType deviceType;
	

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="type")
	private List<Consumable> consumables;
	
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
