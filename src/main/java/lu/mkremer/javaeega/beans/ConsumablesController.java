package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lu.mkremer.javaeega.consumables.Consumable;
import lu.mkremer.javaeega.consumables.ConsumableType;
import lu.mkremer.javaeega.managers.ConsumableManager;
import lu.mkremer.javaeega.util.MessageHelper;

@ManagedBean(name="listcons")
@ViewScoped
public class ConsumablesController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2033785439528123516L;
	
	@EJB private ConsumableManager cm;
	
	@NotNull
	@Min(value=0, message="The amount cannot be negative")
	private int amount;
	
	@NotNull(message="No consumable type selected")
	private ConsumableType type;
	
	@NotNull
	private long consumableId;

	@NotNull
	@Min(value=1, message="Value to increment or decrement must be 1 or higher")
	private int consumableIncrement = 1;
	
	@ManagedProperty("#{usession}")
	private UserSession session;

	public void preRenderPage(ComponentSystemEvent event) {
		if(session.canListConsumables()) {
			List<Consumable> consumables = cm.getIndependentConsumables();
			int criticals = 0;
			for(Consumable c : consumables) {
				if(c.getAmount() <= c.getType().getCritical()) {
					criticals++;
				}
			}
			if(criticals > 0) {
				MessageHelper.throwWarningMessage(String.format("The stock of %d consumable(s) are low in quantity", criticals));
			}
		}
	}

	public void setSession(UserSession session) {
		this.session = session;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public ConsumableType getType() {
		return type;
	}

	public void setType(ConsumableType type) {
		this.type = type;
	}

	public long getConsumableId() {
		return consumableId;
	}

	public void setConsumableId(long consumableId) {
		this.consumableId = consumableId;
	}

	public int getConsumableIncrement() {
		return consumableIncrement;
	}

	public void setConsumableIncrement(int consumableIncrement) {
		this.consumableIncrement = consumableIncrement;
	}

	public List<ConsumableType> getConsumableTypes(){
		return cm.getConsumableTypes();
	}
	
	public List<Consumable> getIndependentConsumables(){
		return cm.getIndependentConsumables();
	}
	
	public void create() {
		if(session.canAddConsumables()) {
			cm.createConsumableForDevice(type, amount, null);
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
	
	public void cincrement() {
		if(session.canModifyConsumables()) {
			Consumable c = cm.getConsumableById(consumableId);
			if(c != null) {
				if(c.getAmount() + consumableIncrement <= Integer.MAX_VALUE) {
					c.setAmount(c.getAmount() + consumableIncrement);
					cm.update(c);
				}else {
					MessageHelper.throwDangerMessage("The requested increasement for consumable \"" + c.getType().getName() + "\" would exceed it's limit");
				}
			}else {
				MessageHelper.throwDangerMessage("Consumable was not found");
			}
			consumableIncrement = 1;
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
	
	public void cdecrement() {
		if(session.canModifyConsumables()) {
			Consumable c = cm.getConsumableById(consumableId);
			if(c != null) {
				if(c.getAmount() - consumableIncrement >= 0) {
					c.setAmount(c.getAmount() - consumableIncrement);
					cm.update(c);
				}else {
					MessageHelper.throwDangerMessage("The stock for consumable \"" + c.getType().getName() + "\" cannot be negative");
				}
			}else {
				MessageHelper.throwDangerMessage("Consumable was not found");
			}
			consumableIncrement = 1;
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
	
	public void delete() {
		cm.deleteConsumableById(consumableId);
	}

}
