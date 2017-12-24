package lu.mkremer.javaeega.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.NotNull;

import lu.mkremer.javaeega.managers.DeviceManager;
import lu.mkremer.javaeega.util.MessageHelper;

@ViewScoped
@ManagedBean(name="rprop")
public class DevicePropertyRemoval implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7917242117212849851L;

	@NotNull
	private long propId;
	
	@EJB
	private DeviceManager dm;
	
	@ManagedProperty("#{usession}")
	private UserSession session;
	
	public void setSession(UserSession session) {
		this.session = session;
	}

	public long getPropId() {
		return propId;
	}

	public void setPropId(long propId) {
		this.propId = propId;
	}
	
	public void remove() {
		if(session.canModifyDeviceType()) {
			dm.removePropertyById(propId);
			propId = 0;
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
	
}
