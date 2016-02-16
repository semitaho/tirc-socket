package fi.toni.tircsocket.client;

import fi.toni.tircsocket.dto.request.IrcLine;
import fi.toni.tircsocket.dto.request.IrcUser;

import java.util.Map;

/**
 * Created by taho on 11/02/16.
 */

public interface TircSocketClient {

  public void sendTopic(String topic);

  public void sendUsers(Map<String, IrcUser> tircUserMap);

  public void sendNewLine(IrcLine line);


}
