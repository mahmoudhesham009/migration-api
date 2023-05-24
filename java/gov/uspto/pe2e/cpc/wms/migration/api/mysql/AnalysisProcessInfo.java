package gov.uspto.pe2e.cpc.wms.migration.api.mysql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/// selection from drop-down list feature (exception)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "analysisProcessInfo")
public class AnalysisProcessInfo {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	private int jobId;
	private String processInstanceId;
	private String projectNumber;
	private String name;
}
