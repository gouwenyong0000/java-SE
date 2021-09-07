package com.atguigu.json;

import com.atguigu.pojo.Person;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JsonTest1 {

    //    1.2.1、javaBean和json的互转
    @Test
    public void test1() {

        Person name1 = new Person(1, "name1");
        Gson gson = new Gson();
        String toJson = gson.toJson(name1);
        System.out.println("toJson = " + toJson);
        Person person = gson.fromJson(toJson, Person.class);
        System.out.println(person);

    }

    //    1.2.2、List 和json的互转
    @Test
    public void test2() {

        ArrayList<Person> people = new ArrayList<>();

        people.add(new Person(1, "name1"));
        people.add(new Person(2, "name2"));
        people.add(new Person(13, "name3"));

        Gson gson = new Gson();
        String jsonElement = gson.toJson(people);
        System.out.println(jsonElement);


        gson.fromJson(jsonElement,new TypeToken<List<Person>>(){}.getType());

    }

    //    1.2.3、map 和json的互转
    @Test
    public void test3() {


    }


}
