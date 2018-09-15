package com.example.florian.avacare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;




public class SignInActivity extends AppCompatActivity {

    static String url = "http://url.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    private void sign_in_attempt(View V){
        EditText etusername = (EditText) findViewById(R.id.Username);
        EditText etpassword = (EditText) findViewById(R.id.Password);
        String username = etusername.getText().toString();
        String password = etpassword.getText().toString();
        try {
            boolean bool = send(username, password);
            if (bool){
                Intent i = new Intent(SignInActivity.this,MainActivity.class);
                startActivity(i);
            }
            else{
                Toast.makeText(this,"wrong login data",Toast.LENGTH_SHORT).show();
                etusername.setText("");
                etpassword.setText("");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    private void sign_up_attempt(View V){
        EditText etusername = (EditText) findViewById(R.id.Username);
        EditText etpassword = (EditText) findViewById(R.id.Password);
        String username = etusername.getText().toString();
        String password = etpassword.getText().toString();
        if(username.equals("") || password.equals("")){
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
            i.putExtra("username", username);
            i.putExtra("password", password);
            startActivity(i);

        }

    }

    static private boolean send (String username, String password)throws JSONException {
        JSONObject obj = new JSONObject();
        try {
            obj.put("username", username);
            obj.put("password",password);

            JSONObject response = curl(obj);
            String id = "id";
            int userid = response.getInt(id);
            if (userid != 0) {
                return true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    static JSONObject curl(JSONObject obj) throws IOException, JSONException {

        URL object=new URL(url);

        HttpURLConnection con = (HttpURLConnection) object.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestMethod("POST");

        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(obj.toString());
        wr.flush();
        wr.close();


        // read the response
        InputStream in = new BufferedInputStream(con.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
        JSONObject jsonObject = new JSONObject(result);


        in.close();
        con.disconnect();

        return jsonObject;
    }
}

