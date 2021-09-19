package com.example.studentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{
    private CardView B13,B14,B15,B16,B17,B18,B19,SEARCH;
    SharedPreferences SP;
    SharedPreferences.Editor editor;
    private FirebaseAuth firebaseAuth;
    private String Email,Password,Batch;
    ProgressDialog PD;
    private NavigationView NV;
    private DrawerLayout DL;
    private ActionBarDrawerToggle ABDT;
    private int cnt=0;
    ProgressDialog downloadProgress;
    AlertDialog AD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("MBSTU CSE");
        downloadProgress = new ProgressDialog(HomeActivity.this);
        downloadProgress.setTitle("Downloading");
        downloadProgress.setMessage("Please wait...");
        downloadProgress.setCancelable(false);
        SEARCH=findViewById(R.id.search_card_id);
        B13=findViewById(R.id.batch_13_card_id);
        B14=findViewById(R.id.batch_14_card_id);
        B15=findViewById(R.id.batch_15_card_id);
        B16=findViewById(R.id.batch_16_card_id);
        B17=findViewById(R.id.batch_17_card_id);
        B18=findViewById(R.id.batch_18_card_id);
        B19=findViewById(R.id.batch_19_card_id);
        SEARCH.setOnClickListener(this);
        B13.setOnClickListener(this);
        B14.setOnClickListener(this);
        B15.setOnClickListener(this);
        B16.setOnClickListener(this);
        B17.setOnClickListener(this);
        B18.setOnClickListener(this);
        B19.setOnClickListener(this);
        DL=findViewById(R.id.drawerId);
        NV=findViewById(R.id.navigationId);
        NV.setNavigationItemSelectedListener(this);
        //for making navigation icon colorful,,
        NV.setItemIconTintList(null);
        ABDT =new ActionBarDrawerToggle(this,DL,R.string.nav_open,R.string.nav_close);
        DL.addDrawerListener(ABDT);
        ABDT.syncState();
        //to make back icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PD = new ProgressDialog(HomeActivity.this);
        PD.setTitle("Loading");
        PD.setMessage("Please wait...");
        PD.setCancelable(true);
        firebaseAuth=FirebaseAuth.getInstance();
        SP=getSharedPreferences("User", Context.MODE_PRIVATE);
        editor=SP.edit();
        Email=SP.getString("user_email","not found!");
        Password=SP.getString("user_password","not found!");
        Batch=SP.getString("user_batch","not found!");
        String secretKey = "mbstuCseSecretKey";
       // Toast.makeText(getApplicationContext(),"Encrypted: "+Password, LENGTH_SHORT).show();
        Password=AES.decrypt(Password, secretKey) ;
        //Toast.makeText(getApplicationContext(),"Decrypted: "+Password, LENGTH_SHORT).show();
        //Password=decryptPassword(Password);
        //getting the log_in_time (if we are in log_in_time then we get "on")
        String log_in_time=SP.getString("log_in_time","not found!");

        //if log_in_time is equal "on" then download my profile data
        //so now we should download our own data
        if(log_in_time.equals("on"))
        {
            //now set log_in_time is equal to "off"
            editor.putString("log_in_time","off");
            editor.commit();
            DatabaseReference databaseRef=FirebaseDatabase.getInstance().getReference().child("users").child(Batch);
            databaseRef.child(makeKeyFromEmail(Email)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String p_id=dataSnapshot.child("id").getValue(String.class);
                    String p_name=dataSnapshot.child("name").getValue(String.class);
                    //String p_batch=dataSnapshot.child("batch").getValue(String.class);
                    String p_mobile=dataSnapshot.child("mobile").getValue(String.class);
                    //String p_email=dataSnapshot.child("email").getValue(String.class);
                    String p_home=dataSnapshot.child("home").getValue(String.class);
                    String p_blood=dataSnapshot.child("blood").getValue(String.class);
                    String p_facebook=dataSnapshot.child("facebook").getValue(String.class);
                    String p_image=dataSnapshot.child("imageUrl").getValue(String.class);
                    editor.putString("user_id",p_id);
                    editor.putString("user_name",p_name);
                    editor.putString("user_mobile",p_mobile);
                    //editor.putString("user_batch",p_batch);
                    editor.putString("user_home",p_home);
                    editor.putString("user_blood",p_blood);
                    //editor.putString("user_email",p_email);
                    editor.putString("user_facebook",p_facebook);
                    editor.putString("user_image",p_image);
                    editor.commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //as it is first time so download all the data
            downloadAllData();
            //show hints
            AlertDialog.Builder ADB3;
            ADB3= new AlertDialog.Builder(HomeActivity.this);
            View mView=getLayoutInflater().inflate(R.layout.alert_dialog_help,null);
            final TextView H=mView.findViewById(R.id.custom_alert_heading);
            final TextView M=mView.findViewById(R.id.custom_alert_message);
            final TextView Pos_B=mView.findViewById(R.id.custom_alert_help_positive);
            Pos_B.setText("Got it");
            Pos_B.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AD.dismiss();
                }
            });
            M.setText("Click the Download icon,\nTo download all Users's data.");
            ADB3.setView(mView);
            AD = ADB3.create();
            AD.show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.search_card_id)
        {
            // Toast.makeText(this,"Search",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.batch_13_card_id)
        {
            //Toast.makeText(this,"13",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(HomeActivity.this,Batch13Activity.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.batch_14_card_id)
        {
            // Toast.makeText(this,"14",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(HomeActivity.this,Batch14Activity.class);
            startActivity(intent);

        }
        if (v.getId()==R.id.batch_15_card_id)
        {
            // Toast.makeText(this,"15",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(HomeActivity.this,Batch15Activity.class);
            startActivity(intent);

        }
        if (v.getId()==R.id.batch_16_card_id)
        {
            // Toast.makeText(this,"16",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(HomeActivity.this,Batch16Activity.class);
            startActivity(intent);

        }
        if (v.getId()==R.id.batch_17_card_id)
        {
            //Toast.makeText(this,"17",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(HomeActivity.this,Batch17Activity.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.batch_18_card_id)
        {
            //Toast.makeText(this,"18",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(HomeActivity.this,Batch18Activity.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.batch_19_card_id)
        {
            //Toast.makeText(this,"19",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(HomeActivity.this,Batch19Activity.class);
            startActivity(intent);
        }

    }

    @Override
    public void finish() {
        super.finish();
		//go left->right (as we are going previous)
		//previous, because we are finishing activity when we are logging out
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(ABDT.onOptionsItemSelected(item))
        {
            return true;

        }
        if(item.getItemId()==R.id.menu_home_download)
        {
            //PD.show();
            //download alert dialog
            final AlertDialog.Builder ADB;
            ADB= new AlertDialog.Builder(HomeActivity.this);
            View mView=getLayoutInflater().inflate(R.layout.alert_dialog_download,null);
            TextView H=mView.findViewById(R.id.custom_alert_download_heading);
            TextView M=mView.findViewById(R.id.custom_alert_download_message);
           // M.setText("To Download Users's Information,\nYou need better Internet Connection.");
            M.setText("To Download Users's Information,\nYou need better Internet Connection.");
            TextView Neutral,Positive;
            Neutral=mView.findViewById(R.id.custom_alert_download_neutral);
            Positive=mView.findViewById(R.id.custom_alert_download_positive);
            Neutral.setText("Close");
            Positive.setText("Continue");
            Neutral.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //for cancel Alert Dialog variable should be defined globally
                    AD.dismiss();
                }
            });
            Positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AD.dismiss();
                    PD.show();
                    firebaseAuth.signInWithEmailAndPassword(Email,Password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // successfully log in
                                    //password is not changed
                                    PD.dismiss();
                                    //now check if my data is in the table or not
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
                                                Intent intent= new Intent(HomeActivity.this,MainActivity.class);
                                                //finishing all previous activity
                                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                downloadProgress.show();
                                                downloadAllData();
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
                                        Toast.makeText(getApplicationContext(),"Check Your Internet Connection & Try Again later.", LENGTH_SHORT).show();
                                        PD.dismiss();
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
                                        Intent intent= new Intent(HomeActivity.this,MainActivity.class);
                                        //finishing all previous activity
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            });
            ADB.setView(mView);
            AD = ADB.create();
            AD.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadAllData() {
        final List<User> userList;
        userList=new ArrayList<>();
        final DatabaseHelper13 databaseHelper13;
        final DatabaseHelper14 databaseHelper14;
        final DatabaseHelper15 databaseHelper15;
        final DatabaseHelper16 databaseHelper16;
        final DatabaseHelper17 databaseHelper17;
        final DatabaseHelper18 databaseHelper18;
        final DatabaseHelper19 databaseHelper19;
        databaseHelper13=new DatabaseHelper13(this);
        databaseHelper14=new DatabaseHelper14(this);
        databaseHelper15=new DatabaseHelper15(this);
        databaseHelper16=new DatabaseHelper16(this);
        databaseHelper17=new DatabaseHelper17(this);
        databaseHelper18=new DatabaseHelper18(this);
        databaseHelper19=new DatabaseHelper19(this);
        DatabaseReference databaseReference13=FirebaseDatabase.getInstance().getReference().child("users").child("13th Batch");
        DatabaseReference databaseReference14=FirebaseDatabase.getInstance().getReference().child("users").child("14th Batch");
        DatabaseReference databaseReference15=FirebaseDatabase.getInstance().getReference().child("users").child("15th Batch");
        DatabaseReference databaseReference16=FirebaseDatabase.getInstance().getReference().child("users").child("16th Batch");
        DatabaseReference databaseReference17=FirebaseDatabase.getInstance().getReference().child("users").child("17th Batch");
        DatabaseReference databaseReference18=FirebaseDatabase.getInstance().getReference().child("users").child("18th Batch");
        DatabaseReference databaseReference19=FirebaseDatabase.getInstance().getReference().child("users").child("19th Batch");
        //Batch 13
        databaseReference13.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //at firsts delete the table
                databaseHelper13.truncateTable();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                    User dummy=dataSnapshot1.getValue(User.class);
                    long rowId = databaseHelper13.insertData(dummy.getId(), dummy.getName(),dummy.getMobile(),dummy.getHome(),dummy.getBatch(),dummy.getBlood(),dummy.getImageUrl(),dummy.getEmail(),dummy.getFacebook());
                    if (rowId>0)
                    {
                       // Toast.makeText(getApplicationContext(), "13 saved at row  " + rowId, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Toast.makeText(getApplicationContext(),"failed to save "+rowId,Toast.LENGTH_SHORT).show();
                    }
                }
                checkComplete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Batch 14
        databaseReference14.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               databaseHelper14.truncateTable();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                    User dummy=dataSnapshot1.getValue(User.class);
                    long rowId = databaseHelper14.insertData(dummy.getId(), dummy.getName(),dummy.getMobile(),dummy.getHome(),dummy.getBatch(),dummy.getBlood(),dummy.getImageUrl(),dummy.getEmail(),dummy.getFacebook());
                    if (rowId>0)
                    {
                        //Toast.makeText(getApplicationContext(), " 14 saved at row " + rowId, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                       // Toast.makeText(getApplicationContext(),"failed to save 14 "+rowId,Toast.LENGTH_SHORT).show();
                    }
                }
                checkComplete();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Batch 15
        databaseReference15.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseHelper15.truncateTable();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                    User dummy=dataSnapshot1.getValue(User.class);
                    long rowId = databaseHelper15.insertData(dummy.getId(), dummy.getName(),dummy.getMobile(),dummy.getHome(),dummy.getBatch(),dummy.getBlood(),dummy.getImageUrl(),dummy.getEmail(),dummy.getFacebook());
                    if (rowId>0)
                    {
                        //Toast.makeText(getApplicationContext(), " 15 saved at row " + rowId, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Toast.makeText(getApplicationContext(),"failed to save "+rowId,Toast.LENGTH_SHORT).show();
                    }
                }
                checkComplete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Batch 16
        databaseReference16.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseHelper16.truncateTable();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                    User dummy=dataSnapshot1.getValue(User.class);
                    long rowId = databaseHelper16.insertData(dummy.getId(), dummy.getName(),dummy.getMobile(),dummy.getHome(),dummy.getBatch(),dummy.getBlood(),dummy.getImageUrl(),dummy.getEmail(),dummy.getFacebook());
                    if (rowId>0)
                    {
                        //Toast.makeText(getApplicationContext(), " 16 saved at row " + rowId, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Toast.makeText(getApplicationContext(),"failed to save "+rowId,Toast.LENGTH_SHORT).show();
                    }
                }
                checkComplete();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Batch 17
        databaseReference17.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseHelper17.truncateTable();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                    User dummy=dataSnapshot1.getValue(User.class);
                    long rowId = databaseHelper17.insertData(dummy.getId(), dummy.getName(),dummy.getMobile(),dummy.getHome(),dummy.getBatch(),dummy.getBlood(),dummy.getImageUrl(),dummy.getEmail(),dummy.getFacebook());
                    if (rowId>0)
                    {
                        //Toast.makeText(getApplicationContext(), "17 saved at row " + rowId, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Toast.makeText(getApplicationContext(),"failed to save "+rowId,Toast.LENGTH_SHORT).show();
                    }
                }
                checkComplete();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Batch 18
        databaseReference18.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseHelper18.truncateTable();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                    User dummy=dataSnapshot1.getValue(User.class);
                    long rowId = databaseHelper18.insertData(dummy.getId(), dummy.getName(),dummy.getMobile(),dummy.getHome(),dummy.getBatch(),dummy.getBlood(),dummy.getImageUrl(),dummy.getEmail(),dummy.getFacebook());
                    if (rowId>0)
                    {
                        //Toast.makeText(getApplicationContext(), "18 saved at row " + rowId, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Toast.makeText(getApplicationContext(),"failed to save "+rowId,Toast.LENGTH_SHORT).show();
                    }
                }
                checkComplete();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Batch 19
        databaseReference19.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseHelper19.truncateTable();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                    User dummy=dataSnapshot1.getValue(User.class);
                    long rowId = databaseHelper19.insertData(dummy.getId(), dummy.getName(),dummy.getMobile(),dummy.getHome(),dummy.getBatch(),dummy.getBlood(),dummy.getImageUrl(),dummy.getEmail(),dummy.getFacebook());
                    if (rowId>0)
                    {
                        //Toast.makeText(getApplicationContext(), "19 saved at row " + rowId, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Toast.makeText(getApplicationContext(),"failed to save "+rowId,Toast.LENGTH_SHORT).show();
                    }
                }
                checkComplete();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void checkComplete()
    {
        cnt++;
        //Toast.makeText(getApplicationContext()," "+cnt,Toast.LENGTH_SHORT).show();
        if(cnt==7)
        {
            if (downloadProgress.isShowing())
            {
                downloadProgress.dismiss();
                Toast.makeText(getApplicationContext(),"Download Complete!!",Toast.LENGTH_SHORT).show();
            }
            cnt=0;
        }
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.nav_profile_id)
        {
           Intent intent =new Intent(HomeActivity.this,MyProfileActivity.class);
           startActivity(intent);
        }
        if (item.getItemId()==R.id.nav_log_out_id)
        {
            final AlertDialog.Builder ADB;
            ADB= new AlertDialog.Builder(HomeActivity.this);
            View mView=getLayoutInflater().inflate(R.layout.alert_dialog_log_out,null);
            TextView H=mView.findViewById(R.id.out_alert_heading);
            TextView M=mView.findViewById(R.id.out_alert_message);
            M.setText("Do you really want to log out?");
            TextView Neutral,Positive;
            Neutral=mView.findViewById(R.id.out_alert_neutral);
            Positive=mView.findViewById(R.id.out_alert_positive);
            Neutral.setText("No");
            Positive.setText("Yes");
            Neutral.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //for cancel Alert Dialog variable should be defined globally
                    AD.dismiss();
                    Toast.makeText(HomeActivity.this,"Welcome Back",Toast.LENGTH_SHORT).show();
                }
            });
            Positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("user_state","0");
                    editor.commit();
                    Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                    //finishing all previous activity
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
            ADB.setView(mView);
            AD = ADB.create();
            AD.show();
        }

        if (item.getItemId() == R.id.nav_admin_id) {
            PD.show();
            firebaseAuth.signInWithEmailAndPassword(Email,Password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            // successfully log in,,password is not changed
                            //now get the key,
                            String key=makeKeyFromEmail(Email);
                            //by the key we will retreve current user's data,,, (to check whether he is admin)
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(Batch).child(key);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getValue()==null)
                                    {
                                        PD.dismiss();
                                        //Email or Password is changed
                                        //So this changed email and password don't match with Stored email and password
                                        makeText(getApplicationContext(),"Log In again...", LENGTH_SHORT).show();
                                        //change state to 0,,, so that user can not auto log in without log in again
                                        editor.putString("user_state","0");
                                        editor.commit();
                                        //So, throw user to Log In page
                                        Intent intent= new Intent(HomeActivity.this,MainActivity.class);
                                        //finishing all previous activity
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        String isAdmin=dataSnapshot.child("isAdmin").getValue(String.class);
                                        String name=dataSnapshot.child("name").getValue(String.class);
                                        //Toast.makeText(getApplicationContext(),isAdmin,Toast.LENGTH_SHORT).show();
                                        //isAdmin == 1, current user is an Admin
                                        //isAdmin == 0, current user is not an Admin
                                        if (isAdmin.equals("1"))
                                        {
                                            Toast.makeText(getApplicationContext(),"Welcome " +name, LENGTH_SHORT).show();
                                            PD.dismiss();
                                            Intent intent=new Intent(HomeActivity.this,ModRequestActivityAdmin.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            PD.dismiss();
                                            Toast.makeText(getApplicationContext(),"Sorry, You are not Admin.", LENGTH_SHORT).show();
                                        }
                                    }
                                    }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    PD.dismiss();
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
                                Intent intent= new Intent(HomeActivity.this,MainActivity.class);
                                //finishing all previous activity
                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    });
        }
        if (item.getItemId() == R.id.nav_moderator_id) {
            PD.show();
            firebaseAuth.signInWithEmailAndPassword(Email,Password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            // successfully log in, password is not changed
                            //now get the key,
                            String key=makeKeyFromEmail(Email);
                            //by the key we will retreve current user's data,,, (to check whether he is admin)
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(Batch).child(key);
                            //checking whether my data is deleted my admin
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getValue()==null)
                                    {
                                        PD.dismiss();
                                        //Email or Password is changed
                                        //So this changed email and password don't match with Stored email and password
                                        makeText(getApplicationContext(),"Log In again...", LENGTH_SHORT).show();
                                        //change state to 0,,, so that user can not auto log in without log in again
                                        editor.putString("user_state","0");
                                        editor.commit();
                                        //So, throw user to Log In page
                                        Intent intent= new Intent(HomeActivity.this,MainActivity.class);
                                        //finishing all previous activity
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Intent intent=new Intent(HomeActivity.this,ModeratorActivity.class);
                                        startActivity(intent);
                                        PD.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    PD.dismiss();
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
                                Toast.makeText(getApplicationContext(),"Check Your Internet Connection & Try Again later.", LENGTH_SHORT).show();
                                PD.dismiss();
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
                                Intent intent= new Intent(HomeActivity.this,MainActivity.class);
                                //finishing all previous activity
                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    });
        }
        if (item.getItemId() == R.id.nav_report_id) {
            PD.show();
            firebaseAuth.signInWithEmailAndPassword(Email,Password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            String key=makeKeyFromEmail(Email);
                            //by the key we will retreve current user's data,,, (to check whether he is admin)
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(Batch).child(key);
                            //checking whether my data is deleted my admin
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getValue()==null)
                                    {
                                        PD.dismiss();
                                        //Email or Password is changed
                                        //So this changed email and password don't match with Stored email and password
                                        makeText(getApplicationContext(),"Log In again...", LENGTH_SHORT).show();
                                        //change state to 0,,, so that user can not auto log in without log in again
                                        editor.putString("user_state","0");
                                        editor.commit();
                                        //So, throw user to Log In page
                                        Intent intent= new Intent(HomeActivity.this,MainActivity.class);
                                        //finishing all previous activity
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        PD.dismiss();
                                        Intent intent = new Intent(HomeActivity.this, ReportWriteActivity.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    PD.dismiss();
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
                                Intent intent= new Intent(HomeActivity.this,MainActivity.class);
                                //finishing all previous activity
                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    });
        }
        if (item.getItemId() == R.id.nav_share_id) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            String shareBody = getResources().getString(R.string.share_message);
            //share.putExtra(Intent.EXTRA_SUBJECT,"subject here...");
            share.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(share, "Share via"));
        }
        if (item.getItemId() == R.id.nav_about_id) {
            Intent intent=new Intent(HomeActivity.this,AboutActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.nav_help_id) {
            Intent intent=new Intent(HomeActivity.this,HelpActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.nav_exit_id)
        {
            onBackPressed();
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder ADB;
        ADB= new AlertDialog.Builder(HomeActivity.this);
        View mView=getLayoutInflater().inflate(R.layout.alert_dialog_exit,null);
        TextView H=mView.findViewById(R.id.exit_alert_heading);
        TextView M=mView.findViewById(R.id.exit_alert_message);
        M.setText("Do you really want to exit?");
        TextView Neutral,Positive;
        Neutral=mView.findViewById(R.id.exit_alert_neutral);
        Positive=mView.findViewById(R.id.exit_alert_positive);
        Neutral.setText("No");
        Positive.setText("Yes");
        Neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for cancel Alert Dialog variable should be defined globally
                AD.dismiss();
                Toast.makeText(HomeActivity.this,"Welcome Back",Toast.LENGTH_SHORT).show();
            }
        });
        Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ADB.setView(mView);
        AD = ADB.create();
        AD.show();
        //super.onBackPressed();
    }
}
