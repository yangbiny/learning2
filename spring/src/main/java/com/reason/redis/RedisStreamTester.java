package com.reason.redis;

import jakarta.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Component;

/**
 * @author impassive
 */
@Component
public class RedisStreamTester implements InitializingBean {

  @Resource
  private RedisTemplate<String, String> redisTemplate;

  private StreamOperations<String, String, String> redisStreamTemplate;


  public RecordId addToStream() {
    long l = System.currentTimeMillis();
    HashMap<String, String> content = new HashMap<>();
    content.put("id-%s".formatted(l), "value-%s".formatted(l));
    return redisStreamTemplate.add("stream", content);
  }

  public List<MapRecord<String, String, String>> readFromStream(String recordId) {
    StreamReadOptions block = StreamReadOptions.empty().block(Duration.ofSeconds(0));
    StreamOffset<String> stream = StreamOffset.fromStart("stream");
    if (recordId != null && !recordId.isEmpty()) {
      stream = StreamOffset.create("stream", ReadOffset.from(recordId));
    }
    return redisStreamTemplate.read(block, stream);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    redisStreamTemplate = redisTemplate.opsForStream();
  }
}
