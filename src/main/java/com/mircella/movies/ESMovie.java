package com.mircella.movies;

import com.neovisionaries.i18n.CountryCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Document(indexName = "movies")
record ESMovie(
        @Id UUID id,
        String title,
        int year,
        @Field(format = DateFormat.date, type = FieldType.Date)
        LocalDate released,
        int runtime,
        Set<Genre> genres,
        Set<String> directors,
        Set<String> actors,
        String plot,
        Set<String> languages,
        CountryCode country,
        @Field(type = FieldType.Nested, store = true)
        Set<Rating> ratings
) {
}
