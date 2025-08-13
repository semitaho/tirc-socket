package fi.toni.tircsocket.client;

import fi.toni.tircsocket.dto.request.IrcLine;
import fi.toni.tircsocket.dto.request.IrcUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by taho on 15/02/16.
 */

@Component
public class TircSocketClientRestImpl implements TircSocketClient {

  static Logger log = LoggerFactory.getLogger(TircSocketClientRestImpl.class);
  static final String DOMAIN = "http://localhost:80";
  private static final String SOCKET_TEXT_URL = DOMAIN + "/irc/say";
  private static final String TOPIC_TEXT_URL = DOMAIN + "/irc/sendtopic";
  private static final String USERS_TEXT_URL = DOMAIN + "/irc/sendusers";

  private WebClient webClient;
  @PostConstruct
  public void postConstruct() {
    webClient = WebClient.builder()
        .baseUrl(DOMAIN)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    log.debug("WebClient created with base URL: " + DOMAIN);

  }


  @Override
  public void sendTopic(String topic) {
    log.debug("SEND new topic: " + topic);
    HttpEntity<String> request = createRequest(topic);
    /*
    AsyncRestTemplate template = new AsyncRestTemplate();
    ListenableFuture<ResponseEntity<String>> responseEntityListenableFuture = template.postForEntity(TOPIC_TEXT_URL, request, String.class);
    responseEntityListenableFuture.addCallback(new TircListenableFutureCallback<ResponseEntity<String>>());
  */
  }



  @Override
  public void sendUsers(Map<String, IrcUser> tircUserMap) {
    log.debug("SET irc users: {}", tircUserMap);

    HttpEntity<Map<String, IrcUser>> request = createRequest(tircUserMap);
    /*
    AsyncRestTemplate template = new AsyncRestTemplate();
    ListenableFuture<ResponseEntity<Map>> responseEntityListenableFuture = template.postForEntity(USERS_TEXT_URL, request, Map.class);
    responseEntityListenableFuture.addCallback(new TircListenableFutureCallback<ResponseEntity<Map>>());
   */
  }


  @Override
  public void sendNewLine(final IrcLine line) {
    log.debug("SEND new line: {} ", line);

    /*
    HttpEntity<IrcLine> tircLineRequestEntity = createRequest(line);
    AsyncRestTemplate template = new AsyncRestTemplate();
    ListenableFuture<ResponseEntity<IrcLine>> responseEntityListenableFuture = template.postForEntity(SOCKET_TEXT_URL, tircLineRequestEntity, IrcLine.class);
    responseEntityListenableFuture.addCallback(new TircListenableFutureCallback<ResponseEntity<IrcLine>>());
     */

  }

  private <T> HttpEntity<T> createRequest(T entity) {
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    HttpEntity<T> httpEntity = new HttpEntity<>(entity, requestHeaders);
    return httpEntity;

  }
}
