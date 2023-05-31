package com.berezovskiy.diploma.analysis;

import com.berezovskiy.diploma.data.entity.Accident;
import com.berezovskiy.diploma.data.entity.Prediction;
import lombok.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@Builder
public class Model {

    private int accidentHour;
    private int dayOfWeek;
    private int driverAgeGroup;
    private int driverGender;
    private int driverExperience;
    private int vehicleType;
    private int vehicleDefected;
    private int roadAlignment;
    private int roadSurfaceType;
    private int roadSurfaceCondition;
    private int lightCondition;
    private int weatherCondition;
    private int collisionType;
    private int vehiclesNumber;
    private int casualtiesNumber;
    private int vehicleMovement;
    private int collisionCause;
    private int accidentSeverity;

    public Model(Accident accident) {
        accidentHour = accident.getAccidentHour();
        dayOfWeek = accident.getDayOfWeek().ordinal();
        driverAgeGroup = accident.getDriverAgeGroup().ordinal();
        driverGender = accident.getDriverGender().ordinal();
        driverExperience = accident.getDriverExperience().ordinal();
        vehicleType = accident.getVehiclesNumber();
        vehicleDefected = accident.isVehicleDefected() ? 1 : 0;
        roadAlignment = accident.getRoadAlignment().ordinal();
        roadSurfaceType = accident.getRoadSurfaceType().ordinal();
        roadSurfaceCondition = accident.getRoadSurfaceCondition().ordinal();
        lightCondition = accident.getLightCondition().ordinal();
        weatherCondition = accident.getWeatherCondition().ordinal();
        collisionType = accident.getCollisionType().ordinal();
        vehiclesNumber = accident.getVehiclesNumber();
        casualtiesNumber = accident.getCasualtiesNumber();
        vehicleMovement = accident.getVehicleMovement().ordinal();
        collisionCause = accident.getCollisionCause().ordinal();
        accidentSeverity = accident.getAccidentSeverity().ordinal();
    }

    public Model(Prediction prediction) {
        accidentHour = prediction.getAccidentHour();
        dayOfWeek = prediction.getDayOfWeek().ordinal();
        driverAgeGroup = prediction.getDriverAgeGroup().ordinal();
        driverGender = prediction.getDriverGender().ordinal();
        driverExperience = prediction.getDriverExperience().ordinal();
        vehicleType = prediction.getVehiclesNumber();
        vehicleDefected = prediction.isVehicleDefected() ? 1 : 0;
        roadAlignment = prediction.getRoadAlignment().ordinal();
        roadSurfaceType = prediction.getRoadSurfaceType().ordinal();
        roadSurfaceCondition = prediction.getRoadSurfaceCondition().ordinal();
        lightCondition = prediction.getLightCondition().ordinal();
        weatherCondition = prediction.getWeatherCondition().ordinal();
        collisionType = prediction.getCollisionType().ordinal();
        vehiclesNumber = prediction.getVehiclesNumber();
        vehicleMovement = prediction.getVehicleMovement().ordinal();
        collisionCause = prediction.getCollisionCause().ordinal();
    }

    @Override
    public String toString() {
        return Stream.of(accidentHour, dayOfWeek, driverAgeGroup, driverGender, driverExperience,
                        vehicleType, vehicleDefected, roadAlignment, roadSurfaceType, roadSurfaceCondition,
                        lightCondition, weatherCondition, collisionType, vehiclesNumber, casualtiesNumber,
                        vehicleMovement, collisionCause, accidentSeverity)
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    public String toStringWithoutAccidentSeverity() {
        return Stream.of(accidentHour, dayOfWeek, driverAgeGroup, driverGender, driverExperience,
                        vehicleType, vehicleDefected, roadAlignment, roadSurfaceType, roadSurfaceCondition,
                        lightCondition, weatherCondition, collisionType, vehiclesNumber, casualtiesNumber,
                        vehicleMovement, collisionCause)
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }
}