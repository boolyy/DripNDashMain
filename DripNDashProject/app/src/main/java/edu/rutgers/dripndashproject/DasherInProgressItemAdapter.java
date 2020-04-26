package edu.rutgers.dripndashproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class DasherInProgressItemAdapter extends RecyclerView.Adapter<DasherInProgressItemAdapter.DasherInProgressItemsViewHolder> {
    private ArrayList<JobRequest> dasherInProgressItemArrayList;
    private OnItemClickListener DasherInProgressListener; //listener that checks for when item has been clicked

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        DasherInProgressListener = listener;
    }

    public static class DasherInProgressItemsViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageViewDasher;
        public TextView dasherStatusText, customerAssigned, assignedTime, customerRoomText;

        public DasherInProgressItemsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageViewDasher = itemView.findViewById(R.id.imageViewDasher);
            dasherStatusText = itemView.findViewById(R.id.statusTextDasher);
            customerAssigned = itemView.findViewById(R.id.customerNameText);
            assignedTime = itemView.findViewById(R.id.timeAssignedDasher);
            customerRoomText = itemView.findViewById(R.id.textCustomerRoom);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public DasherInProgressItemAdapter(ArrayList<JobRequest> dasherInProgressItems){
        dasherInProgressItemArrayList = dasherInProgressItems;
    }

    @NonNull
    @Override
    public DasherInProgressItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inprogressdasher_item, parent, false);
        DasherInProgressItemsViewHolder dipivh = new DasherInProgressItemsViewHolder(v, DasherInProgressListener);
        return dipivh;
    }

    @Override
    public void onBindViewHolder(@NonNull DasherInProgressItemsViewHolder holder, int position) {
        JobRequest currentItem = dasherInProgressItemArrayList.get(position);
        currentItem.currentStageImageResource = currentItem.customerImageMap.get(currentItem.currentStage);
        currentItem.dasherCurrentStatus = currentItem.dasherStages.get(currentItem.currentStage);
        holder.imageViewDasher.setImageResource(currentItem.currentStageImageResource);
        holder.dasherStatusText.setText(currentItem.dasherCurrentStatus);
        holder.customerAssigned.setText(currentItem.customerName);
        holder.assignedTime.setText(currentItem.assignedTimestamp.toDate().toString());
        String roomNumber = "Room " + currentItem.dormRoom;
        holder.customerRoomText.setText(roomNumber);
    }

    @Override
    public int getItemCount() {
        return dasherInProgressItemArrayList.size();
    }
}
