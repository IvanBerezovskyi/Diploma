package com.berezovskiy.diploma.data.repository;

import com.berezovskiy.diploma.data.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    List<Prediction> findByDatasetId(Long datasetId);
}