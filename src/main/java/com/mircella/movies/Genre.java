package com.mircella.movies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
enum Genre {
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
    BIOGRAPHY("Biography");

    @Getter
    private final String value;
}
