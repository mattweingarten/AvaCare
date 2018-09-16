package com.mygdx.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryActivity extends Messagecallbackhandler {

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TODO get history, arrange in scroll view
        setTitle("AvaCare - History");
        setupActionBar();
        //DB
        Intent i = getIntent();
        id = i.getIntExtra("id", 0);
        Log.d("##id",""+id);
        StringBuilder sb = new StringBuilder();
        sb.append("https://c3151d22.ngrok.io/users/");
        sb.append(id);
        sb.append("/history");
        new NetworkAsyncTask("", this, sb.toString()).execute();
    }
    //TODO change picture to png to not have raster
    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleMessageResponse(String s) {
        Log.d("#receive", s);
        JSONObject jsn;

        try {
            jsn = new JSONObject(s);
            JSONArray symptoms = jsn.getJSONArray("symptoms");
            ArrayList<String> list = new ArrayList<String>();
            if (symptoms != null) {
                int len = symptoms.length();
                for (int i=0;i<len;i++){
                    list.add(symptoms.get(i).toString());
                }
            }
            //TODO scroll view reper "Räpper"
            Log.d("#receive", ""+symptoms);
            Log.d("#receive", ""+list);


        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }
}