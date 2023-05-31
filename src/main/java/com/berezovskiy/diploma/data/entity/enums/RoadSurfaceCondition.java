package com.berezovskiy.diploma.data.entity.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum RoadSurfaceCondition {

    DRY("Dry"),
    WET_OR_DAMP("Wet or damp"),
    OTHER("Other");

    private final String title;

    RoadSurfaceCondition(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static RoadSurfaceCondition findByTitle(String title) {
        return Arrays.stream(RoadSurfaceCondition.values())
                .filter(e -> Objects.equals(e.getTitle(), title))
                .findFirst()
                .orElse(OTHER);
    }
}