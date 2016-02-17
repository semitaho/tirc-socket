package fi.toni.tircsocket.thread;

import fi.toni.tircsocket.dto.DataHolder;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

public class WhoisThread {

  private int NEXT_WHOIS_INTERVAL = 4100;
  private long currentTime;
  static Logger log = Logger.getLogger(WhoisThread.class);
  private Iterator<String> whoisNicksIterator;
  private ConnectionThread thread;
  private DataHolder dataHolder;

  public WhoisThread() {

  }

  private void askWhois(String nick) {
    final String WHOIS_LINE = "WHOIS " + nick + " " + nick;
    writeLine(WHOIS_LINE);
  }

  public void init(ConnectionThread th, DataHolder dataHolder) {
    this.thread = th;
    this.dataHolder = dataHolder;
    currentTime = System.currentTimeMillis();
    NEXT_WHOIS_INTERVAL = th.getNextWhoisInterval();

  }

  public void refresh() {

    ArrayList<String> arrayList = new ArrayList<String>(dataHolder.getUsers().keySet());
    whoisNicksIterator = arrayList.iterator();

  }

  public void run() {
    if (null != whoisNicksIterator
            && whoisNicksIterator.hasNext()
            && System.currentTimeMillis() - currentTime >= NEXT_WHOIS_INTERVAL) {
      String next = whoisNicksIterator.next();
      askWhois(next);
      currentTime = System.currentTimeMillis();

    }

    if (null == whoisNicksIterator || whoisNicksIterator.hasNext() == false) {
      refresh();
    }

  }

  public void writeLine(String rivi) {
    thread.writeLine(rivi);

  }

}