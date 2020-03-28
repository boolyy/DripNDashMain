package edu.rutgers.dripndashproject;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

import static android.content.ContentValues.TAG;


// Most of the functions needs to have the activity changed
// Needed to assign an activity so that the code doesn't throw errors
// In the activities that that are going to call the functions you need to assign them properly
// This whole file needs to be tested

public class JobRequestFireStore extends JobRequest{


    // FireStore database (Class variable)
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Needs Reference
    public JobRequestFireStore(String customerUID, Timestamp requestTimestamp, String customerName,
                               String dorm, String dormRoom, String customerInstructions,
                               int numLoadsEstimate) {
        super(customerUID, requestTimestamp, customerName, dorm, dormRoom, customerInstructions,
                numLoadsEstimate);
    }


    // Write to job request that are pending assignment to dorm collection
    private void writeJobRequest(JobRequest jobRequest){

        // Hash map to write data into the database
        final Map<String, Object> Document = new HashMap<>();
        Document.put("Amount_Paid", jobRequest.amountPaid);
        Document.put("ASSIGNED_TIMESTAMP", jobRequest.assignedTimestamp );
        Document.put("CUSTOMER_INSTRUCTIONS", jobRequest.customerInstructions);
        Document.put("CUSTOMER_NAME", jobRequest.customerName);
        Document.put("DASHER_RATING", jobRequest.dasherRating);
        Document.put("DASHER_UID", jobRequest.dasherUID);
        Document.put("DORM", jobRequest.dorm);
        Document.put("DORM_ROOM", jobRequest.dormRoom);
        Document.put("MACHINE_COST", jobRequest.machineCost);
        Document.put("NUM_LOADS_ACTUAL", jobRequest.numLoadsActual);
        Document.put("NUM_LOADS_ESTIMATE", jobRequest.numLoadsEstimate);
        Document.put("REQUEST_TIMESTAMP", jobRequest.requestTimestamp);
        Document.put("WAS_CANCELLED", jobRequest.wasCancelled);

        // Write to the database
        db.collection("dormName").document("jobPendingAssignment").set(jobRequest.jobID)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }) // Checks to see if it failed
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    /*
        Main Job Functions
     */
    // Get the oldest Job in the database   (Needs to be test)
    private JobRequest getOldestJob(Dasher dasher){
        // New instance of a JobRequest
        JobRequest jobRequest = new JobRequest();
        // Order the database  // Dorms pending assignment // Need to write if statements
        Task<QuerySnapshot> jobID = db.collection("dorms").document(dasher.dorm)
                .collection("jobsPendingAssignment")
                .orderBy("REQUEST_TIMESTAMP", Query.Direction.DESCENDING)
                .limit(1).get();

        // Convert jobID to string
        jobRequest.jobID = jobID.toString();

        // Return the jobRequest instance
        return jobRequest;

    }

    // Dasher gets job runs the getOldestJob which then if accepted runs onAssignmentAccept
    JobRequest getJob(Dasher dasher, Boolean yesOrNo){
        // Create new JobRequest instance
        JobRequest inProgressJobRequest = new JobRequest();

        // Get whether or not the Dasher accepted the job
        int answer = wasAccepted(yesOrNo);

        // If the dasher accepted run the other functions
        if(answer == 1){

            // Get the oldest job
            inProgressJobRequest = getOldestJob(dasher);
            // Get document reference
            DocumentReference documentReference = db.collection("dorms")
                    .document(dasher.dorm)
                    .collection("jobsPendingAssignment")
                    .document(inProgressJobRequest.jobID);

            // Starting transferring data
            JobRequest finalInProgressJobRequest = inProgressJobRequest;
            // Get document and Transfer data
            documentReference.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            finalInProgressJobRequest.jobID = jobID;
                            finalInProgressJobRequest.customerUID = customerUID;
                            finalInProgressJobRequest.currentStage = currentStage;
                            finalInProgressJobRequest.Status = Status;
                            finalInProgressJobRequest.wasCancelled = wasCancelled;
                            finalInProgressJobRequest.amountPaid = amountPaid;
                            finalInProgressJobRequest.assignedTimestamp = assignedTimestamp;
                            finalInProgressJobRequest.customerInstructions = customerInstructions;
                            finalInProgressJobRequest.customerRating = customerRating;
                            finalInProgressJobRequest.customerReview = customerReview;
                            finalInProgressJobRequest.dasherName = dasherName;
                            finalInProgressJobRequest.dasherRating = dasherRating;
                            finalInProgressJobRequest.dasherUID = dasherUID;
                            finalInProgressJobRequest.dorm = dorm;
                            finalInProgressJobRequest.dormRoom = dormRoom;
                            finalInProgressJobRequest.customerName = customerName;
                            finalInProgressJobRequest.machineCost = machineCost;
                            finalInProgressJobRequest.numLoadsActual = numLoadsActual;
                            finalInProgressJobRequest.numLoadsEstimate = numLoadsEstimate;
                            finalInProgressJobRequest.requestTimestamp = requestTimestamp;
                        }
                    });

            // Run on Assignment Accept
            onAssignmentAccept(finalInProgressJobRequest, dasher);

            // Return inProgressJobRequest instance
            return finalInProgressJobRequest;
        }
        // Return empty JobRequest
        return inProgressJobRequest;
    }

    // Checks to see if the job was accepted (This needs to be worked on)
    private int wasAccepted(Boolean so){
        // Checks to see if the dasher accepted the Job
        if(so == Boolean.TRUE){
            // If the did
            return 1;
            // If they didn't
        }else{
            return 0;
        }

    }

    // Update on AssignmentAccept (Add success listeners of nah?)
    private void onAssignmentAccept(JobRequest jobRequest, Dasher dasher) {
        // Document Reference (Needs change)
        DocumentReference job = db.collection("dorms")
                .document("dormName")
                .collection("jobsPendingAssignment").document(jobRequest.jobID);
        // Get the time/date
        Date currentTime = Calendar.getInstance().getTime();
        // Update the FireBase object in the database
        job.update("ASSIGNED_TIMESTAMP", currentTime); // Updates the timestamp
        job.update("DASHER_UID", dasher.uid); // Updates the job with the dasher's uid
        // Update the Job Status to Dasher Accepted
        updateJobStatus(jobRequest, 1);
        // Move the document from jobs pending assignment to jobs in progress
        moveToInProgressJobs(jobRequest);
        // Update the local JobRequest Object
        jobRequest.assignedTimestamp = assignedTimestamp; // Update the timestamp
        jobRequest.dasherUID = dasherUID; // Assign dasher's uid
        jobRequest.dasherName = dasher.firstName+ " " + dasher.lastName; // Write dasher full name
    }

    // Update a job's status // Error
    private void updateJobStatus(JobRequest jobRequest, int currentStage) {

        // Checks to see if the landry has been completed
        if(currentStage == 5){
            moveToCompleted(jobRequest);

        // Update to current stage
        }else{
            // Document Reference
            DocumentReference job = db.collection("jobsInProgress")
                    .document(jobRequest.jobID);
            // Update the FireBase object in the database
            job.update("CURRENT_STAGE", currentStage)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                // If Job was deleted
                public void onSuccess(Void aVoid) {
                    // Check to see if the status was update

                }// If Job couldn't be deleted
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            // Update the local JobRequest Object
            jobRequest.currentStage = currentStage;
        }
    }

    // Move document from dorm collection into inProgressJob collection
    // Move jobs in progress to completed jobs
    private void moveToInProgressJobs(final JobRequest jobRequest){

        // From path
        final DocumentReference fromDoc = db.collection("dorms").
                document("dormName").collection("jobsPendingAssignment")
                .document(jobRequest.jobID);
        // To Path
        final DocumentReference toDoc = db.collection("jobsInProgress")
                .document(jobRequest.jobID);

        fromDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // Check to see if successful
                if(task.isSuccessful()){
                    final DocumentSnapshot document = task.getResult();
                    if(document != null){
                        toDoc.set(document.getData()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Transfer document
                                Log.d(TAG, "Document Transferred");
                                // Delete the job
                                fromDoc.delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Document successfully deleted");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            // Can't delete document from the jobsInProgress
                                            // Collection
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    // If the document can't be transferred
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document",e);

                                    }
                                });
                    }else{
                        // If the document doesn't exist
                        Log.d(TAG, "No such document");
                    }

                }else{
                    // If the process fails
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /*
        Other Functions
     */

    // Deletes a Job (Need to figure activities)
    private void deleteJobRequest(JobRequest jobRequest){
        // Get the file to delete
        db.collection("jobsInProgress").document(jobRequest.jobID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            // If Job was deleted
            public void onSuccess(Void aVoid) {

            }// If Job couldn't be deleted
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    // Cancel the job
    private void cancelJob(JobRequest jobRequest){
        // Cancel the job
        jobRequest.wasCancelled = Boolean.TRUE;
        // Delete the job from the FireStore database
        deleteJobRequest(jobRequest);
    }

    // Move jobs in progress to completed jobs
    private void moveToCompleted(final JobRequest jobRequest){

        // From path
        final DocumentReference fromDoc = db.collection("jobsInProgress").document(jobRequest.jobID);
        // To Path
        final DocumentReference toDoc = db.collection("jobsCompleted").document(jobRequest.jobID);

        fromDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // Check to see if successful
                if(task.isSuccessful()){
                    final DocumentSnapshot document = task.getResult();
                    if(document != null){
                        toDoc.set(document.getData()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Transfer document
                                Log.d(TAG, "Document Transferred");
                                // Delete the job
                                fromDoc.delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Document successfully deleted");
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            // Can't delete document from the jobsInProgress
                                            // Collection
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    // If the document can't be transferred
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document",e);

                                    }
                                });
                    }else{
                        // If the document doesn't exist
                        Log.d(TAG, "No such document");
                    }

                }else{
                    // If the process fails
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
    
}
