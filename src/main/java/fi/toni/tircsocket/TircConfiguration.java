package fi.toni.tircsocket;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.FileInputStream;
import java.io.InputStream;

@Component
public class TircConfiguration {

  public static final String TIRC_SERVER_HOST_KEY = "host";
  public static final String TIRC_SERVER_NICK_KEY = "nick";
  public static final String TIRC_SERVER_CHANNEL_KEY = "channel";
  public static final String TIRC_SERVER_USER_KEY = "user";
  public static final String TIRC_SERVER_JOIN_MESSAGE_KEY = "joinmsg";
  public static final String TIRC_SERVER_QUIT_MESSAGE_KEY = "quitmsg";
  public static final String TIRC_INTERVAL_NAMES = "intervalnames";
  public static final String TIRC_INTERVAL_WHOIS = "intervalwhois";

  static Logger log = LoggerFactory.getLogger(TircConfiguration.class);

  private String configuration;


  @Autowired
  private Firestore db;


  public TircConfiguration() {
    configuration = loadConfigurationIdentifier();
    log.info("Configuration loaded with environment: " + configuration);

  }

  @PostConstruct
  public void afterCreate() {
    db.collection("users");
  //  configurationModel = mongoWrapper.loadConfiguration(configuration);

  }


  @SuppressWarnings("all")
  @PreDestroy
  public void onDestroy() {
    log.info("Configuration destroyed.");
  }

  private String loadConfigurationIdentifier() {
    if (System.getenv("env") != null) {
      log.debug("USING env variable: " + System.getenv("env"));
      return System.getenv("env");
    }
    return "dev";
  }

  public String getServerHost() {
    return getProperty(TIRC_SERVER_HOST_KEY);
  }

  public Long getNamesInterval() {
    String property = getProperty(TIRC_INTERVAL_NAMES);
    return Long.valueOf(property);

  }

  public Integer getWhoisInterval() {
    return Integer.valueOf(getProperty(TIRC_INTERVAL_WHOIS));
  }

  public String getChannel() {
    return getProperty(TIRC_SERVER_CHANNEL_KEY);
  }

  public String getProperty(String propKey) {
    return "keijo";
  }
}