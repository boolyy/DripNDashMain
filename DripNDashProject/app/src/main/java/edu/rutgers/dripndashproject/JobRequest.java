package edu.rutgers.dripndashproject;

import com.google.firebase.Timestamp;

public class JobRequest {
    String jobID;
    String customerUID;
    Timestamp requestTimestamp;
    String customerName;
    String dorm;
    String dormRoom;
    String customerInstructions;
    int numLoadsEstimate;
    String dasherUID;
    String dasherName;
    double dasherRating;
    Timestamp assignedTimestamp;
    int currentStage;
    int numLoadsActual;
    double machineCost;
    double amountPaid;
    boolean wasCancelled;
    String customerReview; //customer reviews dasher
    double customerRating; //customer rates dasher

    //when customer creates a job request
    public JobRequest(String jobID, String customerUID, Timestamp requestTimestamp, String customerName, String dorm, String dormRoom, String customerInstructions, int numLoadsEstimate){ //check to see if amountPaid is needed
        this.jobID = jobID;
        this.customerUID = customerUID;
        this.requestTimestamp = requestTimestamp;
        this.customerName = customerName;
        this.dorm = dorm;
        this.dormRoom = dormRoom;
        this.customerInstructions = customerInstructions;
        this.numLoadsEstimate = numLoadsEstimate;


        this.currentStage = 0;

        this.wasCancelled = false;
    }

    public void onAssignment(Dasher dasher, Timestamp time){ //runs when job is assigned to Dasher
        this.dasherUID = dasher.uid;
        this.dasherName = dasher.firstName + " " + dasher.lastName;
        this.dasherRating = dasher.rating;
        this.assignedTimestamp = time;
        this.currentStage = 1; //updates status of job to dasher on way for pick up
    }



}
