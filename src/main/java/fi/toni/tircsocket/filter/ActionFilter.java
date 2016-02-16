package fi.toni.tircsocket.filter;

import fi.toni.tircsocket.dto.request.IrcLine;

public class ActionFilter extends TextReceiverFilter {

  @Override
  public IrcLine apply(String textBuffer) {
    String nick = textBuffer.substring(textBuffer.indexOf(":") + 1,
            textBuffer.indexOf("!"));
    String actionStartStr = textBuffer.substring(textBuffer
            .indexOf("ACTION"));
    String actionStr = actionStartStr
            .substring(actionStartStr.indexOf(" ") + 1);
    String message =  "* " + nick + " "
            + actionStr;
    IrcLine tircLine = new IrcLine();
    tircLine.setLine(message);
    tircLine.setType("action");
    return tircLine;
  }

  @Override
  public boolean isSupported(String channel, String textBuffer) {
    if (textBuffer.contains("ACTION") && textBuffer.contains("PRIVMSG")) {
      return true;
    }
    return false;
  }

}