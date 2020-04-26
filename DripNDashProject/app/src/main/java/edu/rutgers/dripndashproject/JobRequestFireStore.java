package edu.rutgers.dripndashproject;

import android.util.Log;
import android.widget.Toast;
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

    public JobRequestFirestoreInterface delegate;


    // FireStore database (Class variable)
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Needs Reference
    public JobRequestFireStore(String customerUID, Timestamp requestTimestamp, String customerName,
                               String dorm, String dormRoom, String customerInstructions,
                               int numLoadsEstimate) {
        super();

    }

    public JobRequestFireStore(){

    }


    // Write to job request that are pending assignment to user's dorm collection into jobsPendingRequests
    public void writeJobRequest(JobRequest jobRequest){

        // Hash map to write data into the database
        final Map<String, Object> Document = new HashMap<>();
        Document.put("AMOUNT_PAID", jobRequest.amountPaid);
        Document.put("ASSIGNED_TIMESTAMP", jobRequest.assignedTimestamp );
        Document.put("CURRENT_STAGE", 0);
        Document.put("CUSTOMER_INSTRUCTIONS", jobRequest.customerInstructions);
        Document.put("CUSTOMER_UID", jobRequest.customerUID);
        Document.put("CUSTOMER_NAME", jobRequest.customerName);
        Document.put("DASHER_RATING", jobRequest.dasherRating);
        Document.put("DASHER_UID", jobRequest.dasherUID);
        Document.put("DASHER_NAME", jobRequest.dasherName);
        Document.put("DORM", jobRequest.dorm);
        Document.put("DORM_ROOM", jobRequest.dormRoom);
        Document.put("MACHINE_COST", jobRequest.machineCost);
        Document.put("NUM_LOADS_ACTUAL", jobRequest.numLoadsActual);
        Document.put("NUM_LOADS_ESTIMATE", jobRequest.numLoadsEstimate);
        Document.put("REQUEST_TIMESTAMP", jobRequest.requestTimestamp);
        Document.put("WAS_CANCELLED", jobRequest.wasCancelled);

        // Write to the database
        db.collection("dorms").document(jobRequest.dorm).collection("jobsPendingAssignment").document(jobRequest.jobID).set(Document) //had to correct this
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

    // Update a job's status // Error
    private void updateJobStatus(JobRequest jobRequest, int currentStage) {
        // Document Reference
        DocumentReference job = db.collection("jobsInProgress").document(jobRequest.jobID);
        // Update the FireBase object in the database
        job.update("CURRENT_STAGE", currentStage).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    // Update on AssignmentAccept
    private void onAssignmentAccept(final JobRequest jobRequest, Dasher dasher) {
        // Document Reference
        DocumentReference job = db.collection("jobsInProgress").document(jobRequest.jobID);
        // Get the time/date
        Date currentTime = Calendar.getInstance().getTime();
        // Update the FireBase object in the database
        job.update("ASSIGNED_TIMESTAMP", currentTime).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            // If Job was deleted
            public void onSuccess(Void aVoid) {
                // Check to see if the status was update

                // Write to inProgressJobs
                moveToInProgressJobs(jobRequest);
            }// If Job couldn't be deleted
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        // Update the local JobRequest Object
        jobRequest.assignedTimestamp = assignedTimestamp;
    }

    // Move document from dorm collection into inProgressJob collection
    // Move jobs in progress to completed jobs
    public void moveToInProgressJobs(final JobRequest jobRequest){

        // From path
        final DocumentReference fromDoc = db.collection("dorms").
                document(jobRequest.dorm).collection("jobsPendingAssignment")
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
    //Check if there are jobs available
    public void pendingJobsExist(Dasher dasher){ //change this Dasher dasher
        db.collection("dorms").document(dasher.dorm).collection("jobsPendingAssignment").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        delegate.sendPendingJobsExist(true);
                        return;
                    }
                    delegate.sendPendingJobsExist(false);
                    return;
                    //Log.d("TAG", count + "");
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    // Get the oldest Job in the database (NEEDS to be Tested)
     public void getOldestJob(Dasher dasher){ //NEED TO ADD DASHER OBJECT PARAMETER, AND THE OLDEST JOB NEEDS TO COME FROM JOBS PENDING, not in progress job, also that job request. FIRST CHECK BEFOREHAND IF THERE ARE JOBS IN jobsPendingAssignment
        // Orer the database

        JobRequest jobRequest = new JobRequest();
        db.collection("dorms").document(dasher.dorm).collection("jobsPendingAssignment").orderBy("REQUEST_TIMESTAMP", Query.Direction.DESCENDING).limit(1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        queryDocumentSnapshots.toString();
                    }
                });
        //getJob(jobID.toString());
    }

    // get JobID and convert it to a jobRequest object
    void getPendingJob(final String jobID, final JobRequest jobRequest, Dasher dasher){
        // Get document reference
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.collection("dorms").document(dasher.dorm).collection("jobsPendingAssignment").document(jobID);
        // Starting transferring data
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                jobRequest.jobID = jobID;
                jobRequest.customerUID = documentSnapshot.getString("CUSTOMER_UID");
                Double currentStage = documentSnapshot.getDouble("CURRENT_STAGE");
                jobRequest.currentStage = (int) Math.round(currentStage);
                jobRequest.wasCancelled = documentSnapshot.getBoolean("WAS_CANCELLED");
                jobRequest.amountPaid = documentSnapshot.getDouble("AMOUNT_PAID");
                jobRequest.assignedTimestamp = documentSnapshot.getTimestamp("ASSIGNED_TIMESTAMP");
                jobRequest.customerInstructions = documentSnapshot.getString("CUSTOMER_INSTRUCTIONS");
                jobRequest.customerRating = documentSnapshot.getDouble("CUSTOMER_RATING");
                jobRequest.customerReview = documentSnapshot.getString("CUSTOMER_REVIEW");
                jobRequest.dasherName = documentSnapshot.getString("DASHER_NAME");
                jobRequest.dasherRating = documentSnapshot.getDouble("DASHER_RATING");
                jobRequest.dasherUID = documentSnapshot.getString("DASHER_UID");
                jobRequest.dorm = documentSnapshot.getString("DORM");
                jobRequest.dormRoom = documentSnapshot.getString("DORM_ROOM");
                jobRequest.customerName = documentSnapshot.getString("CUSTOMER_NAME");
                jobRequest.machineCost = documentSnapshot.getDouble("MACHINE_COST");
                double numLoadsActual = documentSnapshot.getDouble("NUM_LOADS_ACTUAL");
                jobRequest.numLoadsActual = (int) numLoadsActual;
                double numLoadsEstimate = documentSnapshot.getDouble("NUM_LOADS_ESTIMATE");
                jobRequest.numLoadsEstimate = (int) numLoadsEstimate;
                jobRequest.requestTimestamp = documentSnapshot.getTimestamp("REQUEST_TIMESTAMP");
                delegate.sendJobRequest(jobRequest);
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
