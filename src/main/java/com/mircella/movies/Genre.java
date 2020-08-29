package com.mircella.movies;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum Genre {
    ADVENTURE("Adventure"),
    ACTION("Action"),
    SCI_FI("Sci-Fi"),
    MYSTERY("Mystery"),
    FANTASY("Fantasy"),
    FAMILY("Family"),
    DRAMA("Drama"),
    THRILLER("Thriller"),
    COMEDY("Comedy"),
    ROMANCE("Romance"),
    SPORT("Sport"),
    CRIME("Crime"),
    ANIMATION("Animation"),
    BIOGRAPHY("Biography"),
    DEFAULT("");

    @Getter
    private final String value;

    public static Genre of(String value) {
        return Arrays.stream(Genre.values()).filter(it -> it.value.equals(value)).findAny().orElse(DEFAULT);
    }
}
