package com.berezovskiy.diploma.data.repository;

import com.berezovskiy.diploma.data.entity.Accident;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccidentRepository extends JpaRepository<Accident, Long> {

    String FIND_BY_FIELD_TEMPLATE = "select count(id) from accidents where dataset_id = ?1 and accident_severity = ?2 ";

    List<Accident> findByDatasetId(Long datasetId, Pageable pageable);
    List<Accident> findByDatasetId(Long datasetId);

    @Query(value = FIND_BY_FIELD_TEMPLATE + "and vehicle_type = ?3", nativeQuery = true)
    Integer countByVehicleType(long datasetId, int accidentSeverity, int vehicleType);

    @Query(value = FIND_BY_FIELD_TEMPLATE + "and weather_condition = ?3", nativeQuery = true)
    Integer countByWeatherCondition(long datasetId, int accidentSeverity, int weatherCondition);

    @Query(value = FIND_BY_FIELD_TEMPLATE + "and light_condition = ?3", nativeQuery = true)
    Integer countByLightCondition(long datasetId, int accidentSeverity, int lightCondition);

    @Query(value = FIND_BY_FIELD_TEMPLATE + "and road_condition = ?3", nativeQuery = true)
    Integer countByRoadCondition(long datasetId, int accidentSeverity, int roadCondition);

    @Query(value = FIND_BY_FIELD_TEMPLATE + "and collision_type = ?3", nativeQuery = true)
    Integer countByCollisionType(long datasetId, int accidentSeverity, int collisionType);

    @Query(value = FIND_BY_FIELD_TEMPLATE + "and collision_cause = ?3", nativeQuery = true)
    Integer countByCollisionCause(long datasetId, int accidentSeverity, int collisionCause);
}