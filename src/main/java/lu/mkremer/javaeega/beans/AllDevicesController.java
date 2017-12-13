package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	
	@NotNull
	@Size(min=5, max=100)
	private String ndeviceName;
	
	public List<Device> getAllDevices(){
		return dm.getAllDevices();
	}
	
	public String getDeviceName() {
		return ndeviceName;
	}
	
	public void setDeviceName(String name) {
		this.ndeviceName = name;
	}
}
