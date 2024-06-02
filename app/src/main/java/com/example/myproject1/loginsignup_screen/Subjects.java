package com.example.myproject1.loginsignup_screen;

import java.util.ArrayList;

public class Subjects {
    private ArrayList<String> subjectsArray;

    public Subjects(ArrayList<String> arrayList) {
        this.subjectsArray = arrayList;
    }

    public Subjects() {
    }

    public ArrayList<String> getSubjectsArray() {
        return subjectsArray;
    }

    public void setSubjectsArray(ArrayList<String> subjectsArray) {
        this.subjectsArray = subjectsArray;
    }
}
