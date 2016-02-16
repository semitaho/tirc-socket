package fi.toni.tircsocket.filter;

import fi.toni.tircsocket.dto.request.IrcLine;


public class ChanFilter extends TextReceiverFilter {

  @Override
  public IrcLine apply(String textBuffer) {
    String nick = textBuffer.substring(textBuffer.indexOf(":") + 1,
            textBuffer.indexOf("!"));
    IrcLine line = new IrcLine();
    line.setNick(nick);
    if (textBuffer.contains("JOIN")) {
      //	String channel = textBuffer
      //			.substring(textBuffer.indexOf(":", 2) + 1);
      line.setType("join");
      return line;
    }
    if (textBuffer.contains("QUIT")) {
      String reason = textBuffer
              .substring(textBuffer.indexOf(":", 2) + 1);
      line.setLine(reason);
      line.setType("quit");
      return line;
    }

    if (textBuffer.contains("PART")) {
      line.setType("part");
      return line;
    }
    return null;

  }

  @Override
  public boolean isSupported(String channel, String textBuffer) {
    if (textBuffer.contains(" QUIT ") || textBuffer.contains(" JOIN ")
            || textBuffer.contains(" PART ")) {
      return true;
    }
    return false;
  }

}