package edu.rutgers.dripndashproject;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class CustomerFirestore {

   public CustomerFirestoreInterface delegate;

    public void getCustomer(final String uid) { //new Customer()
            //final CustomerFirestoreInterface delegate;
            FirebaseFirestore db = FirebaseFirestore.getInstance(); //Initializes DataBase
            DocumentReference customerReference = db.collection("customers").document(uid); //reference to document in firestore
            final Customer customer = new Customer();// creates blank customer object

            customerReference.get() //fills in customer object using information in customer
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            customer.uid = uid;
                            customer.rating = documentSnapshot.getDouble("RATING");
                            customer.firstName  = documentSnapshot.getString("FIRST_NAME");
                            customer.lastName = documentSnapshot.getString("LAST_NAME");
                            customer.email = documentSnapshot.getString("EMAIL");
                            customer.dorm = documentSnapshot.getString("DORM");
                            customer.dormRoom = documentSnapshot.getString("DORM_ROOM");
                            customer.gender = documentSnapshot.getString("GENDER");
                            Double age = documentSnapshot.getDouble("AGE");
                            customer.age = (int) Math.round(age);   //converts the rounded age into an integer
                            customer.completedJobs = (ArrayList) documentSnapshot.get("COMPLETED_JOBS");
                            delegate.sendCustomer(customer);
                        }
                    });
    }


}
