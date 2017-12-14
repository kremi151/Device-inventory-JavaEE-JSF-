package lu.mkremer.javaeega.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lu.mkremer.javaeega.devices.Device;
import lu.mkremer.javaeega.managers.DeviceManager;
import lu.mkremer.javaeega.util.MessageHelper;

@ManagedBean(name="intervention")
@ViewScoped
public class InterventionController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3193130332317095597L;
	
	@NotNull(message="A title must be provided")
	@Size(min=8, max=128, message="Title must be between {min} and {max} characters long")
	private String reportTitle;
	
	@NotNull(message="A message must be provided")
	@Size(min=10, message="Message must be at least {min} characters long")
	private String reportMessage;
	
	@NotNull
	private long devId;
	
	@EJB
	private DeviceManager dm;

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

	public long getDevId() {
		return devId;
	}

	public void setDevId(long devId) {
		this.devId = devId;
	}
	
	public void report() {
		UserSession session = UserSession.getCurrentSession();
		Device device = dm.getDeviceById(devId);
		if(device != null && session.canModifyDevice(device)) {
			dm.createReportOnDevice(device, session.getUser(), reportTitle, reportMessage);
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}

}
