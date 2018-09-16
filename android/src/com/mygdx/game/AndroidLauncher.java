package com.mygdx.game;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		CallbackListener callbackListener = new CallbackListener() {
			@Override
			public void sendData(float x, float y, float z) {
				Log.d("##", "x: " + x + ", y: " + y + ", z: " + z);
			}
		};

		initialize(new MyGdxGame(callbackListener), config);
	}
}
