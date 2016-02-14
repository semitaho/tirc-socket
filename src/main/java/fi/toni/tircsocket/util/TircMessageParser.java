package fi.toni.tircsocket.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TircMessageParser {

  private static Pattern linkPattern;

  static {
    final String REGEX_PATTERN = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    //	final String REGEX_PATTERN = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%\\?=~_|!:,\\.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    linkPattern = Pattern.compile(REGEX_PATTERN);
  }


  public static List<String> fetchUrls(String text) {
    Matcher matcher = linkPattern.matcher(text);
    List<String> urls = new ArrayList<String>();
    while (matcher.find()) {
      urls.add(matcher.group());
    }
    return urls;

  }



}