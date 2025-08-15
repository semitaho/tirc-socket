package fi.toni.tircsocket;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutionException;

import static java.lang.System.getenv;
import static java.util.Optional.ofNullable;

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

  private final String configuration;

  private DocumentSnapshot config;

  @Autowired
  private Firestore db;


  public TircConfiguration() {
    configuration = loadConfigurationIdentifier();
    log.info("Configuration loaded with environment: {}", configuration);

  }

  @PostConstruct
  public void afterCreate() {
    final var configRef = db.collection("configuration");
    final var prodRef = configRef.document("prod");


    try {
      this.config = prodRef.get().get();

    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }

  }


  @SuppressWarnings("all")
  @PreDestroy
  public void onDestroy() {
    log.info("Configuration destroyed.");
  }

  private String loadConfigurationIdentifier() {
    final var env = ofNullable(getenv("env"))
            .orElse("prod");
    log.info("USING env variable: {}", env);
    return env;
  }

  public String getServerHost() {
    return getProperty(TIRC_SERVER_HOST_KEY);
  }

  public Long getNamesInterval() {
    final var propertyValue = config.getLong(TIRC_INTERVAL_NAMES);
    return propertyValue;

  }

  public Integer getWhoisInterval() {
    return config.getLong(TIRC_INTERVAL_WHOIS).intValue();
  }

  public String getChannel() {
    return getProperty(TIRC_SERVER_CHANNEL_KEY);
  }

  public String getProperty(String propKey) {
    return config.get(propKey, String.class);
  }
}