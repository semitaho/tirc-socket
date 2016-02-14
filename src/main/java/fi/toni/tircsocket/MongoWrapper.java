/**
 *
 */
package fi.toni.tircsocket;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.Logger;
import org.bson.Document;
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


  static Logger log = Logger.getLogger(MongoWrapper.class);

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

    String textUri = "mongodb://tircuser:tirc123@ds037451.mongolab.com:37451/tirc";
    MongoClientURI mongoClientURI = new MongoClientURI(textUri);
    MongoClient mongoClient = new MongoClient(mongoClientURI);
    tircDb = mongoClient.getDatabase("tirc");

  }




  public Document loadConfiguration(String env) {
    MongoCollection<Document> collection = tircDb
            .getCollection(COLLECTION_CONFIGURATION);
    Document doc = collection.find(eq("_id", env)).first();
    return doc;

  }
}