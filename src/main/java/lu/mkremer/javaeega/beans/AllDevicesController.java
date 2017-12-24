package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lu.mkremer.javaeega.devices.Device;
import lu.mkremer.javaeega.managers.DeviceManager;

@ManagedBean(name="alldevices")
@ViewScoped
public class AllDevicesController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4202403620728191597L;

	@EJB
	private DeviceManager dm;
	
	public List<Device> getAllDevices(){
		return dm.getAllDevices();
	}
	
}
