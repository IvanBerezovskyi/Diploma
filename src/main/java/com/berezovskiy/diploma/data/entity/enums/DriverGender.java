package com.berezovskiy.diploma.data.entity.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum DriverGender {

    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    private final String title;

    DriverGender(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static DriverGender findByTitle(String title) {
        return Arrays.stream(DriverGender.values())
                .filter(e -> Objects.equals(e.getTitle(), title))
                .findFirst()
                .orElse(OTHER);
    }
}