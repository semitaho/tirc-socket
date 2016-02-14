package fi.toni.tircsocket.filter;

import fi.toni.tircsocket.util.TircMessageFormatter;
import fi.toni.tircsocket.dto.IrcLine;

public class PrivMessageFilter extends TextReceiverFilter {

  @Override
  public IrcLine apply(String textBuffer) {
    int indexOf = textBuffer.indexOf(":", 1);
    String text = textBuffer.substring(indexOf + 1);
    String nick = textBuffer.substring(textBuffer.indexOf(":") + 1,
            textBuffer.indexOf("!"));
    IrcLine tircLine = new IrcLine();
    String formattedText = TircMessageFormatter.formatComment(text);
    tircLine.setLine(formattedText);
    tircLine.setType("comment");
    tircLine.setNick(nick);
    return tircLine;
  }

  @Override
  public boolean isSupported(String channel, String textBuffer) {
    if (textBuffer.contains("PRIVMSG " + channel + " :")
            && textBuffer.contains("ACTION") == false) {
      return true;
    }
    return false;
  }

}