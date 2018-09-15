package com.example.florian.avacare;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        Intent i = getIntent();
        EditText etusername = (EditText) findViewById(R.id.Username);
        EditText etpassword = (EditText) findViewById(R.id.Password);
        EditText etpassword2 = (EditText) findViewById(R.id.Password2);
        etusername.setText(i.getStringExtra("username"));
        etpassword.setText(i.getStringExtra("password"));
        etpassword2.setText(i.getStringExtra("password"));

    }

    public void onRadioButtonClicked(View V) {
        RadioButton rbpatient = (RadioButton) findViewById(R.id.Patient);
        RadioButton rbdoc = (RadioButton) findViewById(R.id.Doctor);

        // Is the button now checked?
        boolean checked = ((RadioButton) V).isChecked();
        // Check which radio button was clicked
        switch(V.getId()) {
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
        int type = rtype.getCheckedRadioButtonId();
        boolean doc = false;

        if(type == 1){
            doc = false;
        }else if(type == 0){
            doc = true;
        }

        if(password.equals(password2)){
            Intent i;
            //TODO DB insertion
            Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
            if(doc) {
                i = new Intent(SignUpActivity.this, DocMainActivity.class);
            } else {
                i = new Intent(SignUpActivity.this, MainActivity.class);
            }
            //TODO get userid from DB
            i.putExtra("username", username);
            startActivity(i);
        }else{
            Toast.makeText(this, "Password error", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(SignUpActivity.this, SignUpActivity.class);
            i.putExtra("username", username);
            i.putExtra("password", "");
            startActivity(i);
        }
    }
}
