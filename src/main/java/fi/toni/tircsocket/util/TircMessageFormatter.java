/**
 *
 */
package fi.toni.tircsocket.util;

import java.util.List;

/**
 * @author taho
 */
public class TircMessageFormatter {

  public static String formatComment(String text) {
    List<String> urls = TircMessageParser.fetchUrls(text);
    String formattedText = new String(text);
    for (String url : urls) {
      String tircLink = formatTircLink(url);

      formattedText = formattedText.replace(url, tircLink);
    }
    return formattedText;
  }

  public static String formatTircLink(String url) {
    final String LINK_TEMPLATE = "<a target=\"_blank\" href=\"%s\">%s</a>";
    return String.format(LINK_TEMPLATE, url, url);
  }
}