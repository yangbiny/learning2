package com.reason.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

/**
 * @author impassive
 */
public class ElasticSearchConfig {

  public static ElasticsearchClient elasticsearchClient() {
    RestClient restClient = RestClient.builder(HttpHost.create("localhost:9200"))
        .build();
    RestClientTransport transport = new RestClientTransport(
        restClient,
        new JacksonJsonpMapper()
    );
    return new ElasticsearchClient(transport);
  }

}
