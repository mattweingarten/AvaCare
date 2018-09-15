package com.example.florian.avacare;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends Messagecallbackhandler {

    EditText etusername;
    EditText etpassword;
    EditText etpassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent i = getIntent();
        etusername = (EditText) findViewById(R.id.Username);
        etpassword = (EditText) findViewById(R.id.Password);
        etpassword2 = (EditText) findViewById(R.id.Password2);
        etusername.setText(i.getStringExtra("username"));
        etpassword.setText(i.getStringExtra("secret"));
        etpassword2.setText(i.getStringExtra("secret"));

        RadioButton rbpatient = (RadioButton) findViewById(R.id.Patient);
        rbpatient.setTypeface(null, Typeface.BOLD_ITALIC);
        RadioButton rbdoc = (RadioButton) findViewById(R.id.Doctor);
        rbdoc.setTypeface(null, Typeface.NORMAL);
    }

    public void onRadioButtonClicked(View V) {
        RadioButton rbpatient = (RadioButton) findViewById(R.id.Patient);
        RadioButton rbdoc = (RadioButton) findViewById(R.id.Doctor);

        // Is the button now checked?
        boolean checked = ((RadioButton) V).isChecked();
        // Check which radio button was clicked
        switch (V.getId()) {
            case R.id.Doctor:
                if (checked)
                    //set the checked radio button's text style bold italic
                    rbpatient.setTypeface(null, Typeface.NORMAL);
                //set the other two radio buttons text style to default
                rbdoc.setTypeface(null, Typeface.BOLD_ITALIC);
                break;
            case R.id.Patient:
                if (checked)
                    //set the checked radio button's text style bold italic
                    rbpatient.setTypeface(null, Typeface.BOLD_ITALIC);
                //set the other two radio buttons text style to default
                rbdoc.setTypeface(null, Typeface.NORMAL);
                break;
        }
    }

    public void sign_up_attempt(View V) {
        EditText etusername = (EditText) findViewById(R.id.Username);
        EditText etpassword = (EditText) findViewById(R.id.Password);
        EditText etpassword2 = (EditText) findViewById(R.id.Password2);
        RadioGroup rtype = (RadioGroup) findViewById(R.id.Type);

        String username = etusername.getText().toString();
        String password = etpassword.getText().toString();
        String password2 = etpassword2.getText().toString();
        //int type = rtype.getCheckedRadioButtonId();
        if (etpassword.getText().toString().equals(etpassword2.getText().toString())) {
            StringBuilder sb = new StringBuilder();
            sb.append("&username=");
            sb.append(username);
            sb.append("&secret=");
            sb.append(password);
            new NetworkAsyncTask(sb.toString(), this, "https://97e89c8b.ngrok.io/users").execute();
        }
    }


    @Override
    public void handleMessageResponse(String s) {
        Log.d("#receive", s);
        boolean doc = false;    //simplification
        JSONObject jsn;
        try {
            jsn = new JSONObject(s);
            int id = jsn.getInt("id");

            Intent i;
            if (id != 0) {
                Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
                if (doc) {
                    i = new Intent(SignUpActivity.this, DocMainActivity.class);
                    i.putExtra("id", id);
                    startActivity(i);
                } else {
                    i = new Intent(SignUpActivity.this, MainActivity.class);
                    i.putExtra("id", id);
                    startActivity(i);
                }
            } else {
                Toast.makeText(this, "Sign Up error", Toast.LENGTH_SHORT).show();
                i = new Intent(SignUpActivity.this, SignUpActivity.class);
                i.putExtra("username", etusername.getText().toString());
                i.putExtra("secret", "");
                startActivity(i);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
}
