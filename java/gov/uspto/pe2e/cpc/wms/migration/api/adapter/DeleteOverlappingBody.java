package gov.uspto.pe2e.cpc.wms.migration.api.adapter;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteOverlappingBody {

	public List<String> processIDs;
	String fromDate;
	String toDate;
	
}
