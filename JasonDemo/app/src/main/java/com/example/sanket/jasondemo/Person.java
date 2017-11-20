package com.example.sanket.jasondemo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sanket on 9/19/16.
 */
public class Person {

    private String name,department;
    private int id, age;


    public static Person getPerson(JSONObject obj) throws JSONException {
      Person person = new Person();
        person.setName(obj.getString("name"));
        person.setDepartment(obj.getString("department"));
        person.setId(obj.getInt("id"));
        person.setAge(obj.getInt("age"));
        return  person;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", id=" + id +
                ", age=" + age +
                '}';
    }
}
