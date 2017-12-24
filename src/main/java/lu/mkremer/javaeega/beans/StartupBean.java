package lu.mkremer.javaeega.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.mindrot.jbcrypt.BCrypt;

import lu.mkremer.javaeega.consumables.Consumable;
import lu.mkremer.javaeega.consumables.ConsumableType;
import lu.mkremer.javaeega.devices.Device;
import lu.mkremer.javaeega.devices.DeviceProperty;
import lu.mkremer.javaeega.devices.DevicePropertyType;
import lu.mkremer.javaeega.devices.DeviceType;
import lu.mkremer.javaeega.intervention.Report;
import lu.mkremer.javaeega.intervention.ReportStatus;
import lu.mkremer.javaeega.managers.ConsumableManager;
import lu.mkremer.javaeega.managers.DeviceManager;
import lu.mkremer.javaeega.managers.MessageManager;
import lu.mkremer.javaeega.managers.UserManager;
import lu.mkremer.javaeega.users.User;
import lu.mkremer.javaeega.users.UserGroup;

@Singleton
@Startup
public class StartupBean {//TODO: Run it with some default values before submission
	
	private final static boolean INIT_DEFAULT_VALUES = false;
	
	@EJB private UserManager um;
	@EJB private DeviceManager dm;
	@EJB private ConsumableManager cm;
	@EJB private MessageManager mm;

	@SuppressWarnings("unused")
	@PostConstruct
	public void init() {
		if(INIT_DEFAULT_VALUES) {
			System.out.println("###############################");
			System.out.println("# Initializing default values #");
			System.out.println("###############################");
			
			System.out.println("### Creating user groups ###");
			
			UserGroup group = um.getDefaultGroup();
			UserGroup itgroup = um.createGroup("IT Service");
			UserGroup admingroup = um.createGroup("Administrator");

			System.out.println("### Adding permissions ###");
			
			itgroup.addPermission("devices.view");
			itgroup.addPermission("devices.add");
			itgroup.addPermission("devices.modify");
			itgroup.addPermission("devices.remove");
			itgroup.addPermission("interventions.add");
			itgroup.addPermission("consumables.view");
			itgroup.addPermission("consumables.add");
			itgroup.addPermission("consumables.modify");
			itgroup.addPermission("consumables.remove");
			
			admingroup.addPermission("devices.view");
			admingroup.addPermission("devices.add");
			admingroup.addPermission("devices.modify");
			admingroup.addPermission("devices.remove");
			admingroup.addPermission("devicetypes.view");
			admingroup.addPermission("devicetypes.add");
			admingroup.addPermission("devicetypes.modify");
			admingroup.addPermission("devicetypes.remove");
			admingroup.addPermission("usergroups.view");
			admingroup.addPermission("usergroups.modify");
			admingroup.addPermission("users.view");
			admingroup.addPermission("users.modify");
			admingroup.addPermission("interventions.add");
			admingroup.addPermission("consumables.view");
			admingroup.addPermission("consumables.add");
			admingroup.addPermission("consumables.modify");
			admingroup.addPermission("consumables.remove");
			admingroup.addPermission("consumabletypes.view");
			admingroup.addPermission("consumabletypes.add");
			admingroup.addPermission("consumabletypes.modify");
			admingroup.addPermission("consumabletypes.remove");
			admingroup.addPermission("messages.info");//TODO: Perm
			admingroup.addPermission("messages.warning");//TODO: Perm
			admingroup.addPermission("messages.severe");//TODO: Perm
			
			um.update(itgroup);
			um.update(admingroup);

			System.out.println("### Creating users ###");;
			
			User userAdmin = um.createUser("admin", "Admin", "Istrator", BCrypt.hashpw("adminadmin", BCrypt.gensalt()), admingroup);
			User userIT = um.createUser("ITuser", "IT", "User", BCrypt.hashpw("testme", BCrypt.gensalt()), itgroup);
			um.createUser("user", "Some", "User", BCrypt.hashpw("test", BCrypt.gensalt()), group);

			um.createUser("kremi151", "Michel", "Kremer", "$2a$10$wsGq2bJXf.E4sJzuQkVlNOvQ29jI8UjjMR7ECgOADan1k/5NJp2wi", group);
			
			System.out.println("### Creating device types ###");

			DeviceType deviceGeneric = dm.createDeviceType("Generic device");
			DeviceType deviceComputer = dm.createDeviceType("Computer", deviceGeneric);
			DeviceType deviceDesktopPC = dm.createDeviceType("Desktop PC", deviceComputer);
			DeviceType deviceLaptop = dm.createDeviceType("Laptop", deviceComputer);
			DeviceType deviceTablet = dm.createDeviceType("Tablet PC", deviceComputer);
			DeviceType devicePhone = dm.createDeviceType("Mobile phone", deviceComputer);
			DeviceType devicePrinter = dm.createDeviceType("Printer", deviceGeneric);
			
			System.out.println("### Creating device properties ###");

			DeviceProperty propSerialNumber = dm.createDeviceProperty("Serial number", DevicePropertyType.SERIAL_NUMER, deviceGeneric, "serial,number,snumber,nbr");
			DeviceProperty propVendor = dm.createDeviceProperty("Vendor", DevicePropertyType.STRING, deviceGeneric, "vendor,oem,manufacturer");
			DeviceProperty propMAC = dm.createDeviceProperty("MAC Address", DevicePropertyType.MAC_ADDRESS, deviceComputer, "mac,address");
			DeviceProperty propOS = dm.createDeviceProperty("Operating system", DevicePropertyType.STRING, deviceComputer, "operating,system,os");
			DeviceProperty propStorage = dm.createDeviceProperty("Storage in GB", DevicePropertyType.UFLOAT, deviceComputer, "storage,disk,memory");
			DeviceProperty propRAM = dm.createDeviceProperty("RAM in GB", DevicePropertyType.UFLOAT, deviceComputer, "ram,random,access,memory");
			dm.createDeviceProperty("Hard keyboard", DevicePropertyType.BOOLEAN, devicePhone, "hard,keyboard");
			dm.createDeviceProperty("SIM slots", DevicePropertyType.UNUMBER, devicePhone, "sim,slots");
			dm.createDeviceProperty("Has SIM slot", DevicePropertyType.BOOLEAN, deviceTablet, "has,sim,slots");
			dm.createDeviceProperty("Paper slots", DevicePropertyType.UNUMBER, devicePrinter, "paper,slots");
			dm.createDeviceProperty("Laser technology", DevicePropertyType.BOOLEAN, devicePrinter, "laser,technology,printing,printer,paper");
			
			System.out.println("### Creating consumable types ###");
			
			ConsumableType ctUsbStick = cm.createConsumableType("USB Stick", 3, deviceComputer);
			
			System.out.println("### Creating devices ###");
			
			Device device1 = dm.createDevice("Central Management PC", deviceDesktopPC, userAdmin);
			dm.addOrModifyDeviceProperty(device1, propSerialNumber, "H1T3-CHPC-1234-5678");
			dm.addOrModifyDeviceProperty(device1, propVendor, "IBM");
			dm.addOrModifyDeviceProperty(device1, propMAC, "ab:cd:ef:01:23:45");
			dm.addOrModifyDeviceProperty(device1, propOS, "Windows 3.1");
			dm.addOrModifyDeviceProperty(device1, propStorage, "0,25");
			dm.addOrModifyDeviceProperty(device1, propRAM, "0,0125");

			System.out.println("### Creating consumable types ###");
			
			cm.createConsumableForDevice(ctUsbStick, 11, device1);
			
			System.out.println("### Creating reports ###");
			
			Report report1 = dm.createReportOnDevice(device1, userIT, "Outdated operating system", "The operating should perhaps be updated. The current one is outdated and slow.");
			dm.interveneOnReport(report1, userAdmin, "I considered your request, but I decided to stay at Windows 3.1 because it still works.", ReportStatus.UNSOLVABLE);
			
			System.out.println("### Done ###");
		}
		
		System.out.println("###########################################");
		System.out.println("# Scanning for critical consumable stocks #");
		System.out.println("###########################################");
		
		List<Consumable> consumables = cm.getCriticalConsumables();
		for(Consumable c : consumables) {
			mm.notifyConsumableStock(c);
		}
		
		System.out.println("### Done, found " + consumables.size() + " ###");
	}
	
}
