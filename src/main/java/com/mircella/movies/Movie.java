package com.mircella.movies;

import java.time.LocalDate;
import java.util.Set;

record Movie(
        String title,
        int year,
        LocalDate released,
        int runtime,
        Set<Genre> genres

){}
