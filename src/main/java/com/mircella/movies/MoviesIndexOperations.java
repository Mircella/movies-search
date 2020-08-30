package com.mircella.movies;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class MoviesIndexOperations {

    private final IndexOperations moviesIndexOperations;
    private final ReactiveElasticsearchClient reactiveElasticsearchClient;

    public MoviesIndexOperations(ElasticsearchOperations elasticsearchOperations, ReactiveElasticsearchClient reactiveElasticsearchClient) {
        this.reactiveElasticsearchClient = reactiveElasticsearchClient;
        this.moviesIndexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of("movies"));
    }

    public void createMovieIndex() {
        try {
            if (!moviesIndexExists()) {
//        Map<String, Object> mapping = createMovieMapping();
                CreateIndexRequest createIndexRequest = new CreateIndexRequest("movies");
                reactiveElasticsearchClient.indices()
                        .createIndex(createIndexRequest)
                        .doOnSuccess((unused) -> log.info("Index 'movies' was created"))
                        .doOnError(throwable -> log.error("Error while creating 'movies' index", throwable))
                        .block();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMovieIndex() {
        if (moviesIndexExists()) {
            try {
                DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest().indices("movies");
                reactiveElasticsearchClient.indices().deleteIndex(deleteIndexRequest)
                        .doOnSuccess((unused) -> log.info("Index 'movies' was deleted"))
                        .doOnError(throwable -> log.error("Error while deleting 'movies' index", throwable))
                        .block();
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

    public void refresh() {
        RefreshRequest refreshRequest = new RefreshRequest().indices("movies");
        reactiveElasticsearchClient.indices().refreshIndex(refreshRequest)
                .doOnError(throwable -> log.error("Error while refreshing 'movies' index", throwable))
                .block();
    }
}
