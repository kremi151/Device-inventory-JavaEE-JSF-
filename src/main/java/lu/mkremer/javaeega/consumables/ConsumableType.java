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
import javax.validation.constraints.Min;

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
	
	@Column(nullable=false)
	@Min(0)
	private int critical = 5;
	
	@ManyToOne(optional=true)
	private DeviceType deviceType;
	

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="type")
	private List<Consumable> consumables;
	
	public ConsumableType() {}
	
	public ConsumableType(String name, int critical, DeviceType deviceType) {
		this.name = name;
		this.deviceType = deviceType;
		this.critical = critical;
	}
	
	public ConsumableType(String name, int critical) {
		this(name, critical, null);
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

	public int getCritical() {
		return critical;
	}

	public void setCritical(int critical) {
		this.critical = critical;
	}
}
