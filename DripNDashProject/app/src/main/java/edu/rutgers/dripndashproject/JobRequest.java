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
    String dasherRating;
    Timestamp assignedTimestamp;
    int currentStage;
    int numLoadsActual;
    double machineCost;
    double amountPaid;
    boolean wasCancelled;
    String customerReview; //customer reviews dasher
    double customerRating; //customer rates dasher

    public JobRequest(String jobID, String customerUID, Timestamp requestTimestamp, String customerName, String dorm, String dormRoom, String customerInstructions,       //constructor method
                      int numLoadsEstimate, String dasherUID, String dasherName, String dasherRating, Timestamp assignedTimestamp, int currentStage, int numLoadsActual){ //check to see if amountPaid is needed
        this.jobID = jobID;
        this.customerUID = customerUID;
        this.requestTimestamp = requestTimestamp;
        this.customerName = customerName;
        this.dorm = dorm;
        this.dormRoom = dormRoom;
        this.customerInstructions = customerInstructions;
        this.numLoadsEstimate = numLoadsEstimate;
        this.dasherUID = dasherUID;
        this.dasherName = dasherName;
        this.dasherRating = dasherRating;
        this.assignedTimestamp = assignedTimestamp;
        this.currentStage = currentStage;
        this.numLoadsActual = numLoadsActual;
        this.wasCancelled = false;
    }

}
