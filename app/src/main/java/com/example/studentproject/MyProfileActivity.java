package com.example.studentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MyProfileActivity extends AppCompatActivity {
    private ImageView IV;
    private TextView ID, NAME, MOBILE, HOME, BATCH, BLOOD, EMAIL, FACEBOOK;
    SharedPreferences SP;
    private SharedPreferences.Editor editor;
    String Id,Name,Mobile,Batch,Home,Blood,Email,Facebook,Image,Password;
    private ProgressDialog PD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("My Profile");
        PD = new ProgressDialog(MyProfileActivity.this);
        PD.setTitle("Loading");
        PD.setMessage("Please wait...");
        PD.setCancelable(true);

        SP=getSharedPreferences("User", Context.MODE_PRIVATE);
        editor=SP.edit();
        Id= SP.getString("user_id","not found!");
        Name= SP.getString("user_name","not found!");
        Mobile= SP.getString("user_mobile","not found!");
        Batch=SP.getString("user_batch","not found!");
        Home=SP.getString("user_home","not found!");
        Blood=SP.getString("user_blood","not found!");
        Email=SP.getString("user_email","not found!");
        Facebook=SP.getString("user_facebook","not found!");
        Image=SP.getString("user_image","not found!");
        Password=SP.getString("user_password","not found!");
        //Password=decryptPassword(Password);
        String secretKey = "mbstuCseSecretKey";
        Password=AES.decrypt(Password, secretKey) ;

        IV = findViewById(R.id.image_show_info_profile);
        ID = findViewById(R.id.id_show_info_profile);
        NAME = findViewById(R.id.name_show_info_profile);
        MOBILE = findViewById(R.id.mobile_show_info_profile);
        HOME = findViewById(R.id.home_show_info_profile);
        BATCH = findViewById(R.id.batch_show_info_profile);
        BLOOD = findViewById(R.id.blood_show_info_profile);
        EMAIL = findViewById(R.id.email_show_info_profile);
        FACEBOOK = findViewById(R.id.facebook_show_info_profile);


        ID.setText("" + Id);
        NAME.setText("" + Name);
        MOBILE.setText("" + Mobile);
        HOME.setText("" + Home);
        BATCH.setText("" + Batch);
        BLOOD.setText("" + Blood);
        EMAIL.setText("" + Email);
        if (!Facebook.equals("empty"))
        {
            FACEBOOK.setText(Facebook);
        }
        if (Image.equals("0"))
        {
            IV.setImageResource(R.drawable.guest_red);
        }
        else if (Image.equals("1"))
        {
            IV.setImageResource(R.drawable.guest_blue);
        }
        else if (Image.equals("2"))
        {
            IV.setImageResource(R.drawable.guest_yellow);
        }
        else if (Image.equals("3"))
        {
            IV.setImageResource(R.drawable.guest_green);
        }
        else
        {
            Picasso.get().load(Image).placeholder(R.drawable.default_pic).into(IV);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_profile,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.menu_profile_edit_id)
        {
            PD.show();
            FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(Email,Password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            PD.dismiss();
	                        //checking whether my data is deleted or not
                            DatabaseReference databaseReference11=FirebaseDatabase.getInstance().getReference().child("users");
                            databaseReference11.child(Batch).child(makeKeyFromEmail(Email)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getValue()==null)
                                    {
                                        //data is deleted by ADMIN
                                        Toast.makeText(getApplicationContext(),"Log In again...", LENGTH_SHORT).show();
                                        //change state to 0,,, so that user can not auto log in without log in again
                                        editor.putString("user_state","0");
                                        editor.commit();
                                        //So, throw user to Log In page
                                        Intent intent= new Intent(MyProfileActivity.this,MainActivity.class);
                                        //finishing all previous activity
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Intent intent = new Intent(MyProfileActivity.this, MyProfileEditActivity.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String ex=e.getMessage();
                            if (ex.equals("A network error (such as timeout, interrupted connection or unreachable host) has occurred."))
                            {
                                PD.dismiss();
                                Toast.makeText(getApplicationContext(),"Check Your Internet Connection & Try Again later.", LENGTH_SHORT).show();
                            }
                            else
                            {
                                PD.dismiss();
                                //Email or Password is changed
                                //So this changed email and password don't match with Stored email and password
                                makeText(getApplicationContext(),"Log In again...", LENGTH_SHORT).show();
                                //change state to 0,,, so that user can not auto log in without log in again
                                editor.putString("user_state","0");
                                editor.commit();
                                //So, throw user to Log In page
                                Intent intent= new Intent(MyProfileActivity.this,MainActivity.class);
                                //finishing all previous activity
                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
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
}
