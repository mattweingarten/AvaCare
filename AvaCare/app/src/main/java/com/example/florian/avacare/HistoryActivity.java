package com.example.florian.avacare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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

        //DB
        Intent i = getIntent();
        id = i.getIntExtra("id", 0);
        Log.d("##id",""+id);
        StringBuilder sb = new StringBuilder();
        sb.append("https://97e89c8b.ngrok.io/users/");
        sb.append(id);
        sb.append("/history");
        new NetworkAsyncTask("", this, sb.toString()).execute();
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