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

@Entity
public class DeviceType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4967793190496557889L;

	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false)
	private String name;
	
	@ManyToOne(optional=true)
	private DeviceType parent;

	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="type")
	private List<Device> devices;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="parent")
	private List<DeviceType> children;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="deviceType")
	private List<DeviceProperty> properties;
	
	public DeviceType() {}
	
	public DeviceType(String name, DeviceType parent) {
		this.name = name;
		this.parent = parent;
	}
	
	public DeviceType(String name) {
		this(name, null);
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

	public DeviceType getParent() {
		return parent;
	}

	public void setParent(DeviceType parent) {
		this.parent = parent;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}else if(obj == null || obj.getClass() != DeviceType.class) {
			return false;
		}else {
			return ((DeviceType)obj).id == this.id;
		}
	}
	
	@Override
	public int hashCode() {
		return (int)id;
	}
	
}
