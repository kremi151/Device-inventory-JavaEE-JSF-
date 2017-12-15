package lu.mkremer.javaeega.intervention;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lu.mkremer.javaeega.devices.Device;
import lu.mkremer.javaeega.users.User;

@Entity
public class Report {

	@Id
	@GeneratedValue
	private long id;
	
	@Basic(optional=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date = Calendar.getInstance().getTime();
	
	@ManyToOne(optional=false)
	private Device device;
	
	@ManyToOne(optional=false)
	private User user;

	@Column(nullable=false)
	private String title;
	
	@Column(nullable=false)
	private String message;

	@Enumerated(EnumType.STRING)
	private ReportStatus status = ReportStatus.OPEN;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="report")
	private List<Intervention> interventions;
	
	public Report() {}
	
	public Report(Device device, User user, String title, String message) {
		this.device = device;
		this.user = user;
		this.title = title;
		this.message = message;
	}

	public long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

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

	public ReportStatus getStatus() {
		return status;
	}

	public void setStatus(ReportStatus status) {
		this.status = status;
	}
	
}
