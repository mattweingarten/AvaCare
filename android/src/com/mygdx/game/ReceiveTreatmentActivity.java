package com.mygdx.game;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ReceiveTreatmentActivity extends Messagecallbackhandler {

    private final String URL = MainActivity.URL;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_treatment);
        setTitle("AvaCare - receive treatment");
        Intent i = getIntent();
        id = i.getIntExtra("id", 0);
        Log.d("##id", ""+id);
        new NetworkAsyncTask("", this, URL + "/users/"+id+"/doctor").execute();
        TextView tv = (TextView) findViewById(R.id.textViewTreatment);
        tv.setText("CAll " + "name " + "phone number");
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
        TextView tv1 = (TextView) findViewById(R.id.textViewTreatmentNumber);
        TextView tv2 = (TextView) findViewById(R.id.textViewTreatmentName);
        TextView tv3 = (TextView) findViewById(R.id.textViewTreatmentFachgebite);

 ;

        String city = jsn.getString("city");
        String name = jsn.getString("name");
        //TODO set number
        //TODO set facgebiet
        tv2.setText(name);

        Log.d("#receive", jsn+"");
    }

}

