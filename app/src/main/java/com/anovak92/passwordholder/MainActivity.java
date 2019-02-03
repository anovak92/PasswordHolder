package com.anovak92.passwordholder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.anovak92.passwordholder.model.Credentials;
import com.anovak92.passwordholder.model.CredentialsRepo;
import com.anovak92.passwordholder.model.FileCredentialsRepo;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView contentView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private CredentialsRepo credentialsRepo;
    private Map<Integer, Credentials> credentialsMap;

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
            credentialsMap = credentialsRepo.loadCredentials();
            mAdapter = new CredentialsAdapter(credentialsMap);
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
    }

    private void displayCredentials() {
        for (Credentials credential: credentialsMap.values()) {
            TextView tw = new TextView(this);
            tw.setText(String.format(Locale.US,"%s : %s",
                    credential.getAccountName(),
                    credential.getPassword()
            ));
            tw.setTag(credential.getId());
            tw.setTextSize(24f);
            tw.setTextColor(Color.BLACK);
            tw.setOnClickListener(v -> editCredential((Integer) v.getTag()));
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
        for (int i = 0; i <= credentialsMap.keySet().size(); i++) {
            if (!credentialsMap.keySet().contains(i)) {
                return i;
            }
        }
        throw new RuntimeException("My math sucks");
    }

    private void editCredential(int id) {
        Intent editCredentialsIntent = new Intent(this, CredentialsActivity.class);
        editCredentialsIntent
                .putExtra(CredentialsActivity.MODE_KEY,CredentialsActivity.Mode.EDIT.toString())
                .putExtra(CredentialsActivity.ID_KEY, id);
        startActivity(editCredentialsIntent);
    }

    private void showErrorSnackBar(String errMessage) {
        Snackbar.make(contentView, errMessage, Toast.LENGTH_SHORT).show();
    }

}
