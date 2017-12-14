package lu.mkremer.javaeega.consumables;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lu.mkremer.javaeega.devices.Device;

@Entity
public class Consumable implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1770157790522826017L;

	@Id
	@GeneratedValue
	private long id;
	
	//@Min(value=0, message="The amount must cannot be negative")
	private int amount;
	
	@OneToOne(optional=true)//TODO: Cascading
	private Device device;
	
	@OneToOne(optional=false)//TODO: Cascading
	private ConsumableType type;
	
	public Consumable() {}

	public Consumable(ConsumableType type, int amount) {
		this(type, amount, null);
	}
	
	public Consumable(ConsumableType type, int amount, Device device) {
		this.type = type;
		this.amount = amount;
		this.device = device;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public ConsumableType getType() {
		return type;
	}

	public void setType(ConsumableType type) {
		this.type = type;
	}
}
