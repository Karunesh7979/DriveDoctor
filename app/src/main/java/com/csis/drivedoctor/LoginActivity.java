package com.csis.drivedoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.csis.drivedoctor.Helpers.Loader;
import com.csis.drivedoctor.preauthDatabase.TaskCompletion;
import com.csis.drivedoctor.preauthDatabase.DatabaseHelper;
import com.csis.drivedoctor.Helpers.ModelHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {

    Button signIn;
    TextInputEditText textFieldPassword;
    EditText editTextEmail;
    TextView forgotPasswordLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView signUp = findViewById(R.id.idTextViewSignupLogin);
        signIn = findViewById(R.id.idLoginButtonLogin);
        textFieldPassword = findViewById(R.id.idEditTextPasswordSignin);
        editTextEmail = findViewById(R.id.idEditTextEamilSignIn);
        forgotPasswordLabel = findViewById(R.id.idTextForgetPasswordLogin);

        forgotPasswordLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAction();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    //Button methods
    void loginAction() {

        //Check for empty fields and then login

        if (editTextEmail.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter email address.", Toast.LENGTH_SHORT).show();
        } else if (!isValidEmail(editTextEmail.getText().toString().toLowerCase().trim())) {
            Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
        } else if (textFieldPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter password.", Toast.LENGTH_SHORT).show();
        } else {
            checkInFirebase();
        }
    }

    private void checkInFirebase() {

        DatabaseHelper.getInstance().initialiseFirebase();
        Loader.show(this);
        DatabaseHelper.getInstance().mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString().toLowerCase().trim(), textFieldPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            try {

                                //Save details in shared pref
                                SharedPreferences.Editor editor = DatabaseHelper.getInstance().getPref().edit();
                                editor.putString("userId", DatabaseHelper.getInstance().user.getUid());
                                editor.apply(); // commit changes

                                ModelHelper.getInstance().fetchUser(new TaskCompletion() {
                                    @Override
                                    public void taskCompletion(boolean isSuccess) {
                                        Loader.dismiss();
                                        finish();
                                        startActivity(new Intent(LoginActivity.this, NavigationMainActivity.class));
                                    }
                                });
                            } catch (Exception ex) {
                                Loader.dismiss();
                                Toast.makeText(LoginActivity.this, "Error occured!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Loader.dismiss();
                            Log.w("FIREBASE ::", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
