package com.berezovskiy.diploma.data.entity.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum LightCondition {

    DAYLIGHT("Daylight"),
    DARKNESS_WITH_STREET_LIGHTNING("Darkness - lights lit"),
    DARKNESS_WITHOUT_STREET_LIGHTNING("Darkness - no lighting"),
    OTHER("Other");

    private final String title;

    LightCondition(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static LightCondition findByTitle(String title) {
        return Arrays.stream(LightCondition.values())
                .filter(e -> Objects.equals(e.getTitle(), title))
                .findFirst()
                .orElse(OTHER);
    }
}