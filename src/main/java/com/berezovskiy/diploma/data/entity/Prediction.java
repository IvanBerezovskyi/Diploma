package com.berezovskiy.diploma.data.entity;

import com.berezovskiy.diploma.data.entity.enums.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "predictions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int accidentHour;
    @Enumerated
    private DayOfWeek dayOfWeek;
    @Enumerated
    private DriverAgeGroup driverAgeGroup;
    @Enumerated
    private DriverGender driverGender;
    @Enumerated
    private DriverExperience driverExperience;
    @Enumerated
    private VehicleType vehicleType;

    private boolean vehicleDefected;

    @Enumerated
    private RoadAlignment roadAlignment;
    @Enumerated
    private RoadSurfaceType roadSurfaceType;
    @Enumerated
    private RoadSurfaceCondition roadSurfaceCondition;

    @Enumerated
    private LightCondition lightCondition;
    @Enumerated
    private WeatherCondition weatherCondition;

    @Enumerated
    private CollisionType collisionType;

    private Integer vehiclesNumber;
    private Integer casualtiesNumber;

    @Enumerated
    private VehicleMovement vehicleMovement;

    @Enumerated
    private CollisionCause collisionCause;
    @Enumerated
    private AccidentSeverity accidentSeverity;

    @ManyToOne
    @JoinColumn(name = "dataset_id")
    private Dataset dataset;

    @Override
    public String toString() {
        return String.join(",",
                String.valueOf(accidentHour), dayOfWeek.toString(), driverAgeGroup.toString(), driverGender.toString(),
                driverExperience.toString(), vehicleType.toString(), String.valueOf(vehicleDefected),
                roadAlignment.toString(), roadSurfaceType.toString(), roadSurfaceCondition.toString(),
                lightCondition.toString(), weatherCondition.toString(), collisionType.toString(), vehiclesNumber.toString(),
                casualtiesNumber.toString(), vehicleMovement.toString(), collisionCause.toString(),
                accidentSeverity == null ? "" : accidentSeverity.toString()
        );
    }
}