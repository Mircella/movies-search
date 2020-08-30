package com.mircella.movies;

import com.neovisionaries.i18n.CountryCode;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

record MovieDetails(
        String title,
        int year,
        LocalDate released,
        String runtime,
        Set<Genre> genres,
        Set<String> directors,
        Set<String> actors,
        String plot,
        Set<String> languages,
        CountryCode country,
        Set<Rating> ratings
) {
    public MovieDetails() {
        this(
                null,
                0,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Collections.emptySet()
        );
    }
}
