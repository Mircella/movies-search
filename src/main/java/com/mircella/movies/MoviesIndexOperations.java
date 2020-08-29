package com.mircella.movies;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MoviesIndexOperations {

    private final IndexOperations moviesIndexOperations;

    public MoviesIndexOperations(ElasticsearchOperations elasticsearchOperations) {
        this.moviesIndexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of("movies"));
    }

    public void createMovieIndex() {
        try {
            if (!moviesIndexExists()) {
//        Map<String, Object> mapping = createMovieMapping();
                moviesIndexOperations.create();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMovieIndex() {
        if (moviesIndexExists()) {
            try {
                moviesIndexOperations.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean moviesIndexExists() {
        try {
            return moviesIndexOperations.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Map<String, ?> createMovieMapping() {
        var completionType = Map.of("type", "completion");
        var suggestProperty = Map.of("type", "keyword");
        var suggestProperties = Map.of("suggest", completionType, "title", suggestProperty);
        return Map.of("properties", suggestProperties);
    }

}
