package com.anovak92.passwordholder;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.anovak92.passwordholder.model.Credentials;
import com.anovak92.passwordholder.model.FileCredentialsRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends MainActivityBase {

    private RecyclerView.Adapter mAdapter;

    private CredentialsAdapter.Callback callback = new CredentialsAdapter.Callback() {
        @Override
        public void view(int id) {
            CredentialsActivity.startView(MainActivity.this, id);
        }

        @Override
        public void delete(int position) {
            deleteCredential(position);
        }
    };

    private FileCredentialsRepository repository;
    private List<Credentials> credentialsDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File dataFile = new File(getFilesDir(), Preferences.DATA_FILE_NAME);
        try {
            FileCredentialsRepository.setFile(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
            String errMessage = "Failed to create file for saving credentials. "
                    + "Please restart the app";
            showErrorSnackBar(errMessage);
        }
        repository = FileCredentialsRepository.getInstance();
        try {
            repository.loadDataset();
        } catch (IOException e) {
            e.printStackTrace();
            String toastText = "Something gone wrong while loading credentials."
                    + "Please restart the app";
            showErrorSnackBar(toastText);
        }

        credentialsDataset = repository.getDataset();
        mAdapter = new CredentialsAdapter(credentialsDataset, callback);
        recyclerView.setAdapter(mAdapter);
    }

    private void showErrorSnackBar(String errMessage) {
        Snackbar.make(recyclerView, errMessage, Toast.LENGTH_SHORT).show();
    }

    private void deleteCredential(int position) {
        credentialsDataset.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            repository.saveDataset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
