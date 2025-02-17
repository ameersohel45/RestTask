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

   
    public List<Datasets> getAllDatasets() {
        return dataRepository.findAll();
    }

   
    public Optional<Datasets> getDatasetById(String id) {
        return dataRepository.findById(id);
    }

    // Here i am inserting record 
    public Datasets createDataset(Datasets dataset) {
        try {
            
        	// Id is auto generated here 
            if (dataset.getId() == null || dataset.getId().isBlank()) {
                dataset.setId("RestTask-" + UUID.randomUUID().toString()); 
            }
            
            if (dataset.getStatus() == null) {
                throw new IllegalArgumentException("status is required");
            }


            if (dataset.getDataSchema() == null) {
                throw new IllegalArgumentException("dataSchema is required");
            }

            // Set routeConfig if not provided in postman req body ...
            if (dataset.getRouteConfig() == null) {
                Map<String, Object> routeConfig = new HashMap<>();
                routeConfig.put("enabled", true);  
                dataset.setRouteConfig(routeConfig);
            }

           
            if (dataset.getCreatedBy() == null || dataset.getCreatedBy().isBlank()) {
            	dataset.setCreatedBy(System.getProperty("user.name"));
            }
            
            if (dataset.getUpdatedBy() == null || dataset.getUpdatedBy().isBlank()) {
            	dataset.setUpdatedBy(System.getProperty("user.name"));
            }
            dataset.setUpdatedAt(LocalDateTime.now());

            // Save the dataset ( it can insert new record or update existing record )
            return dataRepository.save(dataset);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create dataset with system info", e);
        }
    }
}