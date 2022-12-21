package com.chocolateminds.elasticsearch.clients;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SearchManager {
    private ElasticsearchClientConfig elasticsearchClientConfig;
    private ElasticsearchClient elasticsearchClient;

    private static final String INDEX_NAME = "flights";
    private static final String FIELD = "route";
    private static final String SEARCH_TEXT = "London New York";

    public SearchManager(){
        elasticsearchClientConfig = new ElasticsearchClientConfig();
        this.elasticsearchClient = elasticsearchClientConfig.getElasticsearchClient();
    }

    public void search(String indexName, String field, String searchText) throws IOException {
        SearchResponse searchResponse =
          this.elasticsearchClient.search(searchRequest -> searchRequest
            .index(indexName)
            .query(queryBuilder ->
                    queryBuilder.match(matchQueryBuilder ->
                            matchQueryBuilder.field(field).query(searchText)))
                ,Flight.class
        );

        // Capture flights
        List<Flight> flights =
                (List<Flight>) searchResponse.hits().hits()
                        .stream().collect(Collectors.toList());

        // Or print them to console
        searchResponse.hits().hits()
                    .stream().forEach(System.out::println);
    }

    public void searchUsingMatchQuery(String indexName, String field, String searchText) throws IOException {

        MatchQuery matchQuery = MatchQuery.of( q -> q
                .field(field)
                .query(searchText)
        );

        SearchResponse searchResponse = this.elasticsearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q.match(matchQuery))
                ,Flight.class
        );

        searchResponse.hits().hits().stream().forEach(
                flightHit -> {
                    System.out.println(flightHit);
                }
        );
    }

    public static void main(String[] args) throws IOException {
        SearchManager searchManager = new SearchManager();
        searchManager.search(INDEX_NAME, FIELD, SEARCH_TEXT);
        searchManager.searchUsingMatchQuery(INDEX_NAME, FIELD, SEARCH_TEXT);
        searchManager.elasticsearchClientConfig.close();
    }
}
