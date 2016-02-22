/**
 *
 */
package fi.toni.tircsocket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author Toni
 */
public class ConnectionClient {

  private BufferedWriter bw;

  private ConnectionClient(Socket socket) {
    try {
      bw = new BufferedWriter(new OutputStreamWriter(
              socket.getOutputStream(), StandardCharsets.UTF_8));
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public static ConnectionClient create(Socket socket) {
    return new ConnectionClient(socket);
  }

  public void write(String text) {
    try {
      bw.write(text);
      bw.flush();

    } catch (IOException e) {
      throw new RuntimeException("Error writing text: " + text, e);

    }
  }

  public void close() {
    try {
      bw.close();
    } catch (IOException e) {
      throw new RuntimeException(e);

    }
  }
}