package fi.toni.tircsocket.filter;
import fi.toni.tircsocket.dto.IrcLine;


/**
 *
 * @author Toni
 *
 */
public abstract class TextReceiverFilter {

  /**
   * Applies filter and makes text changes
   *
   * @param textBuffer
   * @return text that is converted from socket line
   */
  public abstract IrcLine apply(String textBuffer);

  public abstract boolean isSupported(String channel, String textBuffer);


}