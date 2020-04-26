package edu.rutgers.dripndashproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.CollationElementIterator;
import java.util.ArrayList;

public class DasherHomeFragment extends Fragment implements DasherFirestoreInterface, JobRequestFirestoreInterface{
    ArrayList<JobRequest> inProgressItemsDashers; //creates array list for recyclerview

    Dasher dasher = new Dasher();
    private RecyclerView mRecyclerView;
    private DasherInProgressItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //initialize button
    Button requestJobButton;
    FirebaseFirestore db;
    //initialize text fields
    final JobRequestFireStore jobRequestFireStore = new JobRequestFireStore();
    JobRequest jobRequest = new JobRequest();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        View v = inflater.inflate(R.layout.fragment_home,container,false); //initialize view

        inProgressItemsDashers = new ArrayList<>();


            //JobRequestFireStore jobRequestFireStore = new JobRequestFireStore();
            //jobRequestFireStore.delegate = this;  CHECK THIS
            //jobRequestFireStore.getJob(dasher);

        //createDasherInProgressItemsList();
        buildDasherInProgressRecyclerView(v);

        DasherFirestore dasherFirestore = new DasherFirestore();
        dasherFirestore.delegate = this;
        dasherFirestore.getDasher(user.getUid());
        jobRequestFireStore.delegate = this;

        requestJobButton = v.findViewById(R.id.buttonRequest); //assign requestJobButton to proper button id in layout

        requestJobButton.setOnClickListener(new View.OnClickListener(){ //initialize listener for request job button
            @Override
            public void onClick(View v) { //runs when button is clicked
                jobRequestFireStore.pendingJobsExist(dasher);
            }
        });

        return v;
    }

    public void openDasherJobAcceptDialog(){ //opens dialog for system find job
        DasherJobAcceptDialog dasherJobAcceptDialog = new DasherJobAcceptDialog();
        assert getFragmentManager() != null;
        dasherJobAcceptDialog.show(getFragmentManager(), "example dialog");
    }

    public void openNoJobsDialog(){ //opens dialog for when system does not find job 
        DasherNoJobsDialog dasherNoJobsDialog = new DasherNoJobsDialog();
        dasherNoJobsDialog.show(getChildFragmentManager(), "example dialog");
    }

    //public void createDasherInProgressItemsList(){
        //inProgressItems = new ArrayList<>();
       // inProgressItems.add(new DasherInProgressItem(R.drawable.ic_timer_black_24dp, "Line 1", "Line 2"));
        //inProgressItems.add(new DasherInProgressItem(R.drawable.ic_directions_run_black_24dp, "Line 3", "Line 4"));
    //}

    public void buildDasherInProgressRecyclerView(View v){
        mRecyclerView = v.findViewById(R.id.recyclerViewDasher);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new DasherInProgressItemAdapter(inProgressItemsDashers);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new DasherInProgressItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //inProgressItemsDashers.get(position).changeText1("Clicked");
            }
        });
    }


    @Override
    public void sendDasher(Dasher dasher) { //interface for getting Dasher
        this.dasher = dasher;
        //Toast.makeText(getActivity(), dasher.dorm, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendJobRequest(final JobRequest jobRequest) {
        this.jobRequest = jobRequest;
        Toast.makeText(getActivity(),jobRequest.customerInstructions, Toast.LENGTH_SHORT).show();
        //add alert box
        AlertDialog.Builder jobFound = new AlertDialog.Builder(getContext());
        jobFound.setTitle("Job Found! Would you like to accept it?");
        jobFound.setMessage("Customer Name: " + jobRequest.customerName +"\nEstimated Number of Loads: " + jobRequest.numLoadsEstimate + "\nSpecial Instructions: " + jobRequest.customerInstructions);
        jobFound.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                //update on assignment accept

                //update jobRequest object first
                jobRequest.assignedTimestamp = Timestamp.now();
                jobRequest.dasherUID = dasher.uid;
                jobRequest.dasherName = dasher.firstName + " " + dasher.lastName;
                jobRequest.currentStage = 1;
                jobRequest.dasherRating = dasher.rating;

                //update document in firebase
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference jobDoc = db.collection("dorms").document(dasher.dorm).collection("jobsPendingAssignment").document(jobRequest.jobID);
                jobDoc.update("ASSIGNED_TIMESTAMP", jobRequest.assignedTimestamp, "DASHER_UID", jobRequest.dasherUID, "DASHER_NAME", jobRequest.dasherName, "CURRENT_STAGE", jobRequest.currentStage, "DASHER_RATING", dasher.rating)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){ //if successful, add jobRequest to list
                                    inProgressItemsDashers.add(jobRequest);
                                    mAdapter.notifyItemInserted(inProgressItemsDashers.size());
                                    jobRequestFireStore.moveToInProgressJobs(jobRequest);
                                }else{
                                    Toast.makeText(getActivity(), "Error in Job Document Update", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        jobFound.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //maybe opens another alert dialog box that will lead to another job
            }
        });
        jobFound.show();
    }

    @Override
    public void sendPendingJobsExist(Boolean pendingJobsExist) {
        if(pendingJobsExist){ //oldest job exists
            Toast.makeText(getActivity(), "Jobs Exist", Toast.LENGTH_SHORT).show();
            //JobRequest jobRequest = jobRequestFireStore.getOldestJob(dasher);
            //Toast.makeText(getActivity(), jobRequest.customerInstructions, Toast.LENGTH_SHORT).show();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("dorms").document(dasher.dorm).collection("jobsPendingAssignment").orderBy("REQUEST_TIMESTAMP", Query.Direction.ASCENDING).limit(1).get() //gets oldest job
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                Toast.makeText(getActivity(), documentSnapshot.getId(), Toast.LENGTH_SHORT).show();
                                jobRequestFireStore.getPendingJob(documentSnapshot.getId(), jobRequest, dasher);
                            }
                        }
                    });
        } else{
            Toast.makeText(getActivity(), "Job does not exist", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder noJobsAlert = new AlertDialog.Builder(getContext());
            noJobsAlert.setTitle("No Jobs Found");
            noJobsAlert.setMessage("Please try again later");
            noJobsAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which){
                    //just leave blank, should go back to original screen
                }
            });
            noJobsAlert.show();
        }
    }
}