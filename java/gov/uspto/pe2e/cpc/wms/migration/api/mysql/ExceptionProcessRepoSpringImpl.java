package gov.uspto.pe2e.cpc.wms.migration.api.mysql;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExceptionProcessRepoSpringImpl extends JpaRepository<ExceptionProcess, Integer> {

	Optional<ExceptionProcess> findByProcessInstanceId(String processInstanceId);

	Optional<ExceptionProcess> findByJobIdAndProcessInstanceId(String jobId, String processInstanceId);
	Optional<ExceptionProcess> findByJobIdAndProjectNum(String jobId, String projectNum);
	
	List<ExceptionProcess> findByJobId(String jobId);

}
