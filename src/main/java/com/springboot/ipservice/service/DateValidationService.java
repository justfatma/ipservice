package com.springboot.ipservice.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.springframework.stereotype.Service;

@Service
public class DateValidationService {

  public String convertStringToIsoFormat(String dateStr) {

    List<String> formatList = new ArrayList<>();
    formatList.add("dd-MM-yyyy HH:mm:ss");
    formatList.add("dd/MM/yyyy HH:mm:ss");
    formatList.add("dd.MM.yyyy HH:mm:ss");
    formatList.add("yyyy.MM.dd HH:mm:ss");
    formatList.add("yyyy-MM-dd HH:mm:ss");
    formatList.add("yyyy/MM/dd HH:mm:ss");


    for (String format : formatList) {

      if (isValidFormat(format, dateStr)) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        try {
          Date dateInfo = simpleDateFormat.parse(dateStr);

          TimeZone tz = TimeZone.getTimeZone("UTC");
          DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
          df.setTimeZone(tz);
          return df.format(dateInfo);

        } catch (ParseException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

      }
    }

    return null;
  }


  public boolean isValidFormat(String format, String value) {

    Date date = null;
    try {
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      date = sdf.parse(value);
      if (!value.equals(sdf.format(date))) {
        date = null;
      }
    } catch (ParseException ex) {
      // ex.printStackTrace();
    }

    System.out.println("format: " + format + " value: " + value + " date: " + date);

    return date != null;
  }

}
