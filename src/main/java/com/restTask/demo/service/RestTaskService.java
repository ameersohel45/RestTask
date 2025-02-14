package com.restTask.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restTask.demo.entity.Datasets;
import com.restTask.demo.repo.DataRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RestTaskService {

    private final DataRepository dataRepository;

    @Autowired
    public RestTaskService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    // Get all datasets
    public List<Datasets> getAllDatasets() {
        return dataRepository.findAll();
    }

    // Get dataset by ID
    public Optional<Datasets> getDatasetById(String id) {
        return dataRepository.findById(id);
    }

    // Create dataset with system info
    public Datasets createDatasetWithSystemInfo(Datasets dataset) {
        try {
            // Auto-generate ID if not provided
            if (dataset.getId() == null || dataset.getId().isBlank()) {
                dataset.setId("DS-" + UUID.randomUUID().toString()); // Auto-generate ID
            }
            
            if (dataset.getStatus() == null) {
                throw new IllegalArgumentException("status is required");
            }

//            // Set dataSchema if not provided
//            if (dataset.getDataSchema() == null) {
//                Map<String, Object> dataSchema = new HashMap<>();
//                dataSchema.put("$schema", "http://json-schema.org/draft-07/schema#");
//                dataSchema.put("type", "object");
//                dataSchema.put("properties", Map.of(
//                    "id", Map.of("type", "string", "description", "Unique identifier for the record"),
//                    "name", Map.of("type", "string", "description", "Name of the entity"),
//                    "created_at", Map.of("type", "string", "format", "date-time", "description", "Timestamp of creation")
//                ));
//                dataSchema.put("required", List.of("id", "name", "created_at"));
//                dataset.setDataSchema(dataSchema);
//            }
            if (dataset.getDataSchema() == null) {
                throw new IllegalArgumentException("dataSchema is required");
            }

            // Set routeConfig if not provided
            if (dataset.getRouteConfig() == null) {
                Map<String, Object> routeConfig = new HashMap<>();
                routeConfig.put("enabled", true); // Default to true
                dataset.setRouteConfig(routeConfig);
            }

            // Set system-generated fields
            if (dataset.getCreatedBy() == null || dataset.getCreatedBy().isBlank()) {
               
            dataset.setCreatedBy(System.getProperty("user.name"));
            }
            if (dataset.getUpdatedBy() == null || dataset.getUpdatedBy().isBlank()) {
            dataset.setUpdatedBy(System.getProperty("user.name"));
            }
            dataset.setUpdatedAt(LocalDateTime.now());

            // Save the dataset
            return dataRepository.save(dataset);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create dataset with system info", e);
        }
    }
}