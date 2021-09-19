package com.example.studentproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText EMAIL,PASSWORD;
    private Spinner BATCH;
    private Button LOG,SIGN;
    private TextView CLICK;
    private String[] batch;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
    SharedPreferences SP;
    private SharedPreferences.Editor editor;
    private ProgressDialog PD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_main);
        setTitle("CSE Connectivity");
        PD = new ProgressDialog(MainActivity.this);
        PD.setTitle("Logging in");
        PD.setMessage("Please wait...");
        PD.setCancelable(false);
        SP=getSharedPreferences("User", Context.MODE_PRIVATE);
        editor=SP.edit();
        EMAIL=findViewById(R.id.log_in_email);
        PASSWORD=findViewById(R.id.log_in_password);
        BATCH=findViewById(R.id.log_in_batch);
        LOG=findViewById(R.id.log_in_button);
        SIGN=findViewById(R.id.sign_up_button);
        CLICK=findViewById(R.id.click_here_text);
        LOG.setOnClickListener(this);
        SIGN.setOnClickListener(this);
        CLICK.setOnClickListener(this);
        //batch spinner
        batch=getResources().getStringArray(R.array.batch_list);
        ArrayAdapter<String> adapter_batch=new ArrayAdapter<String>(this,R.layout.spinner_text,batch);
        BATCH.setAdapter(adapter_batch);
        firebaseAuth=FirebaseAuth.getInstance();

        //getting state from sharedPreference->
        String state=SP.getString("user_state","not found!");
        //String em=SP.getString("user_email","not found!");
        //String p=SP.getString("user_password","not found!");
        //String b=SP.getString("user_batch","not found!");
        //Toast.makeText(getApplicationContext(),state+" "+em+" "+b, LENGTH_SHORT).show();
    }


    @Override
    public void finish() {
        super.finish();
		//go right->left (as we are going next)
		//as we clciked Log In
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }



    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.log_in_button)
        {
            final String Email,Password,Batch;
            Email=EMAIL.getText().toString().trim().toLowerCase();
            Password=PASSWORD.getText().toString().trim();
            Batch = batch[BATCH.getSelectedItemPosition()];
            if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                EMAIL.setError("Enter valid Email");
                EMAIL.requestFocus();
                return;
            }
            if (Password.equals("")) {
                PASSWORD.setError("Enter Password");
                PASSWORD.requestFocus();
                return;
            }
            if (Password.length() < 8) {
                PASSWORD.setError("Must be 8 characters, Don't use space");
                PASSWORD.requestFocus();
                return;
            }
            if (Batch.equals("null")) {
                AlertDialog.Builder ADB1;
                ADB1 = new AlertDialog.Builder(MainActivity.this);
                ADB1.setTitle("Warning");
                ADB1.setIcon(R.drawable.ic_warning);
                ADB1.setMessage("Batch Should not be null");
                AlertDialog AD1 = ADB1.create();
                AD1.show();
                return;
            }
            PD.show();
            LOG.setEnabled(false);
            firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful())
                    {
                        if (firebaseAuth.getCurrentUser().isEmailVerified())
                        {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
                            String key=makeKeyFromEmail(firebaseAuth.getCurrentUser().getEmail().toLowerCase().toString());
                            //Toast.makeText(getApplicationContext(),key, LENGTH_SHORT).show();
                            DatabaseReference ref= databaseReference.child(Batch).child(key);
                            //checking whether data for that email exist in database or it is deleted
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.getValue() == null) {
                                        makeText(getApplicationContext(),"Your data is deleted!! Contact with your CR", LENGTH_SHORT).show();
                                        PD.dismiss();
                                    }
                                    else{
                                        finish();
                                        PD.dismiss();
                                        makeText(getApplicationContext(),"Welcome", LENGTH_SHORT).show();
                                        //The data of Shared Preference may be viewable by rooted device
                                        // So we planed to encrypt it
                                        String encryptedPassword;
                                        String secretKey = "mbstuCseSecretKey";
                                        encryptedPassword = AES.encrypt(Password, secretKey) ;
                                        //Toast.makeText(getApplicationContext(),encryptedPassword, LENGTH_SHORT).show();
                                        //String decryptedPassword = AES.decrypt(encryptedPassword, secretKey) ;
                                        //Toast.makeText(getApplicationContext(),decryptedPassword, LENGTH_SHORT).show();
                                        //encryptedPassword=encryptPassword(Password);
                                        setSharedPreference(Email,encryptedPassword,Batch,"1");
                                        Intent intent =new Intent(MainActivity.this, HomeActivity.class);
                                        editor.putString("log_in_time","on");
                                        editor.commit();
                                        startActivity(intent);
                                        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    //oncancelled() never call
                                    Toast.makeText(getApplicationContext(), "Database Error "+databaseError, Toast.LENGTH_SHORT).show();
                                    PD.dismiss();
                                }
                            });
                        }
                        else
                        {
                            makeText(getApplicationContext(),"Check Your Mail box & verify Email", LENGTH_LONG).show();
                            PD.dismiss();
                        }
                        LOG.setEnabled(true);
                    }
                }
            });
            firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    makeText(getApplicationContext(),"Check Internet Connection, Email & Password", LENGTH_SHORT).show();
                    LOG.setEnabled(true);
                    PD.dismiss();
                }
            });
        }
        if (v.getId()==R.id.sign_up_button)
        {
            //makeText(this,"sign up", LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.click_here_text)
        {
            Intent intent=new Intent(MainActivity.this,ForgetPasswordActivity.class);
            startActivity(intent);
        }

    }

    private String encryptPassword(String password) {
        String s="";
        for(int i=0;i<password.length();i++)
        {
            char ch;
            ch =(char) (password.charAt(i) + '.');
            s+=ch;
        }
        return  s;
    }


    private void setSharedPreference(String email,String encryptedPassword, String batch, String s) {
        editor.putString("user_email",email);
        editor.putString("user_password",encryptedPassword);
        editor.putString("user_batch",batch);
        editor.putString("user_state",s);
        editor.commit();
    }

    private String makeKeyFromEmail(String email) {
        String s="";
        for (int i=0;i<email.length();i++)
        {
            if (email.charAt(i)=='.')
            {
                s+='A';
            }
            if(email.charAt(i)>='a'&&email.charAt(i)<='z')
            {
                s+=email.charAt(i);
            }
            if(email.charAt(i)>='0'&&email.charAt(i)<='9')
            {
                s+=email.charAt(i);
            }
        }
        return s;
    }

}
