package com.example.sanket.jasondemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sanket on 9/19/16.
 */
public class PersonsUtil {

    static public class PersonJSONParser {
        static ArrayList<Person> pasrsePersons(String in) throws JSONException {
            ArrayList<Person> personsList=  new ArrayList<Person>();

            JSONObject root=new JSONObject(in);

            JSONArray personsArray= root.getJSONArray("persons");

            for( int i=0; i<personsArray.length(); i++)
            {
                JSONObject personJSONobj= personsArray.getJSONObject(i);

                Person person = Person.getPerson(personJSONobj);

                personsList.add(person);



            }

            return personsList;
        }
    }
}
