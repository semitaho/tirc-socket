package fi.toni.tircsocket.client;

import fi.toni.tircsocket.dto.request.IrcLine;
import fi.toni.tircsocket.dto.request.IrcUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by taho on 14/02/16.
 */
public class TircSocketClientLoggerImpl implements TircSocketClient {
  static Logger log = LoggerFactory.getLogger(TircSocketClientLoggerImpl.class);


  private static final String SOCKET_TEXT_URL = "http://ec2-54-77-146-105.eu-west-1.compute.amazonaws.com:8880/backend/say";
  private WebClient webClient;


  @PostConstruct
  public void postConstruct() {
    webClient = WebClient.builder()
        .baseUrl(SOCKET_TEXT_URL)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    log.debug("TEMPLATE created");
  }



  public void sendTopic(String topic){
    log.debug("SEND topic: "+topic);

  }

  public void sendUsers(Map<String, IrcUser> tircUserMap) {
    log.debug("SEND users: "+tircUserMap);
  }

  public void sendNewLine(IrcLine line) {
    log.debug("SEND new line: "+line);
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    HttpEntity<IrcLine> tircLineRequestEntity = new HttpEntity<>(line, requestHeaders);

    /*
    ListenableFuture<ResponseEntity<String>> responseEntityListenableFuture1 = template.postForEntity(SOCKET_TEXT_URL, tircLineRequestEntity, String.class);
    responseEntityListenableFuture1.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
      @Override
      public void onFailure(Throwable throwable) {
        System.out.println("virhe");

      }

      @Override
      public void onSuccess(ResponseEntity<String> stringResponseEntity) {
        System.out.println("OK");

      }
    });
    try {
      responseEntityListenableFuture1.get();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    */
  }
}
