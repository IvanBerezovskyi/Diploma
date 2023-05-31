package com.berezovskiy.diploma.data.entity.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum RoadAlignment {

    TANGENT_ROAD_WITH_FLAT_TERRAIN("Tangent road with flat terrain"), //Дотична дорога з рівнинною місцевістю
    TANGENT_ROAD_WITH_MOUNTAINOUS_TERRAIN("Tangent road with mountainous terrain"), //Дотична дорога з гірською місцевістю
    TANGENT_ROAD_WITH_MILD_GRADE_FLAT_TERRAIN("Tangent road with mild grade and flat terrain"), // Дотична дорога з помірним ухилом і рівнинною місцевістю
    STEEP_GRADE_DOWNWARD_WITH_MOUNTAINOUS_TERRAIN("Steep grade downward with mountainous terrain"), //Крутий схил вниз з гірською місцевістю
    GENTLE_HORIZONTAL_CURVE("Gentle horizontal curve"), //полога горизонтальна крива
    ESCARPMENT("Escarpment"), //откос
    OTHER("Other");

    private final String title;

    RoadAlignment(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static RoadAlignment findByTitle(String title) {
        return Arrays.stream(RoadAlignment.values())
                .filter(e -> Objects.equals(e.getTitle(), title))
                .findFirst()
                .orElse(OTHER);
    }
}
