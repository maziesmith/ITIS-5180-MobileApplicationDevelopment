package com.example.api.xmlsaxparsingdemo;

import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;

/**
 * Created by sanket on 9/26/16.
 */
public class PersonUtil {
    static public class PersonSAXParser extends DefaultHandler {
        ArrayList<Person> personsList;
        Person person;
        StringBuilder xmlInnerTest;

        static public ArrayList<Person> parsePerson(InputStream in) throws IOException, SAXException {

            PersonSAXParser parser = new PersonSAXParser();

            Xml.parse(in, Xml.Encoding.UTF_8, parser);

            return parser.getPersonsList();
        }


        public ArrayList<Person> getPersonsList() {
            return personsList;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            personsList= new ArrayList<Person>();
            xmlInnerTest= new StringBuilder();

        }


        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if(localName.equals("person"))
            {
               person = new Person();
                person.setId(attributes.getValue("id"));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if(localName.equals("person"))
            {
                personsList.add(person);
            }else if(localName.equals("name"))
            {
                person.setName(xmlInnerTest.toString().trim());
            }else if(localName.equals("age"))
            {
                person.setAge(xmlInnerTest.toString().trim());
            }else if(localName.equals("department"))
            {
                person.setDepartment(xmlInnerTest.toString().trim());
            }
            xmlInnerTest.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            xmlInnerTest.append(ch, start, length);
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }


    }
}
