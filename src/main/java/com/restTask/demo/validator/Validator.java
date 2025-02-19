package com.restTask.demo.validator;

import java.util.Optional;

import com.restTask.demo.repo.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.restTask.demo.entity.Datasets;
import com.restTask.demo.entity.Status;

@Component
public class Validator {
	@Autowired
	DataRepository dataRepository;

	public static Optional<String> validate(Datasets dataset) {
		
		if (dataset.getDataSchema() == null || dataset.getDataSchema().isEmpty()
				|| dataset.getDataSchema().values().isEmpty()) {
			return Optional.of("dataSchema is required.");
		}
		
		if (dataset.getRouteConfig() == null || dataset.getRouteConfig().isEmpty()
				|| dataset.getRouteConfig().values().isEmpty()) {
			return Optional.of("Route config key & values are needed.");
		}
		
		if (dataset.getStatus() == null || dataset.getStatus().toString().isBlank()) {
			return Optional.of("Status is required.");
		}
		
		if (dataset.getId() == null || dataset.getId().isBlank()) {
			return Optional.of("Id field is required.");
		}
		
		if (dataset.getCreatedBy() == null) {
			return Optional.of("CreatedBy field required in request body ");
		}
		
		if (dataset.getUpdatedBy() == null) {
			return Optional.of("UpdatedBy fileld required in request body");
		}

		if (dataset.getCreatedBy().matches(".*\\d.*") || dataset.getCreatedBy().isBlank()) {
			return Optional.of("CreatedBy : Give proper name  ");
		}
		
		if (dataset.getUpdatedBy().matches(".*\\d.*") || dataset.getUpdatedBy().isBlank()) {
			return Optional.of("UpdatedBy : Give proper name ");
		}

		
		return Optional.empty();
	}
	
public static Optional<String> validateForupdate(Datasets dataset) {
		
		if (dataset.getDataSchema() == null || dataset.getDataSchema().isEmpty()
				|| dataset.getDataSchema().values().isEmpty()) {
			return Optional.of("dataSchema is required.");
		}
		
		if (dataset.getRouteConfig() == null || dataset.getRouteConfig().isEmpty()
				|| dataset.getRouteConfig().values().isEmpty()) {
			return Optional.of("Route config key & values are needed.");
		}
		
		if (dataset.getStatus() == null || dataset.getStatus().toString().isBlank()) {
			return Optional.of("Status is required.");
		}
		
		if (dataset.getId() == null || dataset.getId().isBlank()) {
			return Optional.of("Id field is required.");
		}
		
		
		
		if (dataset.getUpdatedBy() == null) {
			return Optional.of("UpdatedBy fileld required in request body");
		}

		
		
		if (dataset.getUpdatedBy().matches(".*\\d.*") || dataset.getUpdatedBy().isBlank()) {
			return Optional.of("UpdatedBy : should be name ");
		}
		if(dataset.getCreatedBy()!=null)
		{
			return Optional.of("Created by cannot be changed so remove from request body ");
		}

		
		return Optional.empty();
	}
}
