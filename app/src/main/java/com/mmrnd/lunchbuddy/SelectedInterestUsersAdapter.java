package com.mmrnd.lunchbuddy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Esteban Sosa
 */

public class SelectedInterestUsersAdapter extends RecyclerView.Adapter<SelectedInterestUsersAdapter.InterestUsersViewHolder> {

    // Variables
    private List<User> users;
    private Context mContext;

    // Interface
    OnSelectedInterestInterface selectedInterestInterface;
    public interface OnSelectedInterestInterface {
        void userClicked(int index);
    }

    // Constructor
    public SelectedInterestUsersAdapter(Context context, List<User> users, OnSelectedInterestInterface mListener) {
        this.users = users;
        this.mContext = context;
        this.selectedInterestInterface = mListener;
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    @NonNull
    @Override
    public InterestUsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.interest_user_item, viewGroup, false);
        return new InterestUsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestUsersViewHolder interestsViewHolder, final int i) {
        // Set user info
        interestsViewHolder.nameTextView.setText(users.get(i).getName());
        int experienceLevel = users.get(i).getExperience();
        if(experienceLevel == DatabaseManager.NOVICE) {
            interestsViewHolder.expertiseTextView.setText("- Novice");
        }
        else if(experienceLevel == DatabaseManager.OK) {
            interestsViewHolder.expertiseTextView.setText("- OK");
        }
        else if(experienceLevel == DatabaseManager.EXPERT) {
            interestsViewHolder.expertiseTextView.setText("- Expert");
        }
        interestsViewHolder.detailsTextView.setText(users.get(i).getDetails());
        interestsViewHolder.imageView.setImageResource(R.mipmap.user_icon);
        if(!users.get(i).getUserPhoto().isEmpty()) {
            Glide.with(mContext).load(users.get(i).getUserPhoto()).into(interestsViewHolder.imageView);
        }

        // Set on click listener
        interestsViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedInterestInterface.userClicked(i);
            }
        });
    }

    // ViewHolder class
    public static class InterestUsersViewHolder extends RecyclerView.ViewHolder {
        // GUI elements
        ConstraintLayout constraintLayout;
        CircleImageView imageView;
        TextView nameTextView;
        TextView expertiseTextView;
        TextView detailsTextView;

        // Constructor
        public InterestUsersViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find GUI elements
            constraintLayout = itemView.findViewById(R.id.interest_user_item_constraintlayout);
            imageView = itemView.findViewById(R.id.interest_user_item_imageview);
            nameTextView = itemView.findViewById(R.id.interest_user_item_name_textview);
            expertiseTextView = itemView.findViewById(R.id.interest_user_item_experience_textview);
            detailsTextView = itemView.findViewById(R.id.interest_user_item_details_textview);
        }

    }
}
