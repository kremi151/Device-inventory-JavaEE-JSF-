package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lu.mkremer.javaeega.consumables.Consumable;
import lu.mkremer.javaeega.devices.Device;
import lu.mkremer.javaeega.devices.DeviceProperty;
import lu.mkremer.javaeega.devices.DevicePropertyValue;
import lu.mkremer.javaeega.intervention.Report;
import lu.mkremer.javaeega.managers.ConsumableManager;
import lu.mkremer.javaeega.managers.DeviceManager;
import lu.mkremer.javaeega.util.MessageHelper;

@ManagedBean(name="vdevice")
@ViewScoped
public class ViewDeviceController implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -793544976222187966L;

	@EJB private DeviceManager dm;
	@EJB private ConsumableManager cm;
	
	@NotNull(message="No property value supplied")
	@Size(min=1, message="Property value must be at least {min} character(s) long")
	private String propValue;
	
	@NotNull
	private long propId;
	
	@NotNull(message="A title must be provided")
	@Size(min=8, max=128, message="Title must be between {min} and {max} characters long")
	private String reportTitle;
	
	@NotNull(message="A message must be provided")
	@Size(min=10, message="Message must be at least {min} characters long")
	private String reportMessage;
	
	@NotNull
	@Min(value=1, message="Value to increment or decrement must be 1 or higher")
	private int consumableIncrement = 1;
	
	@NotNull
	private long consumableId;
	
	private Device device;
	
	@ManagedProperty("#{usession}")
	private UserSession session;
	
	@PostConstruct
	public void init() {
		if(session.isLoggedIn()) {
			FacesContext fc = FacesContext.getCurrentInstance();
			String rawDevid = fc.getExternalContext().getRequestParameterMap().get("devid");
			if(rawDevid != null) {
				try {
					long devid = Long.parseLong(rawDevid);
					this.device = dm.getDeviceById(devid);
				}catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void preRenderPage(ComponentSystemEvent event) {
		if(device != null && session.canReadDevice(device)) {
			List<Consumable> consumables = cm.getConsumablesForDevice(device);
			int criticals = 0;
			for(Consumable c : consumables) {
				if(c.getAmount() <= c.getType().getCritical()) {
					criticals++;
				}
			}
			if(criticals > 0) {
				MessageHelper.throwWarningMessage(String.format("The stock of %d consumable(s) are low in quantity", criticals));
			}
		}
	}

	public void setSession(UserSession session) {
		this.session = session;
	}

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

	public Device getDevice() {
		return this.device;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getReportMessage() {
		return reportMessage;
	}

	public void setReportMessage(String reportMessage) {
		this.reportMessage = reportMessage;
	}

	public int getConsumableIncrement() {
		return consumableIncrement;
	}

	public void setConsumableIncrement(int consumableIncrement) {
		this.consumableIncrement = consumableIncrement;
	}

	public long getConsumableId() {
		return consumableId;
	}

	public void setConsumableId(long consumableId) {
		this.consumableId = consumableId;
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
					tmpMap.put(prop, new DevicePropertyValue(device, prop, "?"));//The created value will not be persisted in the database, it will only be used in JSF
				}
			}
			List<DevicePropertyValue> result = new ArrayList<>(tmpMap.values());
			result.sort((a, b) -> a.getProperty().getName().compareTo(b.getProperty().getName()));
			return result;
		}else {
			return Collections.emptyList();
		}
	}
	
	public void modifyProperty() {
		try {
			DeviceProperty property = dm.getPropertyById(propId);
			if(property == null) {
				MessageHelper.throwDangerMessage("Unknown property");
				return;
			}
			if(!propValue.matches(property.getType().getRegEx())) {
				MessageHelper.throwDangerMessage("Expected a value of type " + property.getType().getDescription() + ", got \"" + propValue + "\"");
				return;
			}
			if(device == null) {
				MessageHelper.throwDangerMessage("Unknown device");
				return;
			}
			if(session.canModifyDevice(device)) {
				dm.addOrModifyDeviceProperty(device, property, propValue);
				propId = 0;
				propValue = null;
			}else {
				MessageHelper.throwDangerMessage("You are not allowed to do this");
			}
		}catch(Exception e) {
			MessageHelper.throwDangerMessage("An unexpected error occured: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String delete() {
		try {
			if(device == null) {
				MessageHelper.throwDangerMessage("Unknown device");
			}else if(session.canRemoveDevice(device)) {
				dm.removeDevice(device);
				return session.canListDevices() ? "alldevices.xhtml" : "mydevices.xhtml";
			}else {
				MessageHelper.throwDangerMessage("You are not allowed to do this");
			}
		}catch(Exception e) {
			MessageHelper.throwDangerMessage("An unexpected error occured: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public void report() {
		try {
			if(device != null && session.canReportOnDevice(device)) {
				dm.createReportOnDevice(device, session.getUser(), reportTitle, reportMessage);
			}else {
				MessageHelper.throwDangerMessage("You are not allowed to do this");
			}
		}catch(Exception e) {
			MessageHelper.throwDangerMessage("An unexpected error occured: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public List<Report> getReports(Device device){
		List<Report> res = dm.getReportsForDevice(device);
		res.sort((a, b) -> a.getDate().compareTo(b.getDate()));
		return res;
	}
	
	public List<Consumable> getConsumables(Device device){
		return cm.getConsumablesForDevice(device);
	}
	
	public void cincrement() {
		if(session.canModifyConsumables()) {
			Consumable c = cm.getConsumableById(consumableId);
			if(c != null) {
				if(c.getAmount() + consumableIncrement <= Integer.MAX_VALUE) {
					c.setAmount(c.getAmount() + consumableIncrement);
					cm.update(c);
				}else {
					MessageHelper.throwDangerMessage("The requested increasement for consumable \"" + c.getType().getName() + "\" would exceed it's limit");
				}
			}else {
				MessageHelper.throwDangerMessage("Consumable was not found");
			}
			consumableIncrement = 1;
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
	
	public void cdecrement() {
		if(session.canModifyConsumables()) {
			Consumable c = cm.getConsumableById(consumableId);
			if(c != null) {
				if(c.getAmount() - consumableIncrement >= 0) {
					c.setAmount(c.getAmount() - consumableIncrement);
					cm.update(c);
				}else {
					MessageHelper.throwDangerMessage("The stock for consumable \"" + c.getType().getName() + "\" cannot be negative");
				}
			}else {
				MessageHelper.throwDangerMessage("Consumable was not found");
			}
			consumableIncrement = 1;
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
	
	public void deleteConsumable() {
		cm.deleteConsumableById(consumableId);
	}
	
}
