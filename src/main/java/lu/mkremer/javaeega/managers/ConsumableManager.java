package lu.mkremer.javaeega.managers;

import java.util.List;

import javax.ejb.Local;

import lu.mkremer.javaeega.consumables.Consumable;
import lu.mkremer.javaeega.consumables.ConsumableType;
import lu.mkremer.javaeega.devices.Device;
import lu.mkremer.javaeega.devices.DeviceType;

@Local
public interface ConsumableManager {

	List<ConsumableType> getConsumableTypes();
	List<ConsumableType> getConsumablesForDeviceType(DeviceType deviceType);
	List<ConsumableType> getIndependentConsumableTypes();
	List<Consumable> getConsumablesForDevice(Device device);
	List<Consumable> getIndependentConsumables();
	List<Consumable> getCriticalConsumables();
	ConsumableType createConsumableType(String name, int critical, DeviceType deviceType);
	ConsumableType createConsumableType(String name, int critical);
	Consumable createConsumableForDevice(ConsumableType type, int amount, Device device);
	Consumable getConsumableById(long id);
	ConsumableType getConsumableTypeById(long id);
	void update(Consumable consumable);
	void update(ConsumableType type);
	void deleteConsumableById(long id);
	void deleteConsumableTypeById(long id);
	
}
