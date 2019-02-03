package com.anovak92.passwordholder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.anovak92.passwordholder.model.Credentials;
import com.anovak92.passwordholder.model.CredentialsRepo;
import com.anovak92.passwordholder.model.FileCredentialsRepo;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private RecyclerView contentView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private CredentialsAdapter.Callback callback = new CredentialsAdapter.Callback() {
        @Override
        public void view(int id) {
            viewCredential(id);
        }

        @Override
        public void delete(int id) {
            deleteCredential(id);
        }
    };

    private CredentialsRepo credentialsRepo;
    private List<Credentials> credentialsDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contentView = findViewById(R.id.content_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        contentView.setLayoutManager(mLayoutManager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> addCredentials());

        File dataFile = new File(getFilesDir(), Preferences.DATA_FILE_NAME);
        if (!dataFile.exists()) {
            try {
                boolean created = dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                String errMessage = "Failed to create file for saving credentials. "
                         + "Please restart the app";
                showErrorSnackBar(errMessage);
            }
        }

        credentialsRepo = new FileCredentialsRepo(dataFile);
        try {
            credentialsDataset = credentialsRepo.loadCredentialsList();
            mAdapter = new CredentialsAdapter(credentialsDataset,callback);
            contentView.setAdapter(mAdapter);
        } catch (IOException e) {
            e.printStackTrace();
            String toastText = "Something gone wrong while loading credentials."
                    + "Please restart the app";
            showErrorSnackBar(toastText);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCredentials();
    }

    private void updateCredentials() {
        try {
            credentialsDataset = credentialsRepo.loadCredentialsList();
            mAdapter = new CredentialsAdapter(credentialsDataset, callback);
            contentView.swapAdapter(mAdapter, false);
        } catch (IOException e) {
            e.printStackTrace();
            String toastText = "Something gone wrong while loading credentials."
                    + "Please restart the app";
            showErrorSnackBar(toastText);
        }
    }

    private void addCredentials() {
        Intent addCredentialsIntent = new Intent(this, CredentialsActivity.class);
        addCredentialsIntent
                .putExtra(CredentialsActivity.MODE_KEY,CredentialsActivity.Mode.CREATE.toString())
                .putExtra(CredentialsActivity.ID_KEY, getFreeId());
        startActivity(addCredentialsIntent);
    }

    private int getFreeId() {
        Set<Integer> usedIds = new HashSet<>(credentialsDataset.size());
        Set<Integer> rangeIds = new HashSet<>(credentialsDataset.size() + 1);
        for (Credentials credentials: credentialsDataset) {
            usedIds.add(credentials.getId());
        }
        for (int i = 0; i <= credentialsDataset.size(); i++) {
            rangeIds.add(i);
        }
        rangeIds.removeAll(usedIds);
        return rangeIds.iterator().next();
    }

    private void viewCredential(int id) {
        Intent editCredentialsIntent = new Intent(this, CredentialsActivity.class);
        editCredentialsIntent
                .putExtra(CredentialsActivity.MODE_KEY,CredentialsActivity.Mode.VIEW.toString())
                .putExtra(CredentialsActivity.ID_KEY, id);
        startActivity(editCredentialsIntent);
    }

    private void showErrorSnackBar(String errMessage) {
        Snackbar.make(contentView, errMessage, Toast.LENGTH_SHORT).show();
    }

    private void deleteCredential(int position) {
        credentialsDataset.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            credentialsRepo.saveCredentialsList(credentialsDataset);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
