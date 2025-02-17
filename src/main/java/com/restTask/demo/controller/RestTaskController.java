package com.restTask.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restTask.demo.entity.Datasets;
import com.restTask.demo.service.RestTaskService;

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

    public RestTaskController(RestTaskService restTaskService, ObjectMapper objectMapper) {
        this.restTaskService = restTaskService;
        this.objectMapper = objectMapper;
    }

    // GET all datasets
    @GetMapping("/get")
    public ResponseEntity<Map<String, Object>> getDatasets() {
        List<Datasets> datasets = restTaskService.getAllDatasets();
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Datasets retrieved successfully");
        response.put("totalRecords", datasets.size());
        response.put("data", datasets);
        return ResponseEntity.ok(response);
    }

    // GET dataset by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Map<String, Object>> getDatasetById(@PathVariable String id) {
        Optional<Datasets> dataset = restTaskService.getDatasetById(id);
        Map<String, Object> response = new HashMap<>();

        if (dataset.isPresent()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Dataset retrieved successfully");
            response.put("data", dataset.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", "Dataset not found for ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // POST create dataset
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createDataset(@RequestBody String json) {
        Map<String, Object> response = new HashMap<>();
        try {
            Datasets tempDataset = objectMapper.readValue(json, Datasets.class);
            Datasets savedDataset = restTaskService.createDataset(tempDataset);
            response.put("status", HttpStatus.CREATED.value());
            response.put("message", "Dataset created successfully");
//            response.put("data", savedDataset);
            response.put("id", savedDataset.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", "Failed to parse and save dataset: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}