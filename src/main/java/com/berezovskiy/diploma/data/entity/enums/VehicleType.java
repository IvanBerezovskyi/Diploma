package com.berezovskiy.diploma.data.entity.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum VehicleType {

    AUTOMOBILE("Automobile"),
    LORRY("Lorry"),
    LONG_LORRY("Long lorry"),
    BUS("Bus"),
    MINIBUS("Minibus"),
    MOTORCYCLE("Motorcycle"),
    RIDDEN_HORSE("Ridden horse"),
    OTHER("Other");

    private final String title;

    VehicleType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static VehicleType findByTitle(String title) {
        return Arrays.stream(VehicleType.values())
                .filter(e -> Objects.equals(e.getTitle(), title))
                .findFirst()
                .orElse(OTHER);
    }
}