package edu.rutgers.dripndashproject;

public interface JobRequestFirestoreInterface {
    public void sendJobRequest(JobRequest jobRequest);
    public void sendPendingJobsExist(Boolean pendingJobsExist);
}
