package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lu.mkremer.javaeega.devices.Device;
import lu.mkremer.javaeega.devices.DeviceProperty;
import lu.mkremer.javaeega.devices.DevicePropertyValue;
import lu.mkremer.javaeega.intervention.Report;
import lu.mkremer.javaeega.managers.DeviceManager;
import lu.mkremer.javaeega.util.MessageHelper;

@ManagedBean(name="vdevice")
@ViewScoped
public class ViewDeviceController implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -793544976222187966L;

	@EJB
	private DeviceManager dm;
	
	@NotNull
	@Size(min=1)
	private String propValue;
	
	@NotNull
	private long propId;
	
	@NotNull
	private long devId;

	public String getPropValue() {
		return propValue;
	}

	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}

	public long getPropId() {
		return propId;
	}

	public void setPropId(long propId) {
		this.propId = propId;
	}

	public long getDevId() {
		return devId;
	}

	public void setDevId(long devId) {
		this.devId = devId;
	}

	public Device getDeviceById(long devId) {
		return dm.getDeviceById(devId);
	}

	public List<DevicePropertyValue> getPropertiesForDevice(Device device){
		if(device != null) {
			HashMap<DeviceProperty, DevicePropertyValue> tmpMap = new HashMap<>();
			List<DevicePropertyValue> props = dm.getPropertiesForDevice(device);
			for(DevicePropertyValue val : props) {
				tmpMap.put(val.getProperty(), val);
			}
			List<DeviceProperty> devTypeProps = dm.getPropertiesForDeviceType(device.getType());
			for(DeviceProperty prop : devTypeProps) {
				if(!tmpMap.containsKey(prop)) {
					tmpMap.put(prop, new DevicePropertyValue(null, prop, "?"));//The created value will not be persisted in the database, it will only be used in JSF
				}
			}
			List<DevicePropertyValue> result = new ArrayList<>(tmpMap.values());
			result.sort((a, b) -> a.getProperty().getName().compareTo(b.getProperty().getName()));
			return result;
		}else {
			return Collections.emptyList();
		}
	}
	
	public void modifyProperty() {//TODO: Look here to know how custom messages are thrown
		DeviceProperty property = dm.getPropertyById(propId);
		if(property == null) {
			MessageHelper.throwDangerMessage("Unknown property");
			return;
		}
		if(!propValue.matches(property.getType().getRegEx())) {
			MessageHelper.throwDangerMessage("Expected a value of type " + property.getType().getDescription() + ", got \"" + propValue + "\"");
			return;
		}
		Device device = dm.getDeviceById(devId);
		if(device == null) {
			MessageHelper.throwDangerMessage("Unknown device");
			return;
		}
		if(UserSession.getCurrentSession().canModifyDevice(device)) {
			dm.addOrModifyDeviceProperty(device, property, propValue);
			devId = 0;
			propId = 0;
			propValue = null;
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
		
	}
	
	public String delete() {
		Device device = dm.getDeviceById(devId);
		if(device == null) {
			MessageHelper.throwDangerMessage("Unknown device");
			return null;
		}
		if(UserSession.getCurrentSession().canRemoveDevice(device)) {
			dm.removeDevice(device);
			return UserSession.getCurrentSession().canListDevices() ? "alldevices.xhtml" : "mydevices.xhtml";
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
			return null;
		}
	}
	
	public List<Report> getReports(Device device){
		List<Report> res = dm.getReportsForDevice(device);
		res.sort((a, b) -> a.getDate().compareTo(b.getDate()));
		return res;
	}
	
	//TODO: Interventions
	//TODO: Reference on the UI to the owner -> make link
	
}
