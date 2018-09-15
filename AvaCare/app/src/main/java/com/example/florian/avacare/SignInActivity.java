package com.example.florian.avacare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.IOUtils;

import static java.net.Proxy.Type.HTTP;




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
            send(username, password);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void sign_up_attempt(View V){

    }

    static private void send (String username, String password)throws JSONException {
        JSONObject obj = new JSONObject();
        try {
            obj.put("username", username);
            obj.put("password",password);
            curl(obj);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

