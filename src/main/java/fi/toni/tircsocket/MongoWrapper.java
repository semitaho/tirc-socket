/**
 *
 */
package fi.toni.tircsocket;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * @author taho
 */
@Component
public class MongoWrapper {


  static Logger log = LoggerFactory.getLogger(MongoWrapper.class);

  public static final String COLLECTION_LOCATION = "location";
  public static final String COLLECTION_LOGS = "logs";
  public static final String COLLECTION_CONFIGURATION = "configuration";

  public static final Integer DAYS_LOGS_SUBTRACT = 2;
  public static final Integer LOCATION_LAST_POINTS_COUNT = 15;
  private MongoDatabase tircDb;

  private final List<String> SKIP_TYPE_LIST = Arrays
          .asList("welcome", "quit");

  @PostConstruct
  public void postCreate() {


  }



}