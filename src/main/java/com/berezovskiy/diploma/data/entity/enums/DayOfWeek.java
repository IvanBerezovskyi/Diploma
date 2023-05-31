package com.berezovskiy.diploma.data.entity.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum DayOfWeek {

    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday"),
    OTHER("Other");

    private final String title;

    DayOfWeek(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static DayOfWeek findByTitle(String title) {
        return Arrays.stream(DayOfWeek.values())
                .filter(e -> Objects.equals(e.getTitle(), title))
                .findFirst()
                .orElse(OTHER);
    }
}