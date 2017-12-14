package lu.mkremer.javaeega.managers.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lu.mkremer.javaeega.devices.Device;
import lu.mkremer.javaeega.devices.DeviceProperty;
import lu.mkremer.javaeega.devices.DevicePropertyType;
import lu.mkremer.javaeega.devices.DevicePropertyValue;
import lu.mkremer.javaeega.devices.DevicePropertyValueKey;
import lu.mkremer.javaeega.devices.DeviceType;
import lu.mkremer.javaeega.intervention.Intervention;
import lu.mkremer.javaeega.intervention.Report;
import lu.mkremer.javaeega.intervention.ReportStatus;
import lu.mkremer.javaeega.managers.DeviceManager;
import lu.mkremer.javaeega.users.User;

//@Stateless
@Singleton
public class DeviceManagerImpl implements DeviceManager{

	@PersistenceContext
	private EntityManager em;
	
	private final HashMap<Long, List<DeviceProperty>> propertyCache = new HashMap<>();
	
	private void invalidatePropertyCache() {
		synchronized(propertyCache) {
			propertyCache.clear();
		}
	}

	@Override
	public List<DevicePropertyValue> getPropertiesForDevice(Device device) {
		return em.createQuery("select v from DevicePropertyValue v where device_id = :id", DevicePropertyValue.class)
				.setParameter("id", device.getId())
				.getResultList();
	}

	@Override
	public List<DeviceProperty> getPropertiesForDeviceType(DeviceType type) {
		List<DeviceProperty> cached;
		synchronized(propertyCache) {
			cached = propertyCache.get(type.getId());
		}
		if(cached != null) {
			return cached;
		}else {
			HashSet<DeviceProperty> props = new HashSet<>();
			collectPropertiesWithInheritance(type, props, new HashSet<>());
			cached = new ArrayList<>(props);
			synchronized(propertyCache) {
				propertyCache.put(type.getId(), cached);
			}
			return cached;
		}
	}
	
	private void collectPropertiesWithInheritance(DeviceType type, Set<DeviceProperty> props, Set<DeviceType> visited) {
		if(!visited.contains(type)) {
			visited.add(type);
			props.addAll(em.createQuery("select p from DeviceProperty p where deviceType_id = :id", DeviceProperty.class)
					.setParameter("id", type.getId())
					.getResultList());
			if(type.getParent() != null) {
				collectPropertiesWithInheritance(type.getParent(), props, visited);
			}
		}
	}

	@Override
	public DeviceType createDeviceType(String name) {
		DeviceType type = new DeviceType(name);
		em.persist(type);
		return type;
	}

	@Override
	public DeviceType createDeviceType(String name, DeviceType parent) {
		DeviceType type = new DeviceType(name);
		type.setParent(parent);
		em.persist(type);
		return type;
	}

	@Override
	public Device createDevice(String name, DeviceType type, User owner) {
		Device device = new Device(name, type, owner);
		em.persist(device);
		return device;
	}

	@Override
	public DeviceProperty createDeviceProperty(String name, DevicePropertyType type, DeviceType deviceType) {
		DeviceProperty property = new DeviceProperty(name, type, deviceType);
		em.persist(property);
		invalidatePropertyCache();
		return property;
	}

	@Override
	public DevicePropertyValue addOrModifyDeviceProperty(Device device, DeviceProperty property, String value) {
		DevicePropertyValueKey key = new DevicePropertyValueKey(device, property);
		DevicePropertyValue pvalue = em.find(DevicePropertyValue.class, key);
		if(pvalue != null) {
			pvalue.setValue(value);
			em.merge(pvalue);
		}else {
			pvalue = new DevicePropertyValue(device, property, value);
			em.persist(pvalue);
		}
		return pvalue;
	}

	@Override
	public void updateObject(DeviceType type) {
		em.merge(type);
		invalidatePropertyCache();
	}

	@Override
	public void updateObject(Device device) {
		em.merge(device);
	}

	@Override
	public void updateObject(DeviceProperty property) {
		em.merge(property);
		invalidatePropertyCache();
	}

	@Override
	public void updateObject(DevicePropertyValue value) {
		em.merge(value);
	}

	@Override
	public List<DeviceType> getDeviceTypes() {
		return em.createQuery("select t from DeviceType t", DeviceType.class).getResultList();
	}

	@Override
	public DeviceType getDeviceTypeById(long id){
		return em.find(DeviceType.class, id);
	}

	@Override
	public boolean removePropertyById(long id) {
		DeviceProperty prop = em.find(DeviceProperty.class, id);
		if(prop != null) {
			em.remove(prop);
			invalidatePropertyCache();
			return true;
		}else {
			return false;
		}
	}

	@Override
	public List<Device> getDevicesOfUser(User user) {
		return em.createQuery("select d from Device d where owner_user_id = :user", Device.class).setParameter("user", user.getUserId()).getResultList();
	}

	@Override
	public List<Device> getAllDevices() {
		return em.createQuery("select d from Device d", Device.class).getResultList();
	}

	@Override
	public boolean doesDeviceTypeExist(long id) {
		return em.find(DeviceType.class, id) != null;
	}

	@Override
	public Device getDeviceById(long id) {
		return em.find(Device.class, id);
	}

	@Override
	public DeviceProperty getPropertyById(long id) {
		return em.find(DeviceProperty.class, id);
	}

	@Override
	public boolean removeDeviceTypeById(long id) {
		DeviceType type = em.find(DeviceType.class, id);
		if(type != null) {
			em.remove(type);
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean removeDevice(Device device) {
		Device attachedDevice = em.find(Device.class, device.getId());
		em.remove(attachedDevice);
		return true;
	}

	@Override
	public List<Report> getReportsForDevice(Device device) {
		return em.createQuery("select r from Report r where device_id = :id", Report.class).setParameter("id", device.getId()).getResultList();
	}

	@Override
	public Report createReportOnDevice(Device device, User user, String title, String message) {
		Report report = new Report(device, user, title, message);
		em.persist(report);
		return report;
	}

	@Override
	public Intervention interventOnReport(Report report, User responsible, String message, ReportStatus newStatus) {
		report.setStatus(newStatus);
		em.merge(report);
		Intervention intv = new Intervention(report, responsible, message);
		em.persist(intv);
		return intv;
	}

	@Override
	public Report findReportById(long id) {
		return em.find(Report.class, id);
	}

	@Override
	public List<Intervention> getInterventionsForReport(Report report) {
		return em.createQuery("select i from Intervention i where report_id = :id", Intervention.class).setParameter("id", report.getId()).getResultList();
	}

}
