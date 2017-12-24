package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lu.mkremer.javaeega.devices.DevicePropertyValue;
import lu.mkremer.javaeega.managers.SearchManager;
import lu.mkremer.javaeega.util.SearchResult;

@ManagedBean(name="searcher")
@ViewScoped
public class SearchController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9138238146742995198L;
	
	@ManagedProperty("#{usession}")
	private UserSession session;
	
	@EJB private SearchManager searchManager;
	
	private List<SearchResult> searchResults = new LinkedList<>();
	
	public void setSession(UserSession session) {
		this.session = session;
	}

	@PostConstruct
	public void init() {
		if(session.isLoggedIn()) {
			String searchQuery = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("query");
			if(searchQuery != null) {
				if(session.canListDevices()) {
					List<DevicePropertyValue> props = searchManager.searchForProperties(searchQuery);
					for(DevicePropertyValue val : props) {
						searchResults.add(
								new SearchResult("Device: " + val.getDevice().getName(),
										val.getProperty().getName() + ": " + val.getValue(),
										"device.xhtml",
										new SearchResult.OutcomeId("devid", String.valueOf(val.getDevice().getId()))
										));
					}
				}
			}
		}
	}

	public List<SearchResult> getSearchResults() {
		return searchResults;
	}

}
