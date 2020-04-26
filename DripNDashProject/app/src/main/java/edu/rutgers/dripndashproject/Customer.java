package edu.rutgers.dripndashproject;

import java.util.ArrayList;

public class Customer {
    String firstName;
    String lastName;
    String email;
    String dorm;
    String dormRoom;
    String gender;
    String uid;
    int age;
    ArrayList completedJobs;//string of completed jobs uids
    double rating;
    public Customer() {

    }

    public Customer(String firstName, String lastName, String email, String dorm, String dormRoom, String gender, String uid, int age){

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDorm() {
        return dorm;
    }

    public String getDormRoom() {
        return dormRoom;
    }

    public String getGender() {
        return gender;
    }

    public String getUid() {
        return uid;
    }

    public int getAge() {
        return age;
    }

    public ArrayList getCompletedJobs() {
        return completedJobs;
    }


}
