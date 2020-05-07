package edu.rutgers.dripndashproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;


public class CustomerHomeFragment extends Fragment implements CustomerRequestDialog.CustomerRequestDialogListener, CustomerFirestoreInterface{
    Customer customer; //initialize customer object

    ArrayList<JobRequest> inProgressItemsCustomers; //makes job request array list

    //recyclerView stuff
    private RecyclerView customerInProgressRecyclerView;
    private RecyclerView.Adapter customerInProgressAdapter;
    private RecyclerView.LayoutManager customerInProgressLayoutManager;

    //initialize button
    Button requestJobButton;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser(); //create user object

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        firebaseAuth = FirebaseAuth.getInstance();
        View v = inflater.inflate(R.layout.fragment_customer_home,container,false); //initialize view

        //methods that create recycler view
        //createDasherInProgressItemsList();  THINK THAT I DO NOT NEED THIS ONE
        inProgressItemsCustomers = new ArrayList<>(); //makes array list
        buildCustomerInProgressRecyclerView(v); //builds recycler view

        //creates customer object
        CustomerFirestore customerFirestore = new CustomerFirestore(); //initialize interface
        customerFirestore.delegate = this;
        customerFirestore.getCustomer(user.getUid());
        requestJobButton = v.findViewById(R.id.buttonCustomerRequest); //assign requestJobButton to proper button id in layout

        requestJobButton.setOnClickListener(new View.OnClickListener(){ //initialize listener for request job button
            @Override
            public void onClick(View v) { //runs when button is clicked
                //open a card dialog, put in preferences, how many loads, Edit text, submit button
                openDialog();
            }
        });


        return v;
    }

    public void openDialog() {
        CustomerRequestDialog customerRequestDialog = new CustomerRequestDialog();
        customerRequestDialog.setTargetFragment(CustomerHomeFragment.this, 123);
        customerRequestDialog.show(getFragmentManager(), "customer dialog");
    }

    public void buildCustomerInProgressRecyclerView(View v){
        customerInProgressRecyclerView = v.findViewById(R.id.recyclerViewCustomer);
        customerInProgressRecyclerView.setHasFixedSize(true);
        customerInProgressLayoutManager = new LinearLayoutManager(getContext());
        customerInProgressAdapter = new CustomerInProgressAdapter(inProgressItemsCustomers);

        customerInProgressRecyclerView.setLayoutManager(customerInProgressLayoutManager);
        customerInProgressRecyclerView.setAdapter(customerInProgressAdapter);
    }

    @Override
    public void createJob(String numLoadsEstimate, String customerInstructions) { //creates job from info that user has input
        if(numLoadsEstimate.isEmpty()){  //checks if num Loads estimate is filled in
            Toast.makeText(getActivity(), "Must enter number of loads", Toast.LENGTH_SHORT).show();
        }else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            //creates job request object
            final JobRequest jobRequest = new JobRequest(UUID.randomUUID().toString(), user.getUid(), Timestamp.now(), customer.firstName + " " + customer.lastName, customer.dorm, customer.dormRoom, customerInstructions, Integer.parseInt(numLoadsEstimate), customer.rating);
            //creates job request firestore object
            JobRequestFireStore jobRequestFireStore = new JobRequestFireStore();
            jobRequestFireStore.writeJobRequest(jobRequest); //writes job request to firebase
            inProgressItemsCustomers.add(jobRequest); //adds it to Array list for recycler view
            customerInProgressAdapter.notifyItemInserted(inProgressItemsCustomers.size()); //lets adapter know that item has been inserted at a certain index

            final CollectionReference inProgressJobs = db.collection("jobsInProgress");
            //make listener array list
            inProgressJobs.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        if (documentSnapshot.getId().equals(jobRequest.jobID)){
                            inProgressItemsCustomers.get(inProgressItemsCustomers.indexOf(jobRequest)).currentStage++;
                            inProgressItemsCustomers.get(inProgressItemsCustomers.indexOf(jobRequest)).dasherName = documentSnapshot.getString("DASHER_NAME");
                            customerInProgressAdapter.notifyItemChanged(inProgressItemsCustomers.indexOf(jobRequest));
                            return;
                        }
                    }
                }
            });

            //have to add this to job request
            //create snapshot listener for job id of job request
        }
    }

    @Override
    public void sendCustomer(Customer customer) {
        this.customer = customer;
        //Toast.makeText(getActivity(), customer.lastName, Toast.LENGTH_SHORT).show(); // just a test to make sure that getCustomer is working correctly
    }


}