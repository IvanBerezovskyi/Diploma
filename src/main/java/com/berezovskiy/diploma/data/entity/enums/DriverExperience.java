package com.berezovskiy.diploma.data.entity.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum DriverExperience {

    BELOW_YEAR("Below 1yr"),
    ONE_TO_TWO_YEARS("1-2yr"),
    TWO_TO_FIVE_YEARS("2-5yr"),
    FIVE_TO_TEN_YEARS("5-10yr"),
    ABOVE_TEN_YEARS("Above 10yr"),
    NO_LICENSE("No License"),
    OTHER("Other");

    private final String title;

    DriverExperience(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static DriverExperience findByTitle(String title) {
        return Arrays.stream(DriverExperience.values())
                .filter(e -> Objects.equals(e.getTitle(), title))
                .findFirst()
                .orElse(OTHER);
    }
}