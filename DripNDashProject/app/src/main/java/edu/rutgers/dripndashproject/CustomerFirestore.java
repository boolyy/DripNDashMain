package edu.rutgers.dripndashproject;

import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class CustomerFirestore {

    public Customer getCustomer(final String uid) {

        FirebaseFirestore db = FirebaseFirestore.getInstance(); //initialize database
        final Customer customer = new Customer(); //creates new customer object to be returned

        db.collection("customers").document(uid).get() //gets customer's document
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() { //start listener
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) { //runs on success
                        customer.uid = uid;
                        customer.firstName = documentSnapshot.getString("FIRST_NAME");
                        customer.lastName = documentSnapshot.getString("LAST_NAME");
                        customer.email = documentSnapshot.getString("EMAIL");
                        customer.dorm = documentSnapshot.getString("DORM");
                        customer.dormRoom = documentSnapshot.getString("DORM_ROOM");
                        customer.gender = documentSnapshot.getString("GENDER");
                        Double age = documentSnapshot.getDouble("AGE");
                        customer.age = (int) Math.round(age); //converts double to int since one can only retrieve double from firestore
                        customer.completedJobs = (ArrayList) documentSnapshot.get("COMPLETED_JOBS"); //converts object from firestore to arraylist
                    }
                });

        return customer;
    }


}
