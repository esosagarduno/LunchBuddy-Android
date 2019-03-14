package com.mmrnd.lunchbuddy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Esteban Sosa
 */

public class UsersInterestsAdapter extends RecyclerView.Adapter<UsersInterestsAdapter.UsersInterestsViewHolder>{


    // Variables
    private List<MyInterest> interests;
    private Context mContext;

    // Interface
    OnUsersInterestInterface interestInterface;
    public interface OnUsersInterestInterface {
        void interestClicked(int index);
    }

    // Constructor
    public UsersInterestsAdapter(Context context, List<MyInterest> interests, OnUsersInterestInterface mListener) {
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
    public UsersInterestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_interest_item, viewGroup, false);
        return new UsersInterestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersInterestsViewHolder interestsViewHolder, final int i) {
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
        // Set click listener
        interestsViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interestInterface.interestClicked(i);
            }
        });
    }

    // ViewHolder class
    public static class UsersInterestsViewHolder extends RecyclerView.ViewHolder {
        // GUI elements
        ConstraintLayout constraintLayout;
        TextView titleTextView;
        TextView levelTextView;

        // Constructor
        public UsersInterestsViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find GUI elements
            constraintLayout = itemView.findViewById(R.id.users_interest_item_constraintlayout);
            titleTextView = itemView.findViewById(R.id.users_interest_item_title_textview);
            levelTextView = itemView.findViewById(R.id.users_interest_item_level_textview);
        }

    }
}
