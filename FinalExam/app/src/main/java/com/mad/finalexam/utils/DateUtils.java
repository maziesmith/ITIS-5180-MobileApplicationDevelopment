package com.mad.finalexam.utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sanket on 10/20/2016.
 */

public class DateUtils {

    /**
     * Returns the newly formatted date from old one.
     *
     * @param oldDate
     * @param newFormat
     * @return
     * @throws Exception
     */
    public static String dateFormatChange(Date oldDate, String newFormat) throws Exception {
        String formattedDate = null;
        DateFormat dateFormat = new SimpleDateFormat(newFormat);
        formattedDate = dateFormat.format(oldDate);
        return formattedDate;
    }


    /**
     * Converts String Date to Date object
     *
     * @param dateContender
     * @param currentFormat
     * @return
     */
    public static Date stringToDate(String dateContender, String currentFormat) throws Exception {
        Date stringToDate = null;
        DateFormat dateFormat = new SimpleDateFormat(currentFormat, Locale.ENGLISH);

        try {
            stringToDate = dateFormat.parse(dateContender);
        } catch (ParseException e) {
            throw new Exception("Given date and corresponding format doesn't match");
        }
        return stringToDate;
    }


    public static String prettyDate(Date date) {

        PrettyTime p = new PrettyTime();

        return p.format(date);
    }

}
