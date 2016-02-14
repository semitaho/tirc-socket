package fi.toni.tircsocket.client;

import fi.toni.tircsocket.dto.IrcLine;
import fi.toni.tircsocket.dto.IrcUser;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by taho on 15/02/16.
 */

@Component
public class TircSocketClientRestImpl implements TircSocketClient {

  static Logger log = Logger.getLogger(TircSocketClientRestImpl.class);
  private static final String SOCKET_TEXT_URL = "http://ec2-54-77-146-105.eu-west-1.compute.amazonaws.com:8880/backend/say";


  @Override
  public void sendTopic(String topic) {

  }

  @Override
  public void sendUsers(Map<String, IrcUser> tircUserMap) {

  }

  @Override
  public void sendNewLine(final IrcLine line) {
    log.debug("SEND new line: " + line);
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    HttpEntity<IrcLine> tircLineRequestEntity = new HttpEntity<>(line, requestHeaders);
    AsyncRestTemplate template = new AsyncRestTemplate();
    ListenableFuture<ResponseEntity<String>> responseEntityListenableFuture1 = template.postForEntity(SOCKET_TEXT_URL, tircLineRequestEntity, String.class);
    responseEntityListenableFuture1.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
      @Override
      public void onFailure(Throwable throwable) {
        log.error("virhe with request: "+line, throwable);

      }

      @Override
      public void onSuccess(ResponseEntity<String> stringResponseEntity) {
        log.debug("OK");

      }
    });

  }
}
