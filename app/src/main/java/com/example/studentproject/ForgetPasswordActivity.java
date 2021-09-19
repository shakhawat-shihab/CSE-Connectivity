package com.example.studentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText EMAIL;
    private Button B;
    private ProgressDialog PD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("CSE Connectivity");
        EMAIL=findViewById(R.id.recover_mail_edit_id);
        B=findViewById(R.id.recover_mail_button_id);
        PD = new ProgressDialog(ForgetPasswordActivity.this);
        PD.setTitle("Loading");
        PD.setMessage("Please wait...");
        PD.setCancelable(true);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email=EMAIL.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    EMAIL.setError("Enter valid Email");
                    EMAIL.requestFocus();
                    return;
                }
                PD.show();
                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                firebaseAuth.sendPasswordResetEmail(Email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(),"Recovery Email is sent to your email",Toast.LENGTH_SHORT).show();
                                    PD.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Failed to send recovery email",Toast.LENGTH_SHORT).show();
                                    PD.dismiss();
                                }
                            }
                        });
            }
        });
    }
}
