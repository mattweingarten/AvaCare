package com.example.florian.avacare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SkinsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skins);
        setTitle("AvaCare - Skins");

        setupActionBar();
        //TODO what to do when on click back
    }
    //TODO change picture to png to not have raster
    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
