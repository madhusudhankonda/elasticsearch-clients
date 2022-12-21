package com.chocolateminds.elasticsearch.clients;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Objects;

public class ElasticsearchClientConfig {
    private RestClient restClient;
    private static ElasticsearchTransport elasticsearchTransport;
    private ElasticsearchClient elasticsearchClient;
    private JacksonJsonpMapper jsonMapper;

    public ElasticsearchClientConfig(){
        init();
    }
    /**
     * Initialization of the client
     */
    public void init(){
        jsonMapper = new JacksonJsonpMapper();

        restClient = RestClient.builder(
                new HttpHost("localhost",9200)).build();

        elasticsearchTransport =
            new RestClientTransport(restClient, jsonMapper);

        elasticsearchClient = new ElasticsearchClient(elasticsearchTransport);
    }

    /**
     * Shutdown the tranport
     * @throws IOException
     */
    public void close() throws IOException {
        System.out.println("shutting down the client..");
        elasticsearchTransport.close();
    }

    /**
     * Get the client
     * @return
     */
    public ElasticsearchClient getElasticsearchClient(){
        return this.elasticsearchClient;
    }
}
