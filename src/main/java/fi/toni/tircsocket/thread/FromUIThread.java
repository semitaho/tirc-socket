package fi.toni.tircsocket.thread;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.QuerySnapshot;
import fi.toni.tircsocket.dto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static java.util.Objects.nonNull;

@Component
public class FromUIThread {

  private final String TIRC_DATA_SOURCE = "TIRC";

  private static final Logger logger = LoggerFactory.getLogger(FromUIThread.class);
  private final Firestore db;

  private final ConnectionThread connectionThread;


  FromUIThread(final Firestore db, final ConnectionThread connectionThread) {
    this.db = db;
    this.connectionThread = connectionThread;
  }

  @PostConstruct
  void afterSetup() {
    this.db.collection("messages").whereEqualTo("source", TIRC_DATA_SOURCE)
            .addSnapshotListener(this::listenNewDataFromUi);
  }

  private void listenNewDataFromUi(final QuerySnapshot queryDocumentSnapshots, final FirestoreException firestoreException) {
    if (nonNull(firestoreException)) {
      handleListenException(firestoreException);
      return;
    }

    queryDocumentSnapshots.getDocumentChanges()
            .forEach(queryDocumentSnapshot -> {
              final var message = Message.fromDocument(queryDocumentSnapshot.getDocument());
              logger.info("new data arrived: {}", message);
              switch (message.type()) {
                case "comment" -> handleComment(message);
                default -> logger.warn("Cannot handle message!");
              }
            });

  }

  private void handleComment(final Message message) {
    connectionThread.writeLine(message.toSocketMessage(connectionThread.getChannel()));
  }

  private void handleListenException(final FirestoreException firestoreException) {
    logger.warn("ERROR retrieving new data from firestore tirc db", firestoreException);
  }

}
