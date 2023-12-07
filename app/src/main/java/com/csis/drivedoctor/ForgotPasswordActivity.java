package com.csis.drivedoctor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class ForgotPasswordActivity extends AppCompatActivity {

    EditText newpass;
    EditText confirmpass;
    Button submitButton;
    EditText email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        email = findViewById(R.id.idEditTextEmail);
        newpass = findViewById(R.id.idEditTextNewPassword);
        confirmpass = findViewById(R.id.idEditTextConfirmpassword);
        submitButton = findViewById(R.id.idSubmitbutton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordAction();

            }
        });
    }

    void forgotPasswordAction() {
        String Email = email.getText().toString();
        String Newpass = newpass.getText().toString();
        String ConfirmPass = confirmpass.getText().toString();

        if (Email.equals("")) {
            Toast.makeText(this, "Email cannot be left blank.", Toast.LENGTH_SHORT).show();
        } else if (newpass.getText().toString().trim().length() < 7) {
            Toast.makeText(this, "Password should be at least 7 digits.", Toast.LENGTH_SHORT).show();

        } else if(Newpass.equals(ConfirmPass)) {
            Toast.makeText(this, "password and confirm password not match.", Toast.LENGTH_SHORT).show();
        }
        else {
            finish();
        }
    }
}