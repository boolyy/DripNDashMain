package edu.rutgers.dripndashproject;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class JobRequest {
    String jobID;
    String customerUID;
    Timestamp requestTimestamp;
    String customerName;
    String dorm;
    String dormRoom;
    String customerInstructions;
    int numLoadsEstimate;// Customer’s inputted estimate of number of loads
    // assigned value on assignment to dasher
    String dasherUID;
    String dasherName;
    double dasherRating;
    Timestamp assignedTimestamp;
    // dynamic properties (updated by dasher during job)
    int currentStage;
    // properties assigned @ stage “finished drying” = stage7
    int numLoadsActual;
    double machineCost;
    double amountPaid;
    boolean wasCancelled;
    Timestamp completedTimestamp;

    // properties for completed jobs
    String customerReview;
    Double customerRating;
    // static and computed properties

    public static Map<Integer,String> createStatusMap(){ //creates map to be assigned to stages Hash Map
        Map<Integer,String> statusMap = new HashMap<>();
        statusMap.put(0, "Waiting for Dasher to accept");
        statusMap.put(1, "Dasher accepted request");
        statusMap.put(2, "Dasher on way for pickup");
        statusMap.put(3, "Picked up laundry");
        statusMap.put(4, "Laundry in washer");
        statusMap.put(5, "Finished washing");
        statusMap.put(6, "Laundry in dryer");
        statusMap.put(7, "Finished drying");
        statusMap.put(8, "Dasher on way for drop off");
        statusMap.put(9, "Dropped off laundry");

        return statusMap;
    }
    Map<Integer, String> stages = createStatusMap();
    String currentStatus = this.stages.get(currentStage); //current status assigned from hash map stages

    //constructor method
    public JobRequest(String jobID, String customerUID, Timestamp requestTimestamp, String customerName, String dorm, String dormRoom, String customerInstructions, int numLoadsEstimate){
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

    public void updateOnAssignment(Dasher dasher, Timestamp time){ //runs when job is assigned to Dasher
        this.dasherUID = dasher.uid;
        this.dasherName = dasher.firstName + " " + dasher.lastName;
        this.dasherRating = dasher.rating;
        this.assignedTimestamp = time;
        this.currentStage = 1; //updates status of job to dasher on way for pick up
    }



}