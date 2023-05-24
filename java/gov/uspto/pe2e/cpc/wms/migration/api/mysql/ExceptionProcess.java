package gov.uspto.pe2e.cpc.wms.migration.api.mysql;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Component
@Table(name = "exception_Process")
public class ExceptionProcess implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	private String jobId;

	private String processInstanceId;
	
	private String projectNum;

	@Lob
	private String analysisObject;
	
	@Lob
	private String analysisObjectBeforeSave;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@JsonIgnore
	public String getAnalysisObject() {
		return analysisObject;
	}

	@JsonSetter
	public void setAnalysisObject(String analysisObject) {
		this.analysisObject = analysisObject;
	}

	public String getProjectNum() {
		return projectNum;
	}

	public void setProjectNum(String projectNum) {
		this.projectNum = projectNum;
	}

	public String getAnalysisObjectBeforeSave() {
		return analysisObjectBeforeSave;
	}

	public void setAnalysisObjectBeforeSave(String analysisObjectBeforeSave) {
		this.analysisObjectBeforeSave = analysisObjectBeforeSave;
	}
	
}
