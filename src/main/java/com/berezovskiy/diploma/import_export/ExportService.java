package com.berezovskiy.diploma.import_export;

import com.berezovskiy.diploma.data.entity.Prediction;
import com.berezovskiy.diploma.data.entity.enums.*;
import com.berezovskiy.diploma.data.repository.PredictionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExportService {

    private PredictionRepository predictionRepository;

    public String export(Long datasetId) {
        List<Prediction> predictions = predictionRepository.findByDatasetId(datasetId);
        return predictions.stream()
                .map(Prediction::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public String exportAccidentImportRules() {
        var lines = List.of("Набір даних повинен мати 18 атрибутів, з яких 2 цільові змінні",
                "Записувати значення для однієї аварії необхідно в строку, через кому",
                "Нижче наведені змінні та можливі значення",
                "accidentHour: числове поле, від 0 до 23",
                createStringFromEnum("dayOfWeek", DayOfWeek.values()),
                createStringFromEnum("driverAgeGroup", DriverAgeGroup.values()),
                createStringFromEnum("driverGender", DriverGender.values()),
                createStringFromEnum("driverExperience", DriverExperience.values()),
                createStringFromEnum("vehicleType", VehicleType.values()),
                "vehicleDefected: строка, значеннями є 'no defect', 'defected'",
                createStringFromEnum("roadAlignment", RoadAlignment.values()),
                createStringFromEnum("roadSurfaceType", RoadSurfaceType.values()),
                createStringFromEnum("roadSurfaceCondition", RoadSurfaceCondition.values()),
                createStringFromEnum("lightCondition", LightCondition.values()),
                createStringFromEnum("weatherCondition", WeatherCondition.values()),
                createStringFromEnum("collisionType", CollisionType.values()),
                "vehiclesNumber: числове поле, від 1 до 10",
                "casualtiesNumber: числове поле, від 0 до 10",
                createStringFromEnum("vehicleMovement", VehicleMovement.values()),
                createStringFromEnum("collisionCause", CollisionCause.values()),
                createStringFromEnum("accidentSeverity", AccidentSeverity.values()));
        return lines.stream()
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String createStringFromEnum(String name, Enum [] array) {
        String enumString = Arrays.stream(array)
                .map(Enum::toString)
                .collect(Collectors.joining(","));
        return name + ": " + enumString;
    }
}
