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
                        Toast.makeText(CustomerHomeActivity.this,
                                "Created", Toast.LENGTH_SHORT).show();
                    }
                }) // Checks to see if it failed
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Show Toast
                Toast.makeText(CustomerHomeActivity.this,
                        "Created", Toast.LENGTH_SHORT).show();;
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
                Toast.makeText(CustomerHomeActivity.this,
                        "Job Deleted", Toast.LENGTH_SHORT).show();
            }// If Job couldn't be deleted
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerHomeActivity.this,
                        "Error!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });

    }

    // Update a job's status // Error
    private void updateJobStatus(JobRequest jobRequest, int CurrentStage) {
        // Document Reference
        DocumentReference job = db.collection("jobsInProgress").document(jobRequest.jobID);
        // Update the FireBase object in the database
        job.update("CURRENT_STAGE", currentStage).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            // If Job was deleted
            public void onSuccess(Void aVoid) {
                // Check to see if the status was update
                Toast.makeText(CustomerHomeActivity.this,
                        "Job Status Updated", Toast.LENGTH_SHORT).show();
            }// If Job couldn't be deleted
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerHomeActivity.this,
                        "Error Updating the Job Status!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });

        // Update the local JobRequest Object
        jobRequest.currentStage = currentStage;
    }

    // Update on AssignmentAccept
    private void onAssignmentAccept(JobRequest jobRequest) {
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
                Toast.makeText(CustomerHomeActivity.this,
                        "Job has been Accepted.", Toast.LENGTH_SHORT).show();
            }// If Job couldn't be deleted
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerHomeActivity.this,
                        "Error! Unable to accept Job.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });

        // Update the local JobRequest Object
        jobRequest.assignedTimestamp = assignedTimestamp;
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

    // Get the oldest Job in the database (NEEDS to be Tested)
    private JobRequest getOldestJob(JobRequest jobRequest){

        // Order the database
        Task<QuerySnapshot> jobID = db.collection("jobsInProgress")
                .orderBy("REQUEST_TIMESTAMP", Query.Direction.DESCENDING)
                .limit(1).get();

            jobRequest.jobID = jobID.toString();

        return jobRequest;

    }

    // getInProgressJob and convert it to a jobRequest object
    JobRequest getInProgressJob(final JobRequest jobRequest){

        // Create new JobRequest instance
        final JobRequest inProgressJobRequest = new JobRequest();

        // Get document reference
        DocumentReference documentReference = db.collection("jobsInProgress").document(jobRequest.jobID);

        // Starting transferring data
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                inProgressJobRequest.jobID = jobID;
                inProgressJobRequest.customerUID = customerUID;
                inProgressJobRequest.currentStage = currentStage;
                inProgressJobRequest.Status = Status;
                inProgressJobRequest.wasCancelled = wasCancelled;
                inProgressJobRequest.amountPaid = amountPaid;
                inProgressJobRequest.assignedTimestamp = assignedTimestamp;
                inProgressJobRequest.customerInstructions = customerInstructions;
                inProgressJobRequest.customerRating = customerRating;
                inProgressJobRequest.customerReview = customerReview;
                inProgressJobRequest.dasherName = dasherName;
                inProgressJobRequest.dasherRating = dasherRating;
                inProgressJobRequest.dasherUID = dasherUID;
                inProgressJobRequest.dorm = dorm;
                inProgressJobRequest.dormRoom = dormRoom;
                inProgressJobRequest.customerName = customerName;
                inProgressJobRequest.machineCost = machineCost;
                inProgressJobRequest.numLoadsActual = numLoadsActual;
                inProgressJobRequest.numLoadsEstimate = numLoadsEstimate;
                inProgressJobRequest.requestTimestamp = requestTimestamp;

            }
        });

        // Return inProgressJobRequest instance
        return inProgressJobRequest;
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
