package com.xxbb.jcip.composingobjects.safe;

import com.xxbb.jcip.annotation.ThreadSafe;

import java.util.HashSet;
import java.util.Set;

@ThreadSafe
public class PersonSet {
    private final Set<Person> mySet = new HashSet<>();

    public synchronized void addPerson(Person person) {
        mySet.add(person);
    }

    public synchronized boolean containsPerson(Person p) {
        return mySet.contains(p);
    }

    private static class Person{

    }
}
