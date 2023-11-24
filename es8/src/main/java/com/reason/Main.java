package com.reason;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest.Builder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.SearchTemplateRequest;
import co.elastic.clients.elasticsearch.core.SearchTemplateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reason.config.ElasticSearchConfig;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import lombok.Data;

public class Main {

  public static void main(String[] args) throws IOException {
    ElasticsearchClient client = ElasticSearchConfig.elasticsearchClient();
    var map = new HashMap<String, JsonData>();

    map.put("words", JsonData.of("傻瓜"));
    map.put("from", JsonData.of(1));
    map.put("size", JsonData.of(100));
    SearchTemplateRequest request = new SearchTemplateRequest.Builder()
        .id("my_search_template")
        .params(map)
        .build();

    SearchTemplateResponse<SearchData> response = client.searchTemplate(
        request, SearchData.class);

    List<Hit<SearchData>> hits = response.hits().hits();
    List<SearchData> data = hits.stream().map(Hit::source).toList();
    System.out.println(data);
  }
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(SnakeCaseStrategy.class)
class SearchData {

  private Long atlasId;
  private String msg;

  private List<String> segWords;
}