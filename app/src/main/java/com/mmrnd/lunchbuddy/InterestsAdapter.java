package com.mmrnd.lunchbuddy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

    public void updateList(List<String> newInterests) {
        interests = new ArrayList<>();
        interests.addAll(newInterests);
        notifyDataSetChanged();
    }

    public List<String> getInterests() {
        return interests;
    }

    @NonNull
    @Override
    public InterestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.interest_item, viewGroup, false);
        return new InterestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestsViewHolder interestsViewHolder, final int i) {
        // Set interest info
        interestsViewHolder.textView.setText(interests.get(i));

        // Set on click listener
        interestsViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interestInterface.interestClicked(i);
            }
        });
    }

    // ViewHolder class
    public static class InterestsViewHolder extends RecyclerView.ViewHolder {
        // GUI elements
        ConstraintLayout constraintLayout;
        TextView textView;

        // Constructor
        public InterestsViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find GUI elements
            constraintLayout = itemView.findViewById(R.id.interest_item_constraintlayout);
            textView = itemView.findViewById(R.id.interest_item_textview);
        }

    }
}
