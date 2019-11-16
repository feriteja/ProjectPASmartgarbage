package com.example.feriteja.projectpasmartgarbage;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    EditText input_email;
    Button btnReset;
    TextView btnBack;
    ConstraintLayout activity_forgot;

     FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        input_email = (EditText) findViewById(R.id.forgot_email);
        btnBack = (TextView) findViewById(R.id.forgot_btn_back);
        btnReset = (Button) findViewById(R.id.forgot_btn_reset);
        activity_forgot = (ConstraintLayout) findViewById(R.id.activity_forgotpass);

        btnReset.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.forgot_btn_back){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else if (view== btnReset){
            resetPassword(input_email.getText().toString());
        }

    }

    private void resetPassword(final String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Snackbar snackbar = Snackbar.make(activity_forgot, "We have sent password to email: " + email, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        else if (task.equals("") ){
                            Snackbar snackbar = Snackbar.make(activity_forgot, "Please enter your email", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        else {
                            Snackbar snackbar = Snackbar.make(activity_forgot, "Failed to send password", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }
                });
    }


}