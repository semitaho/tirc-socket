package fi.toni.tircsocket.util;

import fi.toni.tircsocket.dto.request.IrcUser;

import java.util.HashMap;
import java.util.Map;

public class LogFileParser {
  

  public static Map<String, IrcUser> parseNicksFromLine(
          Map<String, IrcUser> oldTircUsers, String line) {
    int lastIndexOf = line.lastIndexOf(":");
    String nicks = line.substring(lastIndexOf + 1).trim();
    String[] split = nicks.split(" ");
    Map<String, IrcUser> users = new HashMap<>();
    for (String nick : split) {
      IrcUser user = null;
      if (nick.contains("@")) {
        user = new IrcUser(nick.substring(1));
      } else if (nick.contains("+")) {
        user = new IrcUser(nick.substring(1));
      } else {
        user = new IrcUser(nick);
      }
      IrcUser oldUser = oldTircUsers.get(user.getNick());
      if ((oldUser != null)) {
        user.setIdleTime(oldUser.getIdleTime());
      }
      users.put(user.getNick(), user);

    }
    return users;
  }

}