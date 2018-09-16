package com.mygdx.game;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;

public abstract class Messagecallbackhandler extends AppCompatActivity {

    public abstract void handleMessageResponse(String s) throws JSONException;

}
