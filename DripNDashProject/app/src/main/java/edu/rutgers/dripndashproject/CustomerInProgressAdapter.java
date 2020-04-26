package edu.rutgers.dripndashproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.grpc.StatusException;


public class CustomerInProgressAdapter extends RecyclerView.Adapter<CustomerInProgressAdapter.CustomerInProgressViewHolder> {
    ArrayList<JobRequest> customerInProgressItems;

    public static class CustomerInProgressViewHolder extends RecyclerView.ViewHolder{
        public ImageView customerCurrentStageInProgressView;
        public TextView customerStatusText, dasherAssigned, assignedTime;

        public CustomerInProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            customerCurrentStageInProgressView = itemView.findViewById(R.id.imageViewCustomer);
            customerStatusText = itemView.findViewById(R.id.statusText);
            assignedTime = itemView.findViewById(R.id.timeAssignedCustomer);
            dasherAssigned = itemView.findViewById(R.id.nameOfDasher);
        }
    }

    public CustomerInProgressAdapter(ArrayList<JobRequest> customerInProgressItems){
        this.customerInProgressItems = customerInProgressItems;
    }

    @NonNull
    @Override
    public CustomerInProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inprogresscutomer_item, parent, false);
        CustomerInProgressViewHolder cipvh = new CustomerInProgressViewHolder(v);
        return cipvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerInProgressViewHolder holder, int position) {
        JobRequest currentItem = customerInProgressItems.get(position);
        currentItem.currentStageImageResource = currentItem.customerImageMap.get(currentItem.currentStage);
        currentItem.customerCurrentStatus = currentItem.customerStages.get(currentItem.currentStage);
        holder.customerCurrentStageInProgressView.setImageResource(currentItem.currentStageImageResource);
        holder.customerStatusText.setText(currentItem.customerCurrentStatus);
        holder.assignedTime.setText(currentItem.requestTimestamp.toDate().toString());
        if(currentItem.dasherName == null){ //if no dasher assigned, leave space blank
            String empty = "";
            holder.dasherAssigned.setText(empty);
        } else{ //dasher gets assigned,
            holder.dasherAssigned.setText(currentItem.dasherName);
        }
    }

    @Override
    public int getItemCount() {
        return customerInProgressItems.size();
    }
}
