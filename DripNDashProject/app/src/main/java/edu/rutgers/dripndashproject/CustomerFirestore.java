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

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String DORM = "dorm";
    private static final String DORM_ROOM = "dormRoom";
    private static final String GENDER = "gender";
    private static final String UID = "uid";
    private static final int AGE = 0;


    FirebaseAuth firebaseAuthorizer;
    String uid = firebaseAuthorizer.getCurrentUser().getUid();
    public Customer getCustomer(final String uid)
    {
            FirebaseFirestore db = FirebaseFirestore.getInstance(); //Initializes DataBase
            final Customer customer = new Customer();                //Creates new Customer object to be returned.

            DocumentReference customerReference = db.collection("customers").document(uid);

            customerReference.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            customer.uid = uid;
                            customer.firstName  = documentSnapshot.getString("FIRST_NAME");
                            customer.lastName = documentSnapshot.getString("LAST_NAME");
                            customer.email = documentSnapshot.getString("EMAIL");
                            customer.dorm = documentSnapshot.getString("DORM");
                            customer.dormRoom = documentSnapshot.getString("DORM_ROOM");
                            customer.gender = documentSnapshot.getString("GENDER");

                            Double age = documentSnapshot.getDouble("AGE");
                            customer.age = (int) Math.round(age);   //converts the rounded age into an integer
                            customer.completedJobs = (ArrayList) documentSnapshot.get("COMPLETED_JOBS");

                        }
                    });

            return customer;
    }


}
