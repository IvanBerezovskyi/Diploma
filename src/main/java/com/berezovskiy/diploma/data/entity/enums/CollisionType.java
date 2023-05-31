package com.berezovskiy.diploma.data.entity.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum CollisionType {

    COLLISION_WITH_VEHICLE("Vehicle with vehicle collision"),
    COLLISION_WITH_PARKED_VEHICLE("Collision with roadside-parked vehicles"),
    COLLISION_WITH_PEDESTRIAN("Collision with pedestrians"),
    COLLISION_WITH_OBJECTS("Collision with roadside objects"),
    COLLISION_WITH_ANIMALS("Collision with animals"),
    FALL_FROM_VEHICLE("Fall from vehicles"),
    ROLLOVER("Rollover"),
    OTHER("Other");

    private final String title;

    CollisionType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static CollisionType findByTitle(String title) {
        return Arrays.stream(CollisionType.values())
                .filter(e -> Objects.equals(e.getTitle(), title))
                .findFirst()
                .orElse(OTHER);
    }
}