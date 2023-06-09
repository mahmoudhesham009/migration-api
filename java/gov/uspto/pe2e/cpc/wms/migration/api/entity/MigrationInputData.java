package gov.uspto.pe2e.cpc.wms.migration.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MigrationInputData {

	private String fromDate;
	private String toDate;
	private String ins;
	private boolean singleProcess=false;
	private String processUUID; 
}
