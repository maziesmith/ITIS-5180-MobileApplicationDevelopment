package com.mad.inclass11.utility;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sanket on 10/31/2016.
 */

public class TimeUtility {


    public static String prettyDate(String dateContender) throws Exception {
        Date stringToDate = null;
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

        try {
            stringToDate = formatter.parse(dateContender);
        } catch (ParseException e) {
            throw new Exception("Given date and corresponding format doesn't match");
        }
        PrettyTime p = new PrettyTime();
        return p.format(stringToDate);
    }

}
