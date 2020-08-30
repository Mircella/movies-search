package com.mircella.movies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MoviesRepository {

    private final ObjectMapper objectMapper;

    public List<Movie> loadMovies() {
       var resource =  new ClassPathResource("movies.json", this.getClass().getClassLoader());
        try (var inputStream = new InputStreamReader(resource.getInputStream())) {
            var movies = objectMapper.readValue(inputStream, Movie[].class);
            return Arrays.asList(movies);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


//    fun createFavoriteMovies(genres: String): List<Movie> {
//        val MOVIES = MOVIES_DTOS.map {
//            val favGenre = it.genres?.split(", ")?.toTypedArray()
//            val suggest = Completion(favGenre)
//            if (favGenre?.contains(genres) ?: false) {
//                suggest.weight = 2
//            } else {
//                suggest.weight = 1
//            }
//            Movie(UUID.randomUUID(),
//                    it.title,
//                    it.year?.toInt(),
//                    LocalDate.parse(it.released, DATE_FORMATTER),
//                    it.runtime,
//                    it.genres,
//                    suggest,
//                    it.directors,
//                    it.actors,
//                    it.plot,
//                    it.languages,
//                    it.country,
//                    it.ratings?.map { Ratings(it.source, it.value) })
//        }
//        return MOVIES
//    }
}
