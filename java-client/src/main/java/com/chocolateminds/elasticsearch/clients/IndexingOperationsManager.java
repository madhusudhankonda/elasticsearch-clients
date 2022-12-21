package com.chocolateminds.elasticsearch.clients;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * A indexing operations helper client
 *
 */
public class IndexingOperationsManager {

    private ElasticsearchClientConfig elasticsearchClientConfig;
    private ElasticsearchClient elasticsearchClient;

    private static final String INDEX_NAME = "flights";
    public IndexingOperationsManager(){
        elasticsearchClientConfig = new ElasticsearchClientConfig();
        this.elasticsearchClient = elasticsearchClientConfig.getElasticsearchClient();
    }

    /**
     * Method that crates an index using bog-standard ElasticsearchIndicesClient
     *
     * @param indexName
     * @throws IOException
     */
    public void createIndexUsingClient(String indexName) throws IOException {
        ElasticsearchIndicesClient elasticsearchIndicesClient =
                this.elasticsearchClient.indices();

        CreateIndexRequest createIndexRequest =
                new CreateIndexRequest.Builder().index(indexName).build();

        CreateIndexResponse createIndexResponse =
                elasticsearchIndicesClient.create(createIndexRequest);

        System.out.println("Index created successfully using Client"+createIndexResponse);
    }

    /**
     * A method to create an index using Lambda expression
     * Flight the flight that will be indexed
     */
    public void indexDocument(String indexName, Flight flight) throws IOException {
        IndexResponse indexResponse = this.elasticsearchClient.index(
                i -> i.index(indexName)
                .document(flight)
        );
        System.out.println("Document indexed successfully"+indexResponse);
    }

    public void indexDocumentWithJSON(String indexName, String fileName) throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        IndexResponse indexResponse = this.elasticsearchClient.index(
                i -> i.index(indexName)
                        .withJson(inputStream)
        );
        System.out.println("JSON Document indexed successfully"+indexResponse);
    }


    public void close() throws IOException {
        elasticsearchClientConfig.close();
    }

    public static void main( String[] args ) throws IOException {
        IndexingOperationsManager main = new IndexingOperationsManager();
        Flight flight = new Flight("London to New York", "BA123", "British Airways", 5);

        // Indexing a flight object
        main.indexDocument(INDEX_NAME, flight);

        // Indexing a flight defined in a json file
//        main.indexDocumentWithJSON(INDEX_NAME, "flight.json" );
        main.close();
    }
}
