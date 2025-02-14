package com.restTask.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restTask.demo.entity.Datasets;

@Repository
public interface DataRepository extends JpaRepository<Datasets, String> {
}