package com.mad.chatmessenger.Utility;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by atulb on 11/22/2016.
 */

public class Util {
    public static String getMergedId(String firstUserId, String secondUserId) {
        if (firstUserId.compareTo(secondUserId) < 0) {
            return firstUserId + secondUserId;
        } else {
            return secondUserId + firstUserId;
        }
    }

    public static String prettyDate(String dateContender) throws Exception {
        Date stringToDate = null;
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.US);

        try {
            stringToDate = formatter.parse(dateContender);
        } catch (ParseException e) {
            throw new Exception("Given date and corresponding format doesn't match");
        }
        PrettyTime p = new PrettyTime();
        return p.format(stringToDate);
    }
}
