package fi.toni.tircsocket.rest;

import fi.toni.tircsocket.dto.response.IrcText;
import fi.toni.tircsocket.thread.ConnectionThread;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by taho on 16/02/16.
 */

@RestController
@RequestMapping("/irc")
public class IrcRestService {

  static Logger log = Logger.getLogger(IrcRestService.class);

  @Autowired
  private ConnectionThread cthread;

  @RequestMapping(value = "/sendtext", method = RequestMethod.POST)
  public void sendText(@RequestBody IrcText text) {
    log.debug("SEND text: " + text);
    cthread.writeLine("PRIVMSG " + cthread.getChannel() + " :(" + text.getNick()
            + "): " + text.getText());
  }

  @RequestMapping(value = "/leave", method = RequestMethod.POST)
  public void leave(@RequestBody String nick) {
    log.debug("LEFT from channel with nick: " + nick);
    cthread.writeLine("PRIVMSG " + cthread.getChannel() + " :\u0001ACTION poistui paikalta nickillä " + nick);
  }

  @RequestMapping(value = "/join", method = RequestMethod.POST)
  public void join(@RequestBody IrcText text) {
    log.debug("JOIN to channel with nick: " + text.getNick());
    cthread.writeLine("PRIVMSG " + cthread.getChannel() + " :\u0001ACTION saapui paikalle nickillä " + text.getNick() + " " + text.getText());
  }
}
