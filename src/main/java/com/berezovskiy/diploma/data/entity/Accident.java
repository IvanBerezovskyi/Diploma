package com.berezovskiy.diploma.data.entity;

import com.berezovskiy.diploma.data.entity.enums.*;
import com.berezovskiy.diploma.data.entity.exception.NoTargetVariableProvidedException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accidents")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Accident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //is not included in prediction
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
    private Integer accidentsNumber;

    @ManyToOne
    @JoinColumn(name = "dataset_id")
    private Dataset dataset;

    public Accident(String accidentString) {
        String [] data = accidentString.split(",");
        if (data[14].isBlank()) {
            throw new NoTargetVariableProvidedException();
        }
        accidentHour = data[0].isBlank() ? 0 : Integer.parseInt(data[0].split(":")[0]); //парсит из строки формата 16:00:00
        dayOfWeek = DayOfWeek.findByTitle(data[1]);
        driverAgeGroup = DriverAgeGroup.findByTitle(data[2]);
        driverGender = DriverGender.findByTitle(data[3]);
        driverExperience = DriverExperience.findByTitle(data[4]);
        vehicleType = VehicleType.findByTitle(data[5]);
        vehicleDefected = !data[6].toLowerCase().contains("no defect");
        roadAlignment = RoadAlignment.findByTitle(data[7]);
        roadSurfaceType = RoadSurfaceType.findByTitle(data[8]);
        roadSurfaceCondition = RoadSurfaceCondition.findByTitle(data[9]);
        lightCondition = LightCondition.findByTitle(data[10]);
        weatherCondition = WeatherCondition.findByTitle(data[11]);
        collisionType = CollisionType.findByTitle(data[12]);
        vehiclesNumber = data[13].isBlank() ? 1 : Integer.parseInt(data[13]);
        casualtiesNumber = Integer.parseInt(data[14]);
        vehicleMovement = VehicleMovement.findByTitle(data[15]);
        collisionCause = CollisionCause.findByTitle(data[16]);
        accidentSeverity = AccidentSeverity.findByTitle(data[17]);
        accidentsNumber = 1;
    }
}