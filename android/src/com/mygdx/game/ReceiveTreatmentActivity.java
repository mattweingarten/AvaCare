package com.mygdx.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceiveTreatmentActivity extends Messagecallbackhandler {

    private final String URL = MainActivity.URL;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_treatment);

        Intent i = getIntent();
        id = i.getIntExtra("id", 0);
        Log.d("##id", ""+id);
        new NetworkAsyncTask("", this, URL + "/users/"+id+"/doctor").execute();
    }

    @Override
    public void handleMessageResponse(String s) throws JSONException { JSONObject jsn;
        Log.d("#receive", s);
        jsn = new JSONObject(s);
        try {
            jsn = jsn.getJSONObject("doctor");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String city = jsn.getString("city");
        String name = jsn.getString("name");

        Log.d("#receive", jsn+"");
    }

}

