package com.mmrnd.lunchbuddy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Esteban Sosa
 */

public class MyInterestsAdapter extends RecyclerView.Adapter<MyInterestsAdapter.MyInterestsViewHolder> {

    // Variables
    private List<MyInterest> interests;
    private Context mContext;

    // Interface
    OnMyInterestInterface interestInterface;
    public interface OnMyInterestInterface {
        void interestClicked(int index);
    }

    // Constructor
    public MyInterestsAdapter(Context context, List<MyInterest> interests, OnMyInterestInterface mListener) {
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
    public MyInterestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_interest_item, viewGroup, false);
        return new MyInterestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyInterestsViewHolder interestsViewHolder, int i) {
        // Set interest info
        interestsViewHolder.titleTextView.setText(interests.get(i).getTitle());
        if(interests.get(i).getLevel() == DatabaseManager.NOVICE) {
            interestsViewHolder.levelTextView.setText("- Novice");
        }
        else if(interests.get(i).getLevel() == DatabaseManager.OK) {
            interestsViewHolder.levelTextView.setText("- OK");
        }
        else if(interests.get(i).getLevel() == DatabaseManager.EXPERT) {
            interestsViewHolder.levelTextView.setText("- Expert");
        }
        interestsViewHolder.detailsTextView.setText(interests.get(i).getDetails());
    }

    // ViewHolder class
    public static class MyInterestsViewHolder extends RecyclerView.ViewHolder {
        // GUI elements
        TextView titleTextView;
        TextView levelTextView;
        TextView detailsTextView;

        // Constructor
        public MyInterestsViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find GUI elements
            titleTextView = itemView.findViewById(R.id.myinterestitem_title_textview);
            levelTextView = itemView.findViewById(R.id.myinterestitem_level_textview);
            detailsTextView = itemView.findViewById(R.id.myinterestitem_details_textview);
        }

    }
}
