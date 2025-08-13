package fi.toni.tircsocket;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

  @Bean
  public Firestore configDb()  throws Exception{
    // Use a service account
    ClassLoader classLoader = getClass().getClassLoader();
    final var inputStream = classLoader.getResourceAsStream("tirc-sa.json");
    final var credentials = GoogleCredentials.fromStream(inputStream);
    final var options = new FirebaseOptions.Builder()
            .setCredentials(credentials)
            .build();
    FirebaseApp.initializeApp(options);

    return FirestoreClient.getFirestore();
  }
}
