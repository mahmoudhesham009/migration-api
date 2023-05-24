package gov.uspto.pe2e.cpc.wms.migration.api.adapter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import gov.uspto.pe2e.cpc.wms.migration.api.entity.MigrationInputData;
import gov.uspto.pe2e.cpc.wms.migration.api.mysql.AnalysisProcessInfo;
import gov.uspto.pe2e.cpc.wms.migration.api.mysql.AnalysisProcessInfoRepoSpringImpl;
import gov.uspto.pe2e.cpc.wms.migration.api.mysql.ExceptionProcess;
import gov.uspto.pe2e.cpc.wms.migration.api.mysql.ExceptionProcessRepoSpringImpl;
import gov.uspto.pe2e.cpc.wms.migration.api.mysql.MigrationJob;
import gov.uspto.pe2e.cpc.wms.migration.api.mysql.MigrationJobRepoSpringImpl;

@RestController
@RequestMapping("/api")
public class Controllers {

	@Autowired
	MigrationJobRepoSpringImpl migrationJobRepoSpringImpl;

	@Autowired
	ExceptionProcessRepoSpringImpl exceptionProcessRepoSpringImpl;
	
	@Autowired
	AnalysisProcessInfoRepoSpringImpl analysisProcessInfoSpringImpl;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private Environment env;
	
	@RequestMapping(value="/singleInstance",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public  String getJobForSingleInstance(@RequestBody MigrationInputData migrationInputData) {
		HttpHeaders httpHeaders=new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<MigrationInputData> requestEntity = new HttpEntity<>(migrationInputData,httpHeaders);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(env.getProperty("analysis.uri") 
				+ "/analysisProcess/singleInstance/" , requestEntity,String.class);
		return responseEntity.getBody();
	}
	
	//==============================================
	@RequestMapping(value="/confirmDeleteAllNewProcesses",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public  String confirmDeleteAllNewProcesses(@RequestBody List<String> processesIDs) {
		HttpHeaders httpHeaders=new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<List<String>> requestEntity = new HttpEntity<>(processesIDs,httpHeaders);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(env.getProperty("analysis.uri") 
				+ "/analysisProcess/confirmDeleteAllNewProcesses/" , requestEntity,String.class);
		return responseEntity.getBody();
	}
	@RequestMapping(value="/checkOverlapping",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String checkOverlapping(@RequestBody MigrationInputData migrationInputData) {
		HttpHeaders httpHeaders=new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<MigrationInputData> requestEntity = new HttpEntity<>(migrationInputData,httpHeaders);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(env.getProperty("analysis.uri") 
				+ "/analysisProcess/checkOverlapping/" , requestEntity,String.class);
		return responseEntity.getBody();
	}
//	@PostMapping("/deleteProcess")
	@RequestMapping(value="/deleteProcess",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String deleteOriginalProcessByName(@RequestBody DeleteReqBody deleteReqBody) {
		HttpHeaders httpHeaders=new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<DeleteReqBody> requestEntity = new HttpEntity<>(deleteReqBody,httpHeaders);
		/// Engine controller >> DELETE mapping  (restTemplate)
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(env.getProperty("migration.uri") 
				+ "/migrationProcess/deleteProcessInst/" , requestEntity,String.class);
		return responseEntity.getBody();
	} //=============================================
	
	@GetMapping("/getAllJobs")
	public List<MigrationJob> getAlljobs() {
		List<MigrationJob> jobs = migrationJobRepoSpringImpl.findAll();
		return jobs;
	}

	@GetMapping("/getAllExceptionProcessByJobId/{jobId}")
	public List<ExceptionProcess> getAllExceptionProcess(@PathVariable int jobId) {
		List<ExceptionProcess> exceptionProcesses = exceptionProcessRepoSpringImpl.findByJobId(String.valueOf(jobId));
		return exceptionProcesses;
	}
	@GetMapping("/getAllProcessAnalysisInfoByJobId/{jobId}")
	public List<AnalysisProcessInfo> getAllProcessAnalsisInfo(@PathVariable int jobId) {
		List<AnalysisProcessInfo> analysisProcesses = analysisProcessInfoSpringImpl.findByJobId(jobId);
		return analysisProcesses;
	}
	@PostMapping("/getExceptionProcessByInstanceIdAndJobId")
	public String getExceptionProcessByInstanceId(@RequestBody ExceptionProcessBody processBody) {
//		Optional<ExceptionProcess> exceptionProcesse = exceptionProcessRepoSpringImpl
//				.findByJobIdAndProcessInstanceId(processBody.getJobId(), processBody.getProcessInstanceId());
//		return exceptionProcesse.get().getAnalysisObject().toString();
		ExceptionProcessBody body = new ExceptionProcessBody();
		body.setJobId(processBody.getJobId());
		body.setProcessInstanceId(processBody.getProcessInstanceId());
		System.out.print("+++++++++++++++++++++++++++++++++++++++"+analysisExceptionProcess(body).getBody());
//		JSONObject jsonObject = new JSONObject(analysisExceptionProcess(body).getBody().toString());
		return analysisExceptionProcess(body).getBody();

	}

	@PostMapping("/deleteExceptionProcess")
	public void deleteExceptionProcess(@RequestBody ExceptionProcessBody processBody) {
		Optional<ExceptionProcess> exceptionProcesse = exceptionProcessRepoSpringImpl
				.findByJobIdAndProcessInstanceId(processBody.getJobId(), processBody.getProcessInstanceId());

		if (exceptionProcesse.isPresent()) {
			exceptionProcessRepoSpringImpl.delete(exceptionProcesse.get());
		}
	}

	@GetMapping("/getJobsById/{jobId}")
	public String getJobsById(@PathVariable int jobId) {
		Optional<MigrationJob> job = migrationJobRepoSpringImpl.findById(jobId);
		if (job.isPresent()) {
			return job.get().getAnalysisObject().toString();
		}
		return "";

	}

	@PatchMapping("/update/{jobId}")
	public void updateJob(@RequestBody String jsonFromUI) {
		JSONObject fromUi = new JSONObject(jsonFromUI.toString());
		Optional<MigrationJob> migrationJobFromDb = Optional.empty();
		Optional<ExceptionProcess> exceptionJobFromDb = Optional.empty();
		JSONArray array;

		if (fromUi.optString("viewType").equals("exception")) {
			if (!fromUi.optString("processInstanceId").isEmpty()) {
				exceptionJobFromDb = exceptionProcessRepoSpringImpl.findByJobIdAndProcessInstanceId(
						fromUi.optString("jobID"), fromUi.optString("processInstanceId"));
			} else {
				exceptionJobFromDb = exceptionProcessRepoSpringImpl.findByJobIdAndProjectNum(fromUi.optString("jobID"),
						fromUi.optString("projectNum"));
			}

			array = new JSONArray(exceptionJobFromDb.get().getAnalysisObject());
		} else {
			migrationJobFromDb = migrationJobRepoSpringImpl.findById(Integer.valueOf(fromUi.optString("jobID")));
			array = new JSONArray(migrationJobFromDb.get().getAnalysisObject().toString());
		}
		for (int z = 0; z < array.length(); z++) {
			JSONObject object2 = objectToJSONObject(array.get(z));
			for (int j = 0; j < object2.optJSONArray("data").length(); j++) {
				JSONObject jsonFromDB = objectToJSONObject(object2.optJSONArray("data").get(j));
				if (checkJson(object2.optString("DeploymentId"), jsonFromDB, fromUi,
						String.valueOf(fromUi.optString("jobID")))) {
					for (int i = 0; i < jsonFromDB.optJSONObject(fromUi.optString("taskDef"))
							.optJSONArray("formFieldList").length(); i++) {

						JSONObject fieldJsonObject = objectToJSONObject(jsonFromDB
								.optJSONObject(fromUi.optString("taskDef")).optJSONArray("formFieldList").get(i));
						for (Object object7 : fromUi.optJSONArray("formValues")) {
							JSONObject object8 = objectToJSONObject(object7);
							if (fieldJsonObject.optString("id").equals(object8.optString("id"))) {

								((JSONObject) array.get(z)).optJSONArray("data").getJSONObject(j)
										.getJSONObject(fromUi.optString("taskDef")).optJSONArray("formFieldList")
										.getJSONObject(i).put("value", object8.optString("value"));
								break;
							}
						}
					}
				}
			}
		}
		if (fromUi.optString("viewType").equals("exception")) {
			exceptionJobFromDb.get().setAnalysisObject(array.toString());
			exceptionProcessRepoSpringImpl.saveAndFlush(exceptionJobFromDb.get());
		} else {
			migrationJobFromDb.get().setAnalysisObject(array.toString());
			migrationJobRepoSpringImpl.saveAndFlush(migrationJobFromDb.get());
		}

	}

	@PostMapping("/createNewJob")
	public ResponseEntity<Object> createNewJob(@RequestBody MigrationInputData inputData) {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<MigrationInputData> entity = new HttpEntity<MigrationInputData>(inputData, headers);

		ResponseEntity<Object> responseEntity = restTemplate
				.postForEntity(env.getProperty("analysis.uri") + "/analysisProcess/analysis", entity, Object.class);
		return responseEntity;
	}

	@GetMapping("/startMigration/{jobId}")
	public ResponseEntity<Object> startMigration(@PathVariable int jobId) {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		ResponseEntity<Object> responseEntity = restTemplate
				.getForEntity(env.getProperty("migration.uri") + "/migrationProcess/" + jobId, Object.class);
		return responseEntity;
	}

	@GetMapping("/Report/{jobId}")
	public ResponseEntity<Object> generateReport(@PathVariable int jobId) {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		ResponseEntity<Object> responseEntity = restTemplate
				.getForEntity(env.getProperty("migration.uri") + "/migrationProcess/Report/" + jobId, Object.class);
		return responseEntity;
	}

	@PostMapping("/analysisExceptionProcess")
	public ResponseEntity<String> analysisExceptionProcess(@RequestBody ExceptionProcessBody processBody) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<ExceptionProcessBody> entity = new HttpEntity<ExceptionProcessBody>(processBody, headers);

		ResponseEntity<String> responseEntity = restTemplate
				.postForEntity(env.getProperty("analysis.uri") + "/analysisProcess/exception", entity, String.class);
		return responseEntity;
	}

	private boolean checkJson(String depolymentId, JSONObject fromDB, JSONObject jsonFromUI, String jobId) {
		boolean isSameJob = false;
		boolean isSameDeploymentId = false;
		boolean isSameTaskDef = false;
		boolean isEqual = false;

		if (jsonFromUI.optString("jobID").equals(jobId)) {
			isSameJob = true;
		}
		if (depolymentId.equals(jsonFromUI.optString("deploymentID"))) {
			isSameDeploymentId = true;
		}
		if (!(fromDB.optJSONObject(jsonFromUI.optString("taskDef")) == null)) {
			if (fromDB.optJSONObject(jsonFromUI.optString("taskDef")).optString("taskDef")
					.equals(jsonFromUI.optString("taskDef"))) {
				isSameTaskDef = true;
			}

		} else {
			isSameTaskDef = false;
		}
		if (isSameJob && isSameDeploymentId && isSameTaskDef) {
			isEqual = true;
		}
		return isEqual;
	}

	public static JSONObject objectToJSONObject(Object object) {
		Object json = null;
		JSONObject jsonObject = null;
		try {
			json = new JSONTokener(object.toString()).nextValue();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (json instanceof JSONObject) {
			jsonObject = (JSONObject) json;
		}
		return jsonObject;
	}
	
}
