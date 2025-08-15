package fi.toni.tircsocket.client;

import com.google.cloud.firestore.Firestore;
import fi.toni.tircsocket.dto.request.IrcLine;
import fi.toni.tircsocket.dto.request.IrcUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

@Component

public class TircSocketClientDbImpl implements TircSocketClient {

  private final Logger log = LoggerFactory.getLogger(TircSocketClientDbImpl.class);

  private final Firestore db;

  private final ExecutorService service;

  public TircSocketClientDbImpl(final Firestore db) {
    this.db = db;
    try (var executor = newVirtualThreadPerTaskExecutor()) {
      this.service = executor;
    }
  }

  @Override
  public void sendTopic(final String topic) {

  }

  @Override
  public void sendUsers(final Map<String, IrcUser> tircUserMap) {

  }

  @Override
  public void sendNewLine(final IrcLine line) {
    log.info("send this line: {}", line);
    final var future = db.collection("messages")
            .document(String.valueOf(line.getTime()))
            .set(line);

    try {
      final var result = future.get();
      log.info("write success. {}", result.getUpdateTime());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }


  }
}
