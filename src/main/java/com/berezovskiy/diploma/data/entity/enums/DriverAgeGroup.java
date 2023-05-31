package com.berezovskiy.diploma.data.entity.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum DriverAgeGroup {

    CHILD("Under 18"),
    YOUNG("18-30"),
    MIDDLE_AGED("31-50"),
    SENIOR("Over 51"),
    OTHER("Unknown");

    private final String title;

    DriverAgeGroup(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static DriverAgeGroup findByTitle(String title) {
        return Arrays.stream(DriverAgeGroup.values())
                .filter(e -> Objects.equals(e.getTitle(), title))
                .findFirst()
                .orElse(OTHER);
    }
}
