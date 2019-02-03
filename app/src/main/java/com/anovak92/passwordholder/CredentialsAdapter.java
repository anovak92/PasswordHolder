package com.anovak92.passwordholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anovak92.passwordholder.model.Credentials;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CredentialsAdapter extends
        RecyclerView.Adapter<CredentialsAdapter.CredentialsViewHolder> {

    private List<Credentials> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class CredentialsViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView accountView;
        TextView usernameView;
        TextView passwordView;
        Button deleteButton;

        CredentialsViewHolder(LinearLayout itemView) {
            super(itemView);

            this.accountView = (TextView) itemView.getChildAt(0);
            this.usernameView = (TextView) itemView.getChildAt(1);
            this.passwordView = (TextView) itemView.getChildAt(2);
            this.deleteButton = (Button) ((FrameLayout)itemView.getChildAt(3)).getChildAt(0);
        }
    }


    CredentialsAdapter(Map<Integer, Credentials> credentialsDataset) {
        mDataset = new ArrayList<>(credentialsDataset.values());
    }

    @NonNull
    @Override
    public CredentialsAdapter.CredentialsViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                       int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.credentials_row, parent, false);

        return new CredentialsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CredentialsViewHolder holder, int position) {
        Credentials element = mDataset.get(position);
        holder.usernameView.setText(element.getAccountName());
        holder.passwordView.setText(element.getPassword());

        holder.deleteButton.setOnClickListener(v -> {
            mDataset.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}