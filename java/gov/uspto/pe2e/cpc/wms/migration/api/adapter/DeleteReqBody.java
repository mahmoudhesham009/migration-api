package gov.uspto.pe2e.cpc.wms.migration.api.adapter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeleteReqBody {
	int jobId;
	String processInstanceId;
	String name;

}
