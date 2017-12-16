package lu.mkremer.javaeega.managers;

import java.util.List;

import javax.ejb.Local;

import lu.mkremer.javaeega.devices.Device;
import lu.mkremer.javaeega.devices.DeviceProperty;
import lu.mkremer.javaeega.devices.DevicePropertyType;
import lu.mkremer.javaeega.devices.DevicePropertyValue;
import lu.mkremer.javaeega.devices.DeviceType;
import lu.mkremer.javaeega.intervention.Intervention;
import lu.mkremer.javaeega.intervention.Report;
import lu.mkremer.javaeega.intervention.ReportStatus;
import lu.mkremer.javaeega.users.User;

@Local
public interface DeviceManager {

	DeviceType createDeviceType(String name);
	DeviceType createDeviceType(String name, DeviceType parent);
	DeviceType getDeviceTypeById(long id);
	Device createDevice(String name, DeviceType type, User owner);
	Device getDeviceById(long id);
	DeviceProperty createDeviceProperty(String name, DevicePropertyType type, DeviceType deviceType);
	DeviceProperty getPropertyById(long id);
	DevicePropertyValue addOrModifyDeviceProperty(Device device, DeviceProperty property, String value);
	void updateObject(DeviceType type);
	void updateObject(Device device);
	void updateObject(DeviceProperty property);
	void updateObject(DevicePropertyValue value);
	List<DevicePropertyValue> getPropertiesForDevice(Device device);
	List<DeviceProperty> getPropertiesForDeviceType(DeviceType type);
	List<DeviceType> getDeviceTypes();
	List<Device> getDevicesOfUser(User user);
	List<Device> getAllDevices();
	boolean removePropertyById(long id);
	boolean removeDeviceTypeById(long id);
	boolean doesDeviceTypeExist(long id);
	boolean removeDevice(Device device);
	List<Report> getReportsForDevice(Device device);
	List<Intervention> getInterventionsForReport(Report report);
	Report createReportOnDevice(Device device, User user, String title, String message);
	Report findReportById(long id);
	Intervention interveneOnReport(Report report, User responsible, String message, ReportStatus newStatus);
	
}
