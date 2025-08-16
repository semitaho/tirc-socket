package fi.toni.tircsocket.dto;

import com.google.cloud.firestore.DocumentSnapshot;

public record Message(Long time, String nick, String type, String comment, String source) {

  public static Message fromDocument(final DocumentSnapshot documentSnapshot) {
    return new Message(
            documentSnapshot.getLong("time"),
            documentSnapshot.getString("nick"),
            documentSnapshot.getString("type"),
            documentSnapshot.getString("comment"),
            documentSnapshot.getString("source")

    );
  }

  public String toSocketMessage(final String channel) {
    return "PRIVMSG %s :(%s): %s".formatted(channel, nick(), comment());
  }
}
