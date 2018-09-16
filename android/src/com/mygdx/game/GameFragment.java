package com.mygdx.game;

import android.app.Activity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class GameFragment extends AndroidFragmentApplication {
    public CallbackListener mainListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances){

        CallbackListener listener = new CallbackListener() {
            @Override
            public void sendData(float x, float y, float z) {

            }
        };
        return initializeForView(new MyGdxGame(listener));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainListener = (CallbackListener) activity;
    }
}
