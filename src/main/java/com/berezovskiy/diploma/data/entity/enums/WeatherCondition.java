package com.berezovskiy.diploma.data.entity.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum WeatherCondition {

    NORMAL("Normal"),
    RAINING("Raining"),
    RAINING_AND_WINDY("Raining and Windy"),
    CLOUDY("Cloudy"),
    WINDY("Windy"),
    FOG_OR_MIST("Fog or mist"),
    SNOW("Snow"),
    OTHER("Other");

    private final String title;

    WeatherCondition(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static WeatherCondition findByTitle(String title) {
        return Arrays.stream(WeatherCondition.values())
                .filter(e -> Objects.equals(e.getTitle(), title))
                .findFirst()
                .orElse(OTHER);
    }
}