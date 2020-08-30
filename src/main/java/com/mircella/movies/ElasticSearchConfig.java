package com.mircella.movies;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
class ElasticSearchConfig extends AbstractReactiveElasticsearchConfiguration {

    private final ElasticSearchSettings settings;

    //    @Override
//    @Bean
//    public RestHighLevelClient elasticsearchClient() {
//        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
//                RestClient.builder(new HttpHost(settings.getHost(), settings.getPort(), settings.getScheme()))
//                        .setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
//                            @Override
//                            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
//                                return requestConfigBuilder.setSocketTimeout(settings.getTimeout());
//                            }
//                        })
//        );
//        return restHighLevelClient;
//    }

    @Bean
    public ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        return mapper;
    }

    @Bean
    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(settings.getHost() + ":" + settings.getPort())
                .withWebClientConfigurer(webClient -> {
                    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                            .codecs(configurer -> configurer.defaultCodecs()
                                    .maxInMemorySize(-1))
                            .build();
                    return webClient.mutate().exchangeStrategies(exchangeStrategies).build();
                })
//                .withProxy("localhost:8888")
                .withConnectTimeout(Duration.ofMillis(settings.getTimeout()))
                .withSocketTimeout(Duration.ofMillis(settings.getTimeout()))
                .build();
//        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo("localhost:9200", "localhost:9291")
//                .withWebClientConfigurer(webClient -> {
//                    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
//                            .codecs(configurer -> configurer.defaultCodecs()
//                                    .maxInMemorySize(-1))
//                            .build();
//                    return webClient.mutate().exchangeStrategies(exchangeStrategies).build();
//                })
//                .build();

        return ReactiveRestClients.create(clientConfiguration);
    }

    @Bean
    public SimpleElasticsearchMappingContext elasticsearchMappingContext() {
        return new SimpleElasticsearchMappingContext();
    }

    @Bean
    public ReactiveElasticsearchOperations reactiveElasticsearchOperations() {
        return new ReactiveElasticsearchTemplate(
                reactiveElasticsearchClient(), elasticsearchEntityMapper(elasticsearchMappingContext())
        );
    }
}
