package com.example.api.xmlpullpasingdemo;

import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;

/**
 * Created by sanket on 9/26/16.
 */
public class PersonUtil {
    static public class PersonSAXParser {

        Person person;

        static ArrayList<Person> parsePersons(InputStream in) throws XmlPullParserException, IOException {
            XmlPullParser parse = XmlPullParserFactory.newInstance().newPullParser();
            parse.setInput(in, "UTF-8");
            Person person = null;
            ArrayList<Person> personsList = new ArrayList<Person>();

            int event = parse.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (parse.getName().equals("person")) {
                            person = new Person();
                            person.setId(parse.getAttributeValue(null, "id"));

                        } else if (parse.getName().equals("name")) {
                            person.setName(parse.nextText().trim());
                        } else if (parse.getName().equals("age")) {
                            person.setAge(parse.nextText().trim());
                        } else if (parse.getName().equals("department")) {
                            person.setDepartment(parse.nextText().trim());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parse.getName().equals("person"))
                        {
                            personsList.add(person);
                            person=null;
                        }
                    default:
                        break;
                }

                event = parse.next();

            }
            return personsList;
        }
    }
}
