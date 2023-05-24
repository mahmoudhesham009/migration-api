package gov.uspto.pe2e.cpc.wms.migration.api.adapter;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionProcessBody {

	private String processInstanceId;
	private String projectNum;
	private String jobId;

}
