package lu.mkremer.javaeega.beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.mindrot.jbcrypt.BCrypt;

import lu.mkremer.javaeega.devices.Device;
import lu.mkremer.javaeega.devices.DeviceProperty;
import lu.mkremer.javaeega.devices.DevicePropertyType;
import lu.mkremer.javaeega.devices.DeviceType;
import lu.mkremer.javaeega.managers.DeviceManager;
import lu.mkremer.javaeega.managers.UserManager;
import lu.mkremer.javaeega.users.User;
import lu.mkremer.javaeega.users.UserGroup;

@Singleton
@Startup
public class StartupBean {//TODO: Run it with some default values before submission
	
	private final static boolean INIT_DEFAULT_VALUES = true;
	
	@EJB
	private UserManager um;
	
	@EJB
	private DeviceManager dm;

	@SuppressWarnings("unused")
	@PostConstruct
	public void init() {
		if(INIT_DEFAULT_VALUES) {
			System.out.println("###############################");
			System.out.println("# Initializing default values #");
			System.out.println("###############################");
			
			UserGroup group = um.getDefaultGroup();
			UserGroup itgroup = um.createGroup("IT Service");
			UserGroup admingroup = um.createGroup("Administrator");

			itgroup.addPermission("devices.view");
			itgroup.addPermission("devices.add");
			itgroup.addPermission("devices.modify");
			itgroup.addPermission("devices.remove");
			itgroup.addPermission("interventions.add");
			
			admingroup.addPermission("devices.view");
			admingroup.addPermission("devices.add");
			admingroup.addPermission("devices.modify");
			admingroup.addPermission("devices.remove");
			admingroup.addPermission("devicetypes.view");
			admingroup.addPermission("devicetypes.add");
			admingroup.addPermission("devicetypes.modify");
			admingroup.addPermission("devicetypes.remove");
			admingroup.addPermission("users.view");//TODO: Perm
			admingroup.addPermission("users.modify");//TODO: Perm
			admingroup.addPermission("interventions.add");
			
			um.update(itgroup);
			um.update(admingroup);
			
			User userAdmin = um.createUser("admin", "Admin", "Istrator", BCrypt.hashpw("adminadmin", BCrypt.gensalt()), admingroup);
			User userIT = um.createUser("ITuser", "IT", "User", BCrypt.hashpw("testme", BCrypt.gensalt()), itgroup);
			um.createUser("user", "Some", "User", BCrypt.hashpw("test", BCrypt.gensalt()), group);

			um.createUser("kremi151", "Michel", "Kremer", "$2a$10$wsGq2bJXf.E4sJzuQkVlNOvQ29jI8UjjMR7ECgOADan1k/5NJp2wi", group);

			DeviceType deviceGeneric = dm.createDeviceType("Generic device");
			DeviceType deviceComputer = dm.createDeviceType("Computer", deviceGeneric);
			DeviceType deviceDesktopPC = dm.createDeviceType("Desktop PC", deviceComputer);
			DeviceType deviceLaptop = dm.createDeviceType("Laptop", deviceComputer);
			DeviceType deviceTablet = dm.createDeviceType("Tablet PC", deviceComputer);
			DeviceType devicePhone = dm.createDeviceType("Mobile phone", deviceComputer);
			DeviceType devicePrinter = dm.createDeviceType("Printer", deviceGeneric);

			DeviceProperty propSerialNumber = dm.createDeviceProperty("Serial number", DevicePropertyType.SERIAL_NUMER, deviceGeneric);
			DeviceProperty propVendor = dm.createDeviceProperty("Vendor", DevicePropertyType.STRING, deviceGeneric);
			DeviceProperty propMAC = dm.createDeviceProperty("MAC Address", DevicePropertyType.MAC_ADDRESS, deviceComputer);
			DeviceProperty propOS = dm.createDeviceProperty("Operating system", DevicePropertyType.STRING, deviceComputer);
			DeviceProperty propStorage = dm.createDeviceProperty("Storage in GB", DevicePropertyType.UFLOAT, deviceComputer);
			DeviceProperty propRAM = dm.createDeviceProperty("RAM in GB", DevicePropertyType.UFLOAT, deviceComputer);
			dm.createDeviceProperty("Hard keyboard", DevicePropertyType.BOOLEAN, devicePhone);
			dm.createDeviceProperty("SIM slots", DevicePropertyType.UNUMBER, devicePhone);
			dm.createDeviceProperty("Has SIM slot", DevicePropertyType.BOOLEAN, deviceTablet);
			dm.createDeviceProperty("Paper slots", DevicePropertyType.UNUMBER, devicePrinter);
			dm.createDeviceProperty("Laser technology", DevicePropertyType.BOOLEAN, devicePrinter);
			
			Device device1 = dm.createDevice("Central Management PC", deviceDesktopPC, userAdmin);
			dm.addOrModifyDeviceProperty(device1, propSerialNumber, "H1T3-CHPC-1234-5678");
			dm.addOrModifyDeviceProperty(device1, propVendor, "IBM");
			dm.addOrModifyDeviceProperty(device1, propMAC, "ab:cd:ef:01:23:45");
			dm.addOrModifyDeviceProperty(device1, propOS, "Windows 3.1");
			dm.addOrModifyDeviceProperty(device1, propStorage, "0,25");
			dm.addOrModifyDeviceProperty(device1, propRAM, "0,0125");
			
			dm.addInterventionToDevice(device1, userIT, "Outdated operating system", "The operating should perhaps be updated. The current one is outdated and slow.");
			dm.addInterventionToDevice(device1, userAdmin, "Re: Outdated operating system", "I considered your request, but I decided to stay at Windows 3.1 because it still works.");
			
			//TODO: Add some default entries to be added before the actual submission of the project
			System.out.println("### Done ###");
		}
	}
}
