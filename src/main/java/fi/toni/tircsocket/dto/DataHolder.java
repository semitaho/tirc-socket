package fi.toni.tircsocket.dto;

import fi.toni.tircsocket.dto.request.IrcUser;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by taho on 17/02/16.
 */
@Repository
public class DataHolder {

  private String topic;

  private volatile Map<String, IrcUser> users;

  @PostConstruct
  public void postConstruct() {
    users = new HashMap<>();
  }


  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public Map<String, IrcUser> getUsers() {
    return new HashMap<>(this.users);
  }

  public void setUsers(Map<String, IrcUser> users) {
    this.users = users;
  }
}
