package lu.mkremer.javaeega.devices;

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

import lu.mkremer.javaeega.consumables.Consumable;
import lu.mkremer.javaeega.intervention.Report;
import lu.mkremer.javaeega.users.User;

@Entity
public class Device implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -992613100425963248L;
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false)
	private String name;

	@ManyToOne(optional=false)
	private User owner;
	
	@ManyToOne(optional=false)
	private DeviceType type;
	

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="device")
	private List<DevicePropertyValue> properties;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="device")
	private List<Report> reports;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="device")
	private List<Consumable> consumable;
	
	public Device() {}
	
	public Device(String name, DeviceType type, User owner) {
		this.name = name;
		this.type = type;
		this.owner = owner;
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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
		this.type = type;
	}

}
