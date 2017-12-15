package lu.mkremer.javaeega.intervention;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lu.mkremer.javaeega.users.User;

@Entity
public class Intervention {
	
	@Id
	@GeneratedValue
	private long id;

	@ManyToOne(optional = false)
	private Report report;
	
	@ManyToOne(optional = false)
	private User responsible;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date = Calendar.getInstance().getTime();
	
	@Column(nullable = false)
	private String message;
	
	public Intervention() {}
	
	public Intervention(Report report, User responsible, String message) {
		this.report = report;
		this.responsible = responsible;
		this.message = message;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public User getResponsible() {
		return responsible;
	}

	public void setResponsible(User responsible) {
		this.responsible = responsible;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getId() {
		return id;
	}
}
