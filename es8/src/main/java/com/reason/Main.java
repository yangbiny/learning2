package com.reason;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest.Builder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.SearchTemplateRequest;
import co.elastic.clients.elasticsearch.core.SearchTemplateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.reason.config.ElasticSearchConfig;
import java.io.IOException;
import java.util.ArrayList;
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

  private static final JsonMapper mapper = new JsonMapper();

  public static void main(String[] args) throws IOException {
    ElasticsearchClient client = ElasticSearchConfig.elasticsearchClient();
    //searchWithSearchAfter(client);
    searchWithTemplate(client);
  }

  private static void searchWithSearchAfter(ElasticsearchClient client) throws IOException {

    SortOptions timeCreateAt = SortOptions.of(
        item -> item.field(f -> f.field("time_create_at")
            .order(SortOrder.Desc)
        )
    );
    SortOptions atlasId = SortOptions.of(
        item -> item.field(f -> f.field("atlas_id").order(SortOrder.Desc)));

    Query cntFav = Query.of(item -> item.term(t -> t.field("cnt_fav").value(0)));
    Query createQuery = Query.of(item -> item.range(
        r -> r.field("time_create_at").lt(JsonData.of(System.currentTimeMillis()))));

    String searchAfter = "[1317886800000,264290]";
    Object[] searchAfterObj = mapper.readValue(searchAfter, Object[].class);

    List<FieldValue> searchAfterValue = new ArrayList<>();
    for (Object o : searchAfterObj) {
      searchAfterValue.add(FieldValue.of(Long.parseLong(o.toString())));
    }

    SearchRequest build = new Builder()
        .query(q -> q.bool(bool -> bool.must(cntFav, createQuery)))
        .sort(timeCreateAt, atlasId)
        .size(10)
        .searchAfter(searchAfterValue)
        .build();
    SearchResponse<SearchData> search = client.search(build, SearchData.class);
    HitsMetadata<SearchData> hits = search.hits();
    String after = "";
    List<Hit<SearchData>> resultData = hits.hits();
    if (!resultData.isEmpty()) {
      Hit<SearchData> searchDataHit = resultData.get(resultData.size() - 1);
      List<FieldValue> sort = searchDataHit.sort();
      Object[] objects = new Object[sort.size()];
      for (int i = 0; i < sort.size(); i++) {
        objects[i] = sort.get(i)._get();
      }
      after = mapper.writeValueAsString(objects);
    }
    System.out.println(after);
    System.out.println(hits);
  }

  private static void searchWithTemplate(ElasticsearchClient client) throws IOException {
    var map = new HashMap<String, JsonData>();

    map.put("words", JsonData.of("好看的壁纸"));
    map.put("from", JsonData.of(0));
    map.put("size", JsonData.of(100));
    map.put("value", JsonData.of(10000));
    var searchMap = new HashMap<>();
    searchMap.put("first", 1310206301000L);
    searchMap.put("second", 70289);
    map.put("search_after_tpl", JsonData.of(searchMap));
    SearchTemplateRequest request = new SearchTemplateRequest.Builder()
        .index("sch-atlas-fine-20231120")
        .id("my_search_template")
        .params(map)
        .build();

    SearchTemplateResponse<SearchData> response = client.searchTemplate(
        request, SearchData.class);

    List<Hit<SearchData>> hits = response.hits().hits();
    List<SearchData> data = hits.stream().map(Hit::source).toList();
    Hit<SearchData> searchDataHit = hits.get(hits.size() - 1);
    List<FieldValue> sort = searchDataHit.sort();
    System.out.println(sort);
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