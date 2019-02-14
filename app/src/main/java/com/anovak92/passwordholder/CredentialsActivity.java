package com.anovak92.passwordholder;

import android.content.Context;
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
import com.anovak92.passwordholder.model.FileCredentialsRepository;

import java.io.IOException;
import java.util.List;

public class CredentialsActivity extends AppCompatActivity implements CredentialsView {

    @SuppressWarnings("CheckStyle")
    public final static String MODE_KEY = "mode";
    @SuppressWarnings("CheckStyle")
    public final static String ID_KEY = "id";

    public static void startView(Context context, int position) {
        Intent starter = new Intent(context, CredentialsActivity.class);
        starter.putExtra(CredentialsActivity.MODE_KEY,CredentialsActivity.Mode.VIEW.toString())
                .putExtra(CredentialsActivity.ID_KEY, position);
        context.startActivity(starter);
    }

    public static void startCreate(Context context) {
        Intent starter = new Intent(context, CredentialsActivity.class);
        starter.putExtra(CredentialsActivity.MODE_KEY,CredentialsActivity.Mode.CREATE.toString());
        context.startActivity(starter);
    }

    private EditText accountInput;
    private EditText usernameInput;
    private EditText passwordInput;
    private CredentialsView.Mode currentMode;
    private FloatingActionButton actionButton;
    private Toolbar toolbar;
    private Credentials currentCredentials;

    private FileCredentialsRepository repository;
    private List<Credentials> dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        setupViews();

        repository = FileCredentialsRepository.getInstance();
        dataset = repository.getDataset();
        currentMode = Mode.valueOf(intent.getStringExtra(MODE_KEY));
        currentCredentials = getCurrentCredentials(currentMode);
        setMode(Mode.valueOf(intent.getStringExtra(MODE_KEY)));
    }


    private void setupViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionButton = findViewById(R.id.fab);

        accountInput = findViewById(R.id.account_input);
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }



    @Override
    public void setMode(Mode mode) {
        switch (mode) {
            case CREATE:
                makeEditableView();
                toolbar.setTitle("Create credentials");
                break;
            case EDIT:
                makeEditableView();
                break;

            case VIEW:
                accountInput.setEnabled(false);
                passwordInput.setEnabled(false);
                usernameInput.setEnabled(false);
                setActionButtonImage(R.drawable.ic_edit_white_24dp);
                actionButton.setOnClickListener(v -> setMode(Mode.EDIT));
                break;

            default:
                throw new RuntimeException("Unknown mode");
        }
        accountInput.setText(currentCredentials.getAccountname());
        usernameInput.setText(currentCredentials.getUsername());
        passwordInput.setText(currentCredentials.getPassword());
    }

    public Credentials getCurrentCredentials(Mode mode) {
        switch (mode) {
            case VIEW:
            case EDIT:
                int position = getIntent().getIntExtra(ID_KEY, -1);
                return dataset.get(position);
            case CREATE:
                return new Credentials(0);
            default:
                throw new RuntimeException(mode.toString());
        }
    }

    private void makeEditableView() {
        accountInput.setEnabled(true);
        passwordInput.setEnabled(true);
        usernameInput.setEnabled(true);
        setActionButtonImage(R.drawable.ic_done_white_24dp);
        actionButton.setOnClickListener(v -> save());
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
                && !accountInput.getText().toString().isEmpty()
                && !usernameInput.getText().toString().isEmpty();
    }

    @Override
    public void save() {
        if (!validate()) {
            showErrorSnackBar("Please fill all fields.");
            return;
        }
        currentCredentials.setAccountname(accountInput.getText().toString());
        currentCredentials.setUsername(usernameInput.getText().toString());
        currentCredentials.setPassword(passwordInput.getText().toString());
        if (currentMode.equals(Mode.CREATE)) {
            dataset.add(currentCredentials);
        }

        try {
            repository.saveDataset();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            setMode(Mode.VIEW);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorSnackBar("Failed to save data.");
        }

    }

    private void showErrorSnackBar(String errMessage) {
        Snackbar.make(passwordInput, errMessage, Toast.LENGTH_SHORT).show();
    }
}
