package com.berezovskiy.diploma.data.entity.enums;

import com.berezovskiy.diploma.data.entity.exception.NoTargetVariableProvidedException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum AccidentSeverity {
    SLIGHT("Slight Injury"),
    SERIOUS("Serious Injury"),
    FATAL("Fatal Injury");

    private final String title;

    AccidentSeverity(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static AccidentSeverity findByTitle(String title) {
        return Arrays.stream(AccidentSeverity.values())
                .filter(e -> Objects.equals(e.getTitle(), title))
                .findFirst()
                .orElseThrow(NoTargetVariableProvidedException::new);
    }
}