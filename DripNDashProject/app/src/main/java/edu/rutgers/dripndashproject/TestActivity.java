package edu.rutgers.dripndashproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity implements JobRequestFirestoreInterface{

    //initialize buttons here
    Button pendingJobsInLivi;
    private JobRequest jobRequest;
    private Boolean pendingJobsExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final JobRequestFireStore jobRequestFireStore = new JobRequestFireStore();
        jobRequestFireStore.delegate = this;
        pendingJobsInLivi = findViewById(R.id.pending_jobs_in_livi);
        pendingJobsInLivi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //jobRequestFireStore.pendingJobsExist(Livingston Apartments);
            }
        });
    }

    @Override
    public void sendJobRequest(JobRequest jobRequest) {
        this.jobRequest = jobRequest;
    }

    @Override
    public void sendPendingJobsExist(Boolean pendingJobsExist) {
        //this.pendingJobsExist = pendingJobsExist;
        if(pendingJobsExist){
            Toast.makeText(this, "Pending Job exists", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "Pending Job does not exist", Toast.LENGTH_SHORT).show();
        }
    }
}
