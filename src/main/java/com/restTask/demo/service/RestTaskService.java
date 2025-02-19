package com.restTask.demo.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.restTask.demo.entity.Datasets;
import com.restTask.demo.repo.DataRepository;
import com.restTask.demo.validator.Validator;

@Service
public class RestTaskService {

    private final DataRepository dataRepository;
    private final ObjectMapper objectMapper;

    public RestTaskService(DataRepository dataRepository, ObjectMapper objectMapper) {
        this.dataRepository = dataRepository;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<Map<String, Object>> getAllDatasets() {
    	Map<String, Object> response = new HashMap<>();
        try
        {
    	List<Datasets> datasets = dataRepository.findAll();
        
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Datasets retrieved successfully");
        response.put("totalRecords", datasets.size());
        response.put("data", datasets);
        return ResponseEntity.ok(response);
        }
        catch (org.springframework.dao.DataAccessResourceFailureException e) {
            response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
            response.put("message", "Database is currently unavailable. Please try again later.");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        } 
    }

    public ResponseEntity<Map<String, Object>> getDatasetById(String id) {
      Map<String, Object> response = new HashMap<>();  
      try {
      Optional<Datasets> dataset = dataRepository.findById(id);
        
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
      }catch (org.springframework.dao.DataAccessResourceFailureException e) {
          response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
          response.put("message", "Database is currently unavailable. Please try again later.");
          return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
      } 
    }

    public ResponseEntity<Map<String, Object>> createDataset(String json) {
        Map<String, Object> response = new HashMap<>();
        try {
            Datasets tempDataset = objectMapper.readValue(json, Datasets.class);
            Optional<String> validationError = Validator.validate(tempDataset);
            if (validationError.isPresent()) {
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("message", validationError.get());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (dataRepository.findById(tempDataset.getId()).isPresent()) {
                response.put("status", HttpStatus.CONFLICT.value());
                response.put("message", "Dataset with the same ID already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            tempDataset.setUpdatedBy("admin");
            tempDataset.setCreatedAt(LocalDateTime.now());
            tempDataset.setUpdatedAt(LocalDateTime.now());
            Datasets savedDataset = dataRepository.save(tempDataset);
            response.put("status", HttpStatus.CREATED.value());
            response.put("message", "Dataset created successfully");
            response.put("id", savedDataset.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (InvalidFormatException e) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", "Status should be Live or Draft or RETIRED");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (org.springframework.dao.DataAccessResourceFailureException e) {
            response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
            response.put("message", "Database is currently unavailable. Please try again later.");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", "Check request body");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> updateDataset(String id, String json) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Datasets> existingDatasetOpt = dataRepository.findById(id);

            if (existingDatasetOpt.isEmpty()) {
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("message", "Dataset not found for ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Datasets existingDataset = existingDatasetOpt.get();
            Datasets updates = objectMapper.readValue(json, Datasets.class);
            Optional<String> validationError = Validator.validateForupdate(updates);
            if (validationError.isPresent()) {
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("message", validationError.get());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

          
            if (updates.getDataSchema() != null) {
                existingDataset.setDataSchema(updates.getDataSchema());
            }
            if (updates.getRouteConfig() != null) {
                existingDataset.setRouteConfig(updates.getRouteConfig());
            }
            if (updates.getStatus() != null) {
                existingDataset.setStatus(updates.getStatus());
            }
            if (updates.getUpdatedBy() != null) {
                existingDataset.setUpdatedBy(updates.getUpdatedBy());
            }

            existingDataset.setUpdatedAt(LocalDateTime.now()); 
            dataRepository.save(existingDataset);
            
            response.put("id", id);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Dataset updated successfully");
//            response.put("data", existingDataset);

            return ResponseEntity.ok(response);

        } catch (org.springframework.dao.DataAccessResourceFailureException e) {
            response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
            response.put("message", "Database is currently unavailable. Please try again later.");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }catch (Exception e) {
        	response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", "Check request body");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    public ResponseEntity<Map<String,Object>> delete(String id)
    {
    	Map<String,Object> response = new HashMap<>();
    	
    	if (dataRepository.findById(id).isPresent()) {
    	dataRepository.deleteById(id);
    	response.put("id", id);
    	response.put("status ", HttpStatus.OK.value());
    	response.put("message", "Dataset with Id "+id+" deleted successfully");
    	return ResponseEntity.ok(response);
    	}
    	else
    	{
    	response.put("id", id);
    	response.put("status ", HttpStatus.NOT_FOUND.value());
    	response.put("message", "Dataset with Id "+id+" not found ");
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    	}
    }
}
