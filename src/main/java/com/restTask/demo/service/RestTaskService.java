package com.restTask.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


			dataset.setCreatedAt(LocalDateTime.now());
			dataset.setUpdatedAt(LocalDateTime.now());
			// Save the dataset ( it can insert new record or update existing record )
			return dataRepository.save(dataset);

		} catch (Exception e) {
			throw new RuntimeException("Failed to create dataset with system info", e);
		}
	}
}