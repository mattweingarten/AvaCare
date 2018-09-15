package com.example.florian.avacare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class SignInActivity extends Messagecallbackhandler {

    EditText etusername;
    EditText etpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void sign_in_attempt(View V) {
        etusername = (EditText) findViewById(R.id.Username);
        etpassword = (EditText) findViewById(R.id.Password);
        String username = etusername.getText().toString();
        String password = etpassword.getText().toString();
        StringBuilder sb = new StringBuilder();
        sb.append("&username=");
        sb.append(username);
        sb.append("&secret=");
        sb.append(password);
        new NetworkAsyncTask(sb.toString(),this,"https://97e89c8b.ngrok.io/login").execute();
    }

    public void sign_up_attempt(View V) {
        EditText etusername = (EditText) findViewById(R.id.Username);
        EditText etpassword = (EditText) findViewById(R.id.Password);
        String username = etusername.getText().toString();
        String password = etpassword.getText().toString();
        if (username.equals("") || password.equals("")) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
            i.putExtra("username", etusername.getText().toString());
            i.putExtra("secret", etpassword.getText().toString());
            startActivity(i);
        }
    }

    @Override
    public void handleMessageResponse(String s) {

        Log.d("#receive", s);

        JSONObject jsn;
        try {

            jsn = new JSONObject(s);
            int id = jsn.getInt("id");
            if (id != 0){
                Toast.makeText(this,"logging in...",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SignInActivity.this,MainActivity.class);
                i.putExtra("id",id);
                startActivity(i);
            }
            else{
                Toast.makeText(this,"wrong login data",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SignInActivity.this,SignInActivity.class);
                etusername.setText("");
                etpassword.setText("");
                startActivity(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


