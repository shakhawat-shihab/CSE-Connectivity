package com.example.studentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class ModeratorActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences SP;
    SharedPreferences.Editor editor;
    private FirebaseAuth firebaseAuth;
    private String Email,Password,Batch,key;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
    private Button B1, B2, B3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Moderator");
        B1=findViewById(R.id.apply_moderator_button);
        B2=findViewById(R.id.check_moderator_button);
        B3=findViewById(R.id.enter_moderator_button);
        B1.setOnClickListener(this);
        B2.setOnClickListener(this);
        B3.setOnClickListener(this);
        firebaseAuth=FirebaseAuth.getInstance();
        SP=getSharedPreferences("User", Context.MODE_PRIVATE);
        editor=SP.edit();
        Email=SP.getString("user_email","not found!");
        Password=SP.getString("user_password","not found!");
        //Password=decryptPassword(Password);
        String secretKey = "mbstuCseSecretKey";
        Password=AES.decrypt(Password, secretKey) ;
        Batch=SP.getString("user_batch","not found!");
        key=makeKeyFromEmail(Email);
    }

    private String decryptPassword(String password) {
        String s="";
        for(int i=0;i<password.length();i++)
        {
            char ch;
            ch =(char) (password.charAt(i) - '.');
            s+=ch;
        }
        return  s;
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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.apply_moderator_button)
        {
            //at first take your data
            DatabaseReference ref= databaseReference.child(Batch).child(key);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String my_isModerator=dataSnapshot.child("isModerator").getValue(String.class);
                    if (my_isModerator.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(),"You are already a moderator",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String my_id=dataSnapshot.child("id").getValue(String.class);
                        String my_name=dataSnapshot.child("name").getValue(String.class);
                        String my_batch=dataSnapshot.child("batch").getValue(String.class);
                        String my_email=dataSnapshot.child("email").getValue(String.class);
                        String my_mobile=dataSnapshot.child("mobile").getValue(String.class);
                        User user =new User(my_id, my_name, my_batch, my_email, my_mobile);
                        //insert data in 'moderator_request' RTD
                        DatabaseReference databaseRef=FirebaseDatabase.getInstance().getReference().child("moderator_request");
                        //in this insert we will not use the 'batch child'
                        databaseRef.child(key).setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),"Your Request is send.\n Wait for approval.",Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"Failed to upload",Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if (v.getId()==R.id.check_moderator_button)
        {
            DatabaseReference ref= databaseReference.child(Batch).child(key);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String isModerator=dataSnapshot.child("isModerator").getValue(String.class);
                    //Toast.makeText(getApplicationContext(),isModerator,Toast.LENGTH_SHORT).show();
                    //isModerator == 0, current user is not approved as moderator
                    //isModerator == 1, current user is already approved as moderator
                    if (isModerator.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(),"Congratulations, You are a moderator!", LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"You are not approved as a moderator!", LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        if (v.getId()==R.id.enter_moderator_button)
        {
            DatabaseReference ref= databaseReference.child(Batch).child(key);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String isModerator=dataSnapshot.child("isModerator").getValue(String.class);
                    //Toast.makeText(getApplicationContext(),isModerator,Toast.LENGTH_SHORT).show();
                    //isModerator == 0, current user is not approved as moderator
                    //isModerator == 1, current user is already approved as moderator
                    if (isModerator.equals("1"))
                    {
                        Intent intent=new Intent(ModeratorActivity.this, RequestActivityModerator.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"You are not approved as a moderator!", LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Database Error "+databaseError, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
