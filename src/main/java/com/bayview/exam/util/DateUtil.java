package com.bayview.exam.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by romelquitasol on 27/08/2018.
 */
public class DateUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

  /**
   * Date util to get time string format
   * @param dateStr
   * @return
   */
  public static String getTimeStr(String dateStr) {
    String time = "";
    try {
      DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = sdf.parse(dateStr);
      DateFormat timeFormat = new SimpleDateFormat("HH:mm");
      time = timeFormat.format(date);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
    return time;
  }

}
