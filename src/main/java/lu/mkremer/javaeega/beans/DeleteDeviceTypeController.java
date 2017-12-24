package lu.mkremer.javaeega.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lu.mkremer.javaeega.managers.DeviceManager;
import lu.mkremer.javaeega.util.MessageHelper;

@ManagedBean(name="rmdevtype")
@ViewScoped
public class DeleteDeviceTypeController implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3261666010400643388L;
	
	@EJB
	private DeviceManager dm;
	
	@ManagedProperty("#{usession}")
	private UserSession session;
	
	public void setSession(UserSession session) {
		this.session = session;
	}

	public String deleteDeviceType() {
		if(session.canRemoveDeviceTypes()) {
			try {
				long id = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
				dm.removeDeviceTypeById(id);
				return "devicetypes.xhtml";
			}catch(NumberFormatException | NullPointerException e) {
				MessageHelper.throwDangerMessage("Invalid device type id");
				return "devicetype.xhtml";
			}
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
			return "devicetype.xhtml";
		}
		
	}
	
}
