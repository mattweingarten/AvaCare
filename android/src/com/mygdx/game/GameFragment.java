package com.mygdx.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class GameFragment extends AndroidFragmentApplication {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances){

        CallbackListener listener = new CallbackListener() {
            @Override
            public void sendData(float x, float y, float z) {

            }
        };
        return initializeForView(new MyGdxGame(listener));
    }
}
