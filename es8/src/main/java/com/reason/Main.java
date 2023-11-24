package com.reason;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchTemplateRequest;
import co.elastic.clients.elasticsearch.core.SearchTemplateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reason.config.ElasticSearchConfig;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import lombok.Data;

/*

// 使用搜索模板进行查询
GET sch-atlas-fine-20231120/_search/template
{
  "id": "my_search_template",
  "params": {
    "words":"壁纸",
    "from":0,
    "size":10,
    "value":1000000000
  }
}

// 渲染搜索模板（不执行查询）
POST _render/template
{
  "id": "my_search_template",
  "params": {
    "words":"壁纸",
    "from":1,
    "size":1,
    "value":100000
  }
}

// 创建 搜索 模板
PUT _scripts/my_search_template
{
  "script": {
    "lang": "mustache",
    "source": {
      "query": {
        "script_score": {
          "script": {
            "source": "_score + doc['time_latest_fav_at'].value / {{value}}"
          },
          "query": {
            "bool": {
              "must": [
                {
                  "match": {
                    "msg": "{{words}}"
                  }
                }
              ]
            }
          }
        }
      },
      "from": "{{from}}",
      "size": "{{size}}"
    }
  }
}



 */


public class Main {

  public static void main(String[] args) throws IOException {
    ElasticsearchClient client = ElasticSearchConfig.elasticsearchClient();
    var map = new HashMap<String, JsonData>();

    map.put("words", JsonData.of("好看的壁纸"));
    map.put("from", JsonData.of(1));
    map.put("size", JsonData.of(100));
    map.put("value", JsonData.of(10000));
    SearchTemplateRequest request = new SearchTemplateRequest.Builder()
        .index("sch-atlas-fine-20231120")
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