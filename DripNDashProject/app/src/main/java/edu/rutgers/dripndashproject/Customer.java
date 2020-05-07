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
    public Customer() { //constructor

    }

    public String getDorm() {
        return dorm;
    }

}
