package lu.mkremer.javaeega.managers;

import java.util.List;

import javax.ejb.Local;

import lu.mkremer.javaeega.devices.DevicePropertyValue;

@Local
public interface SearchManager {

	List<DevicePropertyValue> searchForProperties(String searchQuery);
	
}
