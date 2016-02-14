package fi.toni.tircsocket.thread;

import fi.toni.tircsocket.ConnectionClient;
import fi.toni.tircsocket.TircConfiguration;
import fi.toni.tircsocket.client.TircSocketClient;
import fi.toni.tircsocket.dto.IrcLine;
import fi.toni.tircsocket.dto.IrcUser;
import fi.toni.tircsocket.filter.ActionFilter;
import fi.toni.tircsocket.filter.ChanFilter;
import fi.toni.tircsocket.filter.PrivMessageFilter;
import fi.toni.tircsocket.filter.TextReceiverFilter;
import fi.toni.tircsocket.util.LogFileParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.AsyncRestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class ConnectionThread extends Thread {

  public static final String THREAD_SERVICE = "EXECUTOR_THREAD";

  private ConnectionClient client;
  protected InputStreamReader streamReader;
  private Socket socket;
  private String nick;
  private String nicksId;

  @Autowired
  private TircSocketClient socketClient;

  private List<TextReceiverFilter> filters = new ArrayList<TextReceiverFilter>();
  private Map<String, String> opFilters = new HashMap<>();

  {
    final String ANGERE = "a91-153-145";
    opFilters.put("taho", ANGERE);
    opFilters.put("hannaz", ANGERE);
    opFilters.put("hasu", ANGERE);
    opFilters.put("Andon", "pp.htv.fi");

  }

  @Autowired
  private TircConfiguration instance;

  private WhoisThread whoisThread;

  private long NAMES_INTERVAL = 17000;

  private String channel;
  private volatile Map<String, IrcUser> oldTircUsers;
  static Logger log = Logger.getLogger(ConnectionThread.class);

  public ConnectionThread() {

  }

  @PostConstruct
  public void postConstruct() {
    log.debug("instance: " + this.instance);
    whoisThread = new WhoisThread();
    NAMES_INTERVAL = instance.getNamesInterval();
    channel = instance.getChannel();
    oldTircUsers = new HashMap<>();
    nick = instance.getProperty(TircConfiguration.TIRC_SERVER_NICK_KEY);
    registerFilters();
  }

  public ConnectionThread(TircConfiguration configuration) {
    super();
    this.instance = configuration;

  }

  private void registerFilters() {
    filters.add(new PrivMessageFilter());
    filters.add(new ChanFilter());
    filters.add(new ActionFilter());
  }

  @Override
  public void run() {
    long names_interval = System.currentTimeMillis();
     whoisThread.init(this);

    String textBuffer = null;
    writeLine("PRIVMSG " + channel + " :" + instance.getProperty(TircConfiguration.TIRC_SERVER_JOIN_MESSAGE_KEY));
    while (socket.isConnected() && socket.isClosed() == false) {
      whoisThread.run();
      try {
        textBuffer = readLine();
        if (System.currentTimeMillis() - names_interval > NAMES_INTERVAL
                && textBuffer.contains(" 451 ") == false) {
          writeLine("NAMES " + channel);
          names_interval = System.currentTimeMillis();
        }

      } catch (Exception se) {
        throw new RuntimeException("", se);
      }
      if (!applyActions(textBuffer)) {
        log.info("closed! bye!");
        break;
      }

      // apply filters
      applyTextlineFilters(textBuffer);
    }
  }

  private char converChar(int chr) {
    if (chr == 164) {
      return 'ä';
    }
    if (chr == 165) {
      return 'å';
    }
    if (chr == 133) {
      return 'Å';
    }

    if (chr == 132) {
      return 'Ä';
    }

    if (chr == 150) {
      return 'Ö';
    }

    if (chr == 182) {
      return 'ö';
    }
    return (char) chr;
  }

  private boolean skipChar(int chars) {
    if (chars == 195) {
      return true;
    }
    return false;
  }

  private String readLine() {
    int chars = 0;
    StringBuilder textBuilder = new StringBuilder();
    try {
      do {
        chars = streamReader.read();
        if (skipChar(chars)) {
          continue;
        }
        char converChar = converChar(chars);

        textBuilder.append(converChar);
      } while (chars != -1 && chars != 10);
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    } finally {
    }
    return textBuilder.toString();

  }

  public String getChannel() {
    return this.channel;
  }

  private final boolean isWhoisLine(String textBuffer, String nick) {
    final String SEARCHED_TEXT_PHRASE = " 317 " + nick + " ";
    if (textBuffer.contains(SEARCHED_TEXT_PHRASE)) {
      return true;
    }
    return false;
  }

  public boolean isNamesLine(String channel, String nick, String textBuffer) {
    if (textBuffer.contains(channel) && textBuffer.contains(nick)
            && textBuffer.contains("353")) {
      return true;
    }
    return false;
  }

  public boolean connect() throws RuntimeException {
    final int DEFAULT_PORT = 6667;

    try {

      socket = new Socket(
              instance.getProperty(TircConfiguration.TIRC_SERVER_HOST_KEY),
              DEFAULT_PORT);

      client = ConnectionClient.create(socket);

      client.write("USER "
              + instance
              .getProperty(TircConfiguration.TIRC_SERVER_USER_KEY)
              + " 8 * :Clientti-taho\n");
      client.write("NICK "
              + instance
              .getProperty(TircConfiguration.TIRC_SERVER_NICK_KEY)
              + "\n");
      client.write("JOIN " + instance.getProperty(TircConfiguration.TIRC_SERVER_CHANNEL_KEY)
              + "\n");

      try {

        streamReader = new InputStreamReader(socket.getInputStream(),
                StandardCharsets.ISO_8859_1);
      } catch (IOException e) {
        throw new RuntimeException("", e);
      }

      while (socket.isConnected() && socket.isClosed() == false) {
        try {
          streamReader.read();
          try {
            return true;
          } catch (RuntimeException re) {
            log.warn("", re);
          }
        } catch (IOException e) {
          log.warn("", e);
        }

      }
      return true;
    } catch (IOException ioe) {
      log.warn("", ioe);
      return false;
    }

  }

  public void writeLine(String rivi) {
    AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
    client.write(rivi + "\n");
    //applyFilters(":" + nick + "! " + rivi);
  }

  public void writePrivateLine(String fromNick, String toNick, String rivi) {
    String protocolLine = "PRIVMSG " + toNick + " :" + fromNick
            + " privailoo: " + rivi;
    client.write(protocolLine + "\n");
    applyTextlineFilters(":" + fromNick + "! PRIVMSG " + toNick + " :" + rivi);

  }

  public int getNextWhoisInterval() {
    return instance.getWhoisInterval();
  }

  @PreDestroy
  public void onDestroy() {
    writeLine("QUIT :" + instance.getProperty(TircConfiguration.TIRC_SERVER_QUIT_MESSAGE_KEY));

    try {
      client.close();
      if (streamReader != null) {
        streamReader.close();
      }
      if (socket != null) {
        socket.close();
      }
      log.debug("tIrc connection thread destroyed");

    } catch (IOException e) {
      log.error(e);
    }

  }


  public void applyTextlineFilters(String text) {
    for (TextReceiverFilter filter : filters) {
      if (filter.isSupported(channel, text)) {
        IrcLine line = filter.apply(text);
        socketClient.sendNewLine(line);
        return;
      }
    }

  }

  private final boolean applyActions(String textBuffer) {
    if (textBuffer.contains("PRIVMSG") == false
            && (textBuffer == null || textBuffer.contains("null"))) {
      return false;
    }

    if (textBuffer.contains("JOIN")) {
      Set<String> keySet = opFilters.keySet();
      for (String nick : keySet) {
        if (textBuffer.contains(nick) && textBuffer.contains(channel)) {
          if (textBuffer.contains(opFilters.get(nick))) {
            writeLine("MODE " + channel + " +o " + nick);
            break;
          }
        }
      }
    }

    if (isNamesLine(channel, nick, textBuffer)) {
      processNamesLine(textBuffer);
    } else if (isWhoisLine(textBuffer, nick)) {
      String startFromNick = textBuffer.substring(
              textBuffer.indexOf(nick)).trim();
      String nickStr = startFromNick
              .substring(startFromNick.indexOf(" ")).trim();
      String[] split = nickStr.split(" ");
      String whoistarget = split[0];
      String secondStr = split[1];
      int second = Integer.parseInt(secondStr);
      Map<String, IrcUser> users = getUsers();
      if (users.containsKey(whoistarget)) {
        users.get(whoistarget).setIdleTime(second);
        refreshUsers(users);
        socketClient.sendUsers(users);
      }
    } else if (isTopicLine(textBuffer, channel)) {
      processTopicLine(textBuffer);
    }

    if (textBuffer.contains("PING")) {
      writeLine("PONG " + instance.getServerHost());
    }
    return true;
  }

  private void processTopicLine(String textBuffer) {
    String topicStr = textBuffer.substring(textBuffer.indexOf(channel));
    topicStr = topicStr.substring(topicStr.indexOf(":") + 1);
    socketClient.sendTopic(topicStr);

  }

  private void processNamesLine(String textBuffer) {
    Map<String, IrcUser> tircUserMap = LogFileParser
            .parseNicksFromLine(getUsers(), textBuffer);
    socketClient.sendUsers(tircUserMap);

  }

  private boolean isTopicLine(String textBuffer, String channel) {
    if (textBuffer.contains(channel)
            && (textBuffer.contains(" 332 ") || textBuffer
            .contains(" TOPIC "))) {
      return true;
    }
    return false;
  }

  /**
   * @return the nicksId
   */
  public String getNicksId() {
    return nicksId;
  }

  /**
   * @param nicksId the nicksId to set
   */
  public void setNicksId(String nicksId) {
    this.nicksId = nicksId;
  }

  public Map<String, IrcUser> getUsers() {
    return new HashMap<>(this.oldTircUsers);
  }

  public void refreshUsers(Map<String, IrcUser> users) {
    this.oldTircUsers = users;

  }

}