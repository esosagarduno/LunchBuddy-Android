package com.mmrnd.lunchbuddy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.InterestsViewHolder> {

    // Variables
    private List<String> interests;
    private Context mContext;

    // Interface
    OnInterestInterface interestInterface;
    public interface OnInterestInterface {
        void interestClicked(int index);
    }

    // Constructor
    public InterestsAdapter(Context context, List<String> interests, OnInterestInterface mListener) {
        this.interests = interests;
        this.mContext = context;
        this.interestInterface = mListener;
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    @NonNull
    @Override
    public InterestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.interest_item, viewGroup, false);
        return new InterestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestsViewHolder interestsViewHolder, int i) {
        // Set interest info
        interestsViewHolder.textView.setText(interests.get(i));
    }

    // ViewHolder class
    public static class InterestsViewHolder extends RecyclerView.ViewHolder {
        // GUI elements
        TextView textView;

        // Constructor
        public InterestsViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find GUI elements
            textView = itemView.findViewById(R.id.interest_item_textview);
        }

    }
}
