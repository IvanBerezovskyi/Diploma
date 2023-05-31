package com.berezovskiy.diploma.utils;

import com.berezovskiy.diploma.data.entity.Accident;
import com.berezovskiy.diploma.analysis.Model;
import com.berezovskiy.diploma.data.entity.Prediction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class MappingUtils {

    private MappingUtils() {}

    public static List<Model> accidentsToModelList(List<Accident> accidents) {
        return accidents.stream()
                .map(MappingUtils::accidentToModelList)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static List<Model> accidentToModelList(Accident accident) {
        List<Model> models = new ArrayList<>();
        for (int i = 0; i < accident.getAccidentsNumber(); i++) {
            models.add(new Model(accident));
        }
        return models;
    }

    public static Model predictionToModel(Prediction prediction) {
        return new Model(prediction);
    }
}
