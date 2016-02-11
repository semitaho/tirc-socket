package fi.toni.tircsocket;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by taho on 11/02/16.
 */
public class TircSocketClientTest {

  private TircSocketClient client;
  @Before
  public void setup(){
    client = new TircSocketClient();
    client.postConstruct();

  }


  @Test
  public void sendLine(){
    client.sendNewLine();


  }

}