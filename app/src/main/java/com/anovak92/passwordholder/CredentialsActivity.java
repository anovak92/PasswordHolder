package com.anovak92.passwordholder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.anovak92.passwordholder.model.Credentials;
import com.anovak92.passwordholder.model.CredentialsRepo;
import com.anovak92.passwordholder.model.FileCredentialsRepo;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CredentialsActivity extends AppCompatActivity implements CredentialsView {

    @SuppressWarnings("CheckStyle")
    public final static String MODE_KEY = "mode";
    @SuppressWarnings("CheckStyle")
    public final static String ID_KEY = "id";

    private EditText accountInput;
    private EditText passwordInput;
    private CredentialsView.Mode currentMode;
    private FloatingActionButton actionButton;

    private CredentialsRepo credentialsRepo;
    private Credentials currentCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionButton = findViewById(R.id.fab);
        accountInput = findViewById(R.id.account_input);
        passwordInput = findViewById(R.id.password_input);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            File dataFile = new File(getFilesDir(), Preferences.DATA_FILE_NAME);
            credentialsRepo = new FileCredentialsRepo(dataFile);
            int credentialsId = getIntent().getIntExtra(ID_KEY,-1);

            try {
                currentCredentials = getCredentialById(credentialsId);
                setMode(Mode.valueOf(intent.getStringExtra(MODE_KEY)));
            } catch (IOException e) {
                e.printStackTrace();
                finish();
            }
        }
    }

    @Override
    public void setMode(Mode mode) {
        currentMode = mode;
        switch (mode) {
            case CREATE:
            case EDIT:
                makeEditableView();
                break;

            case VIEW:
                accountInput.setEnabled(false);
                passwordInput.setEnabled(false);
                setActionButtonImage(R.drawable.ic_edit_white_24dp);
                actionButton.setOnClickListener(v -> setMode(Mode.EDIT));
                break;

            default:
                throw new RuntimeException("Unknown mode");
        }
        accountInput.setText(currentCredentials.getAccountname());
        passwordInput.setText(currentCredentials.getPassword());
    }

    private void makeEditableView() {
        accountInput.setEnabled(true);
        passwordInput.setEnabled(true);
        setActionButtonImage(R.drawable.ic_done_white_24dp);
        actionButton.setOnClickListener(v -> save());
    }

    private Credentials getCredentialById(int id) throws IOException {
        Credentials credentials = credentialsRepo.loadCredentials().get(id);
        if (credentials == null) {
            credentials = new Credentials(id);
        }

        return credentials;
    }

    private void setActionButtonImage(int id) {
        actionButton.setImageDrawable(getDrawable(id));
    }

    @Override
    public Mode getCurrentMode() {
        return currentMode;
    }

    private boolean validate() {
        return !passwordInput.getText().toString().isEmpty()
                && !accountInput.getText().toString().isEmpty();
    }

    @Override
    public void save() {
        if (!validate()) {
            showErrorSnackBar("Please fill all fields.");
            return;
        }

        try {
            currentCredentials.setAccountname(accountInput.getText().toString());
            currentCredentials.setPassword(passwordInput.getText().toString());

            Map<Integer, Credentials> credentialsMap = credentialsRepo.loadCredentials();
            credentialsMap.put(currentCredentials.getId(),currentCredentials);
            credentialsRepo.saveCredentials(credentialsMap);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            setMode(Mode.VIEW);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorSnackBar("Please fill all fields.");
        }

    }

    private void showErrorSnackBar(String errMessage) {
        Snackbar.make(passwordInput, errMessage, Toast.LENGTH_SHORT).show();
    }
}
