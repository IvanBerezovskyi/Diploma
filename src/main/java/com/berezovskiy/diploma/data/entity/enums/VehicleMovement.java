package com.berezovskiy.diploma.data.entity.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum VehicleMovement {

    GOING_STRAIGHT("Going straight"),
    MOVING_BACKWARD("Moving Backward"),
    U_TURN("U-Turn"), //разворот
    TURNOVER("Turnover"), //перекидання
    WAITING("Waiting to go"),
    REVERSING("Reversing"),
    STOPPING("Stopping"),
    OVERTAKING("Overtaking"),
    OTHER("Other");

    private final String title;

    VehicleMovement(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static VehicleMovement findByTitle(String title) {
        return Arrays.stream(VehicleMovement.values())
                .filter(e -> Objects.equals(e.getTitle(), title))
                .findFirst()
                .orElse(OTHER);
    }

}