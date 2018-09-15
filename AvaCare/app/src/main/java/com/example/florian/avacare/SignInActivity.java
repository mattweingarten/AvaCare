package com.example.florian.avacare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;




public class SignInActivity extends AppCompatActivity {

    static String url = "https://72916a52.ngrok.io/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void sign_in_attempt(View V){
        EditText etusername = (EditText) findViewById(R.id.Username);
        EditText etpassword = (EditText) findViewById(R.id.Password);
        String username = etusername.getText().toString();
        String password = etpassword.getText().toString();
        try {
            boolean bool = send(username, password);
            if (bool){
                Intent i = new Intent(SignInActivity.this,MainActivity.class);
                i.putExtra("username",username);
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
    public void sign_up_attempt(View V){
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

            JSONObject response = post_request(obj);
            String id = "user_id";
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
        return false;
    }

    static JSONObject post_request(JSONObject obj) throws IOException, JSONException {

        PostMethod post = new PostMethod(url);
        NameValuePair[] data = {
                new NameValuePair("username", obj.getString("username")),
                new NameValuePair("password", obj.getString("password"))
        };
        post.setRequestBody(data);
// execute method and handle any error responses.

        InputStream in = post.getResponseBodyAsStream();
        JSONObject answer = new JSONObject();
        answer.putOpt("id", in);

// handle response
        return answer;
    }

}

