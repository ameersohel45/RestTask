package com.restTask.demo.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.restTask.demo.entity.Datasets;
import com.restTask.demo.entity.Status;

@Component
public class Validator {

    public static Optional<String> validate(Datasets dataset) {
        if (dataset.getDataSchema() == null || dataset.getDataSchema().isEmpty()|| dataset.getDataSchema().values().isEmpty()) {
            return Optional.of("dataSchema is required.");
        }
        if (dataset.getRouteConfig() == null || dataset.getRouteConfig().isEmpty() || dataset.getRouteConfig().values().isEmpty()){
            return Optional.of("Route config key & values are needed.");
        }
        if (dataset.getStatus() == null || dataset.getStatus().toString().isBlank()) {
            return Optional.of("Status is required.");
        }
        if (dataset.getId() == null || dataset.getId().isBlank()) {
            return Optional.of("Id field is required.");
        }
        
        return Optional.empty();
    }
}
