package com.berezovskiy.diploma.data.repository;

import com.berezovskiy.diploma.data.entity.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatasetRepository extends JpaRepository<Dataset, Long> {
    List<Dataset> findByUserId(Long userId);
    List<Dataset> findByIsTrainedOnAndUserId(boolean isTrainedOn, Long userId);
}