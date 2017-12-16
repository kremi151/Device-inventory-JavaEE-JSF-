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
	List<Consumable> getConsumablesForDevice(Device device);
	ConsumableType createConsumableType(String name, DeviceType deviceType);
	ConsumableType createConsumableType(String name);
	Consumable createConsumableForDevice(ConsumableType type, int amount, Device device);
	
}
