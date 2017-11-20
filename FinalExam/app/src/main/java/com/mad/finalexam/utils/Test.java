package com.mad.finalexam.utils;

import java.util.Date;

/**
 * Created by Sanket on 10/20/2016.
 */

public class Test {

 public static void main(String args[]) {
     String date = "10/20/2016";

     String format = "MM/dd/yyyy";
     Date obj=null;
     String obj1=null;
     try {
         obj=DateUtils.stringToDate(date, format);

        obj1=DateUtils.dateFormatChange(obj, "MMM dd, yyyy");
     } catch (Exception e) {
         e.printStackTrace();
     }

     System.out.println(obj.toString());
     System.out.println(obj1.toString());
 }

}
