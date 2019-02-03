package com.anovak92.passwordholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anovak92.passwordholder.model.Credentials;

import java.util.List;

public class CredentialsAdapter extends
        RecyclerView.Adapter<CredentialsAdapter.CredentialsViewHolder> {

    public interface Callback {
        void view(int id);

        void delete(int position);
    }

    private Callback callback;
    private List<Credentials> mDataset;

    static class CredentialsViewHolder extends RecyclerView.ViewHolder {
        TextView accountView;
        TextView usernameView;
        TextView passwordView;
        Button deleteButton;

        CredentialsViewHolder(LinearLayout itemView) {
            super(itemView);

            this.accountView = (TextView) itemView.getChildAt(0);
            this.usernameView = (TextView) itemView.getChildAt(1);
            this.passwordView = (TextView) itemView.getChildAt(2);
            this.deleteButton = (Button) itemView.getChildAt(3);
        }
    }


    CredentialsAdapter(List<Credentials> credentialsDataset, Callback callback) {
        this.callback = callback;
        this.mDataset = credentialsDataset;
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

        holder.deleteButton.setOnClickListener(v -> callback.delete(position));
        holder.itemView.setOnClickListener(v -> callback.view(element.getId()));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}