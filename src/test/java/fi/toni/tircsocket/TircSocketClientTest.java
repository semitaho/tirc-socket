package fi.toni.tircsocket;

import fi.toni.tircsocket.client.TircSocketClientLoggerImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by taho on 11/02/16.
 */
public class TircSocketClientTest {

  private TircSocketClientLoggerImpl client;
  @Before
  public void setup(){
    client = new TircSocketClientLoggerImpl();
    client.postConstruct();

  }


  @Test
  public void sendLine(){

  }

}