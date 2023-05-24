package gov.uspto.pe2e.cpc.wms.migration.api.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AnalysisProcessInfoRepoSpringImpl  extends JpaRepository<AnalysisProcessInfo, Integer>{
	
	List<AnalysisProcessInfo> findByJobId(int jobId);
}
