package com.berezovskiy.diploma.data.entity.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum RoadSurfaceType {

    ASPHALT_ROAD("Asphalt roads"),
    BAD_ASPHALT_ROAD("Asphalt roads with some distress"),
    EARTH_ROAD("Earth roads"),
    GRAVEL_ROAD("Gravel roads"),
    OTHER("Other");

    private final String title;

    RoadSurfaceType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static RoadSurfaceType findByTitle(String title) {
        return Arrays.stream(RoadSurfaceType.values())
                .filter(e -> Objects.equals(e.getTitle(), title))
                .findFirst()
                .orElse(OTHER);
    }
}