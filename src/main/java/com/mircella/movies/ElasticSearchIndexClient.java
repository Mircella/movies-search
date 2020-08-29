package com.mircella.movies;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ElasticSearchIndexClient {

    private final RestHighLevelClient restHighLevelClient;

    public void createMovieIndex() {
        var request = new CreateIndexRequest("movies");
//        Map<String, Object> mapping = createMovieMapping();
//        request.mapping(mapping);
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 1));
        try {
            restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMovieIndex() {
        var request = new DeleteIndexRequest("movies");
        try {
            restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean indexExists() {
        try {
            GetIndexRequest request = new GetIndexRequest("movies");
            request.local(false);
            request.humanReadable(true);
            request.includeDefaults(true);

            return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
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
