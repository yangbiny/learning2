package com.reason;


import com.reason.aspect.AspectBean;
import com.reason.basic.Config;
import com.reason.basic.ConfigTest;
import com.reason.basic.ConfigTest1;
import com.reason.jpa.ImFriend;
import com.reason.jpa.ImFriendRepository;
import com.reason.jpa.JpaConfiguration;
import com.reason.redis.RedisConfig;
import com.reason.redis.RedisStreamTester;
import java.io.IOException;
import java.util.List;
import org.apache.tomcat.util.threads.VirtualThreadExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;

public class Main {

  public static void main(String[] args) throws Exception {

    var context = new AnnotationConfigApplicationContext(Config.class);
    ConfigTest configTest = context.getBean(ConfigTest.class);

    ConfigTest1 configTest1 = configTest.getConfigTest1();

    ConfigTest1 configTest11 = context.getBean("configTest1", ConfigTest1.class);
    ConfigTest1 configTest12 = context.getBean("configTest12", ConfigTest1.class);


    System.out.println(configTest);

    SpringApplication application = new SpringApplication(Main.class);
    WebApplicationType webApplicationType = application.getWebApplicationType();
    System.out.println(webApplicationType);
  }


  private static void redis() throws IOException {
    var context = new AnnotationConfigApplicationContext(RedisConfig.class);
    var tester = context.getBean("redisStreamTester", RedisStreamTester.class);

    VirtualThreadExecutor virtualThreadExecutor = new VirtualThreadExecutor("test");
    virtualThreadExecutor.execute(() -> {
      while (true) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        RecordId recordId = tester.addToStream();
        System.out.println("recordId = " + recordId);
      }

    });

    virtualThreadExecutor.execute(() -> {
      String record = "";
      while (true) {
        List<MapRecord<String, String, String>> mapRecords = tester.readFromStream(record);
        System.out.println(mapRecords);
        if (!mapRecords.isEmpty()) {
          record = mapRecords.getLast().getId().getValue();
        }
      }
    });

    System.in.read();
  }

  private static void jpa() {
    ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(
        JpaConfiguration.class
    );
    ImFriendRepository bean = context.getBean(ImFriendRepository.class);
    List<ImFriend> all = bean.findAll();
    System.out.println(all);
  }

  private static void extracted() {
    ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(
        ConfigConfiguration.class
    );

    AspectBean aspectBean = context.getBean("aspectBean", AspectBean.class);
    aspectBean.test();
    System.out.println();

    ConfigTest configTest = context.getBean("configTest", ConfigTest.class);

    ConfigTest1 configTest1 = context.getBean("configTest1", ConfigTest1.class);

    ConfigTest configTest2 = configTest1.getConfigTest();
    System.out.println(configTest1);
  }
}