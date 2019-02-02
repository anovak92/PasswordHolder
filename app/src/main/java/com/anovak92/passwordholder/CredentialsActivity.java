package com.anovak92.passwordholder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class CredentialsActivity extends AppCompatActivity implements CredentialsView {

    @SuppressWarnings("CheckStyle")
    public final static String MODE_KEY = "mode";
    @SuppressWarnings("CheckStyle")
    public final static String ID_KEY = "id";

    private CredentialsView.Mode currentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar
                .make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        );

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            setMode(Mode.valueOf(intent.getStringExtra(MODE_KEY)));
        }
    }

    @Override
    public void setMode(Mode mode) {
        currentMode = mode;
        switch (mode) {
            case CREATE:
                break;
            case EDIT:
                break;
            case VIEW:
                break;
            default:
                throw new RuntimeException("Unknown mode");
        }
    }

    @Override
    public Mode getCurrentMode() {
        return currentMode;
    }

    @Override
    public void save() {

    }
}
