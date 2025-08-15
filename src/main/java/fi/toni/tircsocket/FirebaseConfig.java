package fi.toni.tircsocket;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

@Configuration
public class FirebaseConfig {

  final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

  @Bean
  public Firestore configDb()  throws Exception{
    // Use a service account
    ClassLoader classLoader = getClass().getClassLoader();
    final var inputStream = classLoader.getResourceAsStream("tirc-sa.json");
    final var credentials = GoogleCredentials.fromStream(inputStream);
    final var options = FirebaseOptions.builder()
            .setCredentials(credentials)
            .build();
    FirebaseApp.initializeApp(options);
    return  FirestoreClient.getFirestore("tirc-db");
  }
}
