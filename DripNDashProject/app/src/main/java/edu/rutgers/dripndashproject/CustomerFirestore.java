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

   /* private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextDorm;
    private EditText editTextDormRoom;
    private EditText editTextGender;
    private EditText editTextUID;
    private EditText editTextAGE;
*/
    FirebaseAuth firebaseAuthorizer;
    String uid = firebaseAuthorizer.getCurrentUser().getUid();
    public Customer getCustomer()
    {
        /*String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String email = editTextEmail.getText().toString();
        String dorm = editTextDorm.getText().toString();
        String dormRoom = editTextDormRoom.getText().toString();
        String gender = editTextGender.getText().toString();
        String uid = editTextUID.getText().toString();
        int age = Integer.valueOf(editTextAGE.getText().toString());*/

        final DocumentReference customerRef = db.collection("customers").document(uid);

        //Customer customer = new Customer(firstName,lastName,email,dorm,dormRoom,gender,uid,age);
        Customer customer = new Customer();
        customerRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Customer customer = documentSnapshot.toObject(Customer.class);
                            String firstName = customer.getFirstName();
                            String lastName = customer.getLastName();
                            String email = customer.getEmail();
                            String dorm = customer.getDorm();
                            String dormRoom = customer.getDormRoom();
                            String gender = customer.getGender();
                            int age = customer.getAge();

                        } else {
                            //Error if Document does not exist. Could be redundant if documents are assumed to be valid already.
                            Toast.makeText(CustomerJobStatusActivity.this, "Document does not Exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CustomerJobStatusActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return customer;
    }


}
