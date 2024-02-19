package com.gb;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import lombok.Data;
import com.gb.timer.Timer;
import org.slf4j.event.Level;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BookProvider {

  // HttpClient - java.net
  // RestTemplate - spring.web
  // WebClient - spring.reactive

  private final WebClient webClient;
  private final EurekaClient eurekaClient;

//  public BookProvider(ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction) {
//    webClient = WebClient.builder()
//      .filter(loadBalancerExchangeFilterFunction)
//      .build();
////    this.eurekaClient = eurekaClient;
//  }

  public BookProvider(EurekaClient eurekaClient) {
    webClient = WebClient.builder().build();
//            .filter(loadBalancerExchangeFilterFunction)
//            .build();
    this.eurekaClient = eurekaClient;
  }

  @Timer(level = Level.WARN)
  public UUID getRandomBookId() {
    BookResponse randomBook = webClient.get()
            // .uri("http://book-service/api/book/random")
            .uri(getBookServiceIp() + "/api/book/random")
            .retrieve()
            .bodyToMono(BookResponse.class)
            .block();

    return randomBook.getId();
  }

  // round robbin

  @Timer(level = Level.WARN)
  private String getBookServiceIp() {
    Application application = eurekaClient.getApplication("BOOK-SERVICE");
    List<InstanceInfo> instances = application.getInstances();

    int randomIndex = ThreadLocalRandom.current().nextInt(instances.size());
    InstanceInfo randomInstance = instances.get(randomIndex);
    return "http://" + randomInstance.getIPAddr() + ":" + randomInstance.getPort();
  }

  @Data
  private static class BookResponse{
    private UUID id;
  }

}
