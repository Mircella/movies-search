package com.mircella.movies;

import com.neovisionaries.i18n.CountryCode;
import org.springframework.data.annotation.Id;
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
        LocalDate released,
        int runtime,
        Set<Genre> genres,
        List<String> directors,
        List<String> actors,
        String plot,
        String language,
        CountryCode country,
        @Field(type = FieldType.Nested, store = true)
        List<Rating> ratings
) {
}
