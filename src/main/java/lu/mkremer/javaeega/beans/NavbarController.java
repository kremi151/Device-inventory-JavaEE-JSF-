package lu.mkremer.javaeega.beans;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="navbarcon")
@SessionScoped
public class NavbarController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2641854698240518172L;
	
	private String searchQuery;
	
	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public String search() {
		try {
			return "/search.xhtml?faces-redirect=true&query=" + URLEncoder.encode(searchQuery, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "search.xhtml";
		}
	}

}
