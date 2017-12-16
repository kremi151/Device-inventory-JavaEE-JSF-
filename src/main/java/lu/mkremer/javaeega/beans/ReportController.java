package lu.mkremer.javaeega.beans;

import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lu.mkremer.javaeega.intervention.Intervention;
import lu.mkremer.javaeega.intervention.Report;
import lu.mkremer.javaeega.intervention.ReportStatus;
import lu.mkremer.javaeega.managers.DeviceManager;
import lu.mkremer.javaeega.util.MessageHelper;

@ManagedBean(name="reportc")
@ViewScoped
public class ReportController {

	@EJB
	private DeviceManager dm;
	
	@NotNull
	private long reportId;
	
	@NotNull(message="A message must be provided")
	@Size(min=10, message="Message must be at least {min} characters long")
	private String interventionMessage;
	
	@NotNull
	private ReportStatus newReportStatus;
	
	public Report fetchReport(long id) {
		return dm.findReportById(id);
	}
	
	public List<Intervention> getInterventions(Report report){
		List<Intervention> list = dm.getInterventionsForReport(report);
		list.sort((a, b) -> a.getDate().compareTo(b.getDate()));
		return list;
	}
	
	public List<ReportStatus> getReportStatuses(){
		return Arrays.asList(ReportStatus.values());
	}

	public String getInterventionMessage() {
		return interventionMessage;
	}

	public void setInterventionMessage(String interventionMessage) {
		this.interventionMessage = interventionMessage;
	}
	
	public ReportStatus getNewReportStatus() {
		return newReportStatus;
	}

	public void setNewReportStatus(ReportStatus newReportStatus) {
		this.newReportStatus = newReportStatus;
	}

	public long getReportId() {
		return reportId;
	}

	public void setReportId(long reportId) {
		this.reportId = reportId;
	}

	public void intervene() {
		UserSession session = UserSession.getCurrentSession();
		if(session.canSubmitInterventions()) {
			Report report = dm.findReportById(reportId);
			if(report != null) {
				dm.interveneOnReport(report, session.getUser(), interventionMessage, newReportStatus);
			}else{
				MessageHelper.throwDangerMessage("Report was not found");
			}
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
}
