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

@ManagedBean(name="postintv")
@ViewScoped
public class PostInterventionController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3193130332317095597L;
	
	@NotNull
	@Size(min=8, max=128)
	private String title;
	
	@NotNull
	@Size(min=10)
	private String message;
	
	@NotNull
	private long devId;
	
	@EJB
	private DeviceManager dm;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getDevId() {
		return devId;
	}

	public void setDevId(long devId) {
		this.devId = devId;
	}
	
	public void post() {
		UserSession session = UserSession.getCurrentSession();
		if(session.canSubmitInterventions()) {
			Device device = dm.getDeviceById(devId);
			if(device != null) {
				dm.addInterventionToDevice(device, session.getUser(), title, message);
			}else {
				MessageHelper.throwDangerMessage("Device was not found");
			}
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
		
	}

}
