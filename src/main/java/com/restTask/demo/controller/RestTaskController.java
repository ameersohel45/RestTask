package com.restTask.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.restTask.demo.entity.Datasets;
import com.restTask.demo.entity.Status;
import com.restTask.demo.repo.DataRepository;
import com.restTask.demo.service.RestTaskService;
import com.restTask.demo.validator.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/datasets")
public class RestTaskController {

	private final RestTaskService restTaskService;
	private final ObjectMapper objectMapper;
	private final Validator validator;

	public RestTaskController(RestTaskService restTaskService, ObjectMapper objectMapper, Validator validator) {
		this.restTaskService = restTaskService;
		this.objectMapper = objectMapper;
		this.validator = validator;
	}

	@GetMapping("/get")
	public ResponseEntity<Map<String, Object>> getDatasets() {
		return restTaskService.getAllDatasets();
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Map<String, Object>> getDatasetById(@PathVariable String id) {
		return restTaskService.getDatasetById(id);	
	}

	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> createDataset(@RequestBody String json) {

		return restTaskService.createDataset(json);
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<Map<String, Object>> updateDataset(
	        @PathVariable String id, @RequestBody String json) {
	    return restTaskService.updateDataset(id, json);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String,Object>> deleteById(@PathVariable String id)
	{
		return restTaskService.delete(id);
	}

}