package fi.toni.tircsocket;

import fi.toni.tircsocket.client.TircSocketClientLoggerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Created by taho on 11/02/16.
 */
public class TircSocketClientTest {

  private TircSocketClientLoggerImpl client;
  @BeforeEach
  public void setup(){
    client = new TircSocketClientLoggerImpl();
    client.postConstruct();

  }


  @Test
  public void sendLine(){

  }

}