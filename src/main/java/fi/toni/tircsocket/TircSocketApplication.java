package fi.toni.tircsocket;

import fi.toni.tircsocket.thread.ConnectionThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by taho on 11/02/16.
 */

@SpringBootApplication
public class TircSocketApplication {

  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(TircSocketApplication.class, args);
    ConnectionThread bean = ctx.getBean(ConnectionThread.class);
    if (bean.connect()) {
      bean.start();
    }


  }


}
