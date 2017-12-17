package lu.mkremer.javaeega.managers.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ejb.Singleton;

import lu.mkremer.javaeega.consumables.Consumable;
import lu.mkremer.javaeega.managers.MessageManager;
import lu.mkremer.javaeega.users.User;

@Singleton
public class MessageManagerImpl implements MessageManager{
	
	private final Set<ConsumableStockMessage> consumableMessages = Collections.synchronizedSet(new HashSet<>());

	@Override
	public void notifyConsumableStock(Consumable consumable) {
		synchronized(consumableMessages) {
			ConsumableStockMessage csm = new ConsumableStockMessage(consumable.getId());
			consumableMessages.remove(csm);
			if(consumable.getAmount() <= consumable.getType().getCritical()) {
				csm.stock = consumable.getAmount();
				csm.name = consumable.getType().getName();
				if(consumable.getDevice() != null) {
					csm.device = consumable.getDevice().getName();
				}
				consumableMessages.add(csm);
			}
		}
	}

	@Override
	public void untrackConsumable(Consumable consumable) {
		consumableMessages.remove(new ConsumableStockMessage(consumable.getId()));
	}

	@Override
	public List<String> getWarningMessages() {
		LinkedList<String> res = new LinkedList<>();
		synchronized(consumableMessages) {
			for(ConsumableStockMessage csm : consumableMessages) {
				if(csm.device != null) {
					res.add(String.format("Consumable stock of <strong>%s</strong> for device <strong>%s</strong> is at a critical level of <strong>%d</strong>", csm.name, csm.device, csm.stock));
				}else {
					res.add(String.format("General consumable stock of <strong>%s</strong> is at a critical level of <strong>%d</strong>", csm.name, csm.stock));
				}
			}
		}
		return res;
	}

	@Override
	public int notificationCount(User user) {
		return consumableMessages.size();//TODO: Adjust notification count maybe in future
	}
	
	private static class ConsumableStockMessage{
		private final long consId;
		private int stock;
		private String name, device;
		
		private ConsumableStockMessage(long id) {
			this.consId = id;
		}
		
		@Override
		public boolean equals(Object other) {
			if(other == this)
				return true;
			else if(other == null || other.getClass() != ConsumableStockMessage.class)
				return false;
			else
				return ((ConsumableStockMessage)other).consId == this.consId;
		}
	
		@Override
		public int hashCode() {
			return (int) consId;
		}
	}

}
