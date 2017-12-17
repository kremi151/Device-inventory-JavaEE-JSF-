package lu.mkremer.javaeega.managers.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lu.mkremer.javaeega.consumables.Consumable;
import lu.mkremer.javaeega.consumables.ConsumableType;
import lu.mkremer.javaeega.devices.Device;
import lu.mkremer.javaeega.devices.DeviceType;
import lu.mkremer.javaeega.managers.ConsumableManager;

@Singleton
public class ConsumableManagerImpl implements ConsumableManager{
	
	@PersistenceContext
	private EntityManager em;
	
	private final HashMap<Long, List<ConsumableType>> consumableCache = new HashMap<>();
	
	private void invalidateCache() {
		synchronized(consumableCache) {
			consumableCache.clear();
		}
	}

	@Override
	public List<ConsumableType> getConsumableTypes() {
		return em.createQuery("select t from ConsumableType t", ConsumableType.class).getResultList();
	}

	@Override
	public ConsumableType createConsumableType(String name, DeviceType deviceType) {
		ConsumableType type = new ConsumableType(name, deviceType);
		em.persist(type);
		invalidateCache();
		if(deviceType != null)addDefaultConsumable(deviceType, type);
		return type;
	}
	
	private void addDefaultConsumable(DeviceType devType, ConsumableType type) {
		List<Device> devices = em.createQuery("select d from Device d where type_id = :id", Device.class).setParameter("id", devType.getId()).getResultList();
		for(Device device : devices) {
			createConsumableForDevice(type, 0, device);
		}
		List<DeviceType> children = em.createQuery("select t from DeviceType t where parent_id = :id", DeviceType.class).setParameter("id", devType.getId()).getResultList();
		for(DeviceType t : children) {
			addDefaultConsumable(t, type);
		}
	}

	@Override
	public ConsumableType createConsumableType(String name) {
		ConsumableType type = new ConsumableType(name);
		em.persist(type);
		invalidateCache();
		return type;
	}

	@Override
	public List<ConsumableType> getConsumablesForDeviceType(DeviceType deviceType) {
		List<ConsumableType> res;
		synchronized(consumableCache) {
			res = consumableCache.get(deviceType.getId());
		}
		if(res == null) {
			res = new ArrayList<>();
			DeviceType devType = deviceType;
			do {
				res.addAll(em.createQuery("select c from ConsumableType c where deviceType_id = :id", ConsumableType.class).setParameter("id", devType.getId()).getResultList());
			}while((devType = devType.getParent()) != null);
			synchronized(consumableCache) {
				consumableCache.put(deviceType.getId(), res);
			}
		}
		return Collections.unmodifiableList(res);
	}

	@Override
	public Consumable createConsumableForDevice(ConsumableType type, int amount, Device device) {
		Consumable consumable = new Consumable(type, amount, device);
		em.persist(consumable);
		return consumable;
	}

	@Override
	public List<Consumable> getConsumablesForDevice(Device device) {
		return em.createQuery("select c from Consumable c where device_id = :id", Consumable.class).setParameter("id", device.getId()).getResultList();
	}

	@Override
	public Consumable getConsumableById(long id) {
		return em.find(Consumable.class, id);
	}

	@Override
	public void update(Consumable consumable) {
		em.merge(consumable);
	}

	@Override
	public List<Consumable> getIndependentConsumables() {
		return em.createQuery("select c from Consumable c where c.device = null", Consumable.class).getResultList();
	}

	@Override
	public List<ConsumableType> getIndependentConsumableTypes() {
		return em.createQuery("select t from ConsumableType t where t.deviceType = null", ConsumableType.class).getResultList();
	}

	@Override
	public void deleteConsumableById(long id) {
		Consumable c = em.find(Consumable.class, id);
		if(c != null) {
			em.remove(c);
		}
	}

}
