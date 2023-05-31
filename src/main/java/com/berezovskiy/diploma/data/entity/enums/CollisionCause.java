package com.berezovskiy.diploma.data.entity.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CollisionCause {

    MOVING_BACKWARD("Moving Backward"),
    OVERTAKING("Overtaking"),
    CHANGING_LINE_TO_RIGHT("Changing lane to the right"),
    CHANGING_LINE_TO_LEFT("Changing lane to the left"),
    NO_DISTANCE("No distancing"),
    NO_PRIORITY_TO_VEHICLE("No priority to vehicle"),
    NO_PRIORITY_TO_PEDESTRIAN("No priority to pedestrian"),
    OVERLOADING("Overloading"),
    DRIVING_AT_HIGH_SPEED("Driving at high speed"),
    IMPROPER_PARKING("Improper parking"),
    DRIVING_CARELESSLY("Driving carelessly"),
    DRUNK_DRIVING("Drunk driving"),
    OTHER("Other");

    private final String title;

    CollisionCause(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static CollisionCause findByTitle(String title) {
        return Arrays.stream(CollisionCause.values())
                .filter(e -> e.getTitle().contains(title))
                .findFirst()
                .orElse(OTHER);
    }
}