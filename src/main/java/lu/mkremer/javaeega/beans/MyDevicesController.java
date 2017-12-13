package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lu.mkremer.javaeega.devices.Device;
import lu.mkremer.javaeega.managers.DeviceManager;

@ManagedBean(name="mydevices")
@ViewScoped
public class MyDevicesController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7614781136266118026L;
	
	@EJB
	private DeviceManager dm;
	
	public List<Device> getMyDevices(){
		UserSession session = UserSession.getCurrentSession();
		if(session.isLoggedIn()) {
			return dm.getDevicesOfUser(session.getUser());
		}else {
			return Collections.emptyList();
		}
	}
}
