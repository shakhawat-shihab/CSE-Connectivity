package com.example.studentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestActivityAdmin extends AppCompatActivity {
    SharedPreferences SP;
    SharedPreferences.Editor editor;
    ProgressDialog PD;
    private RequestAdapter requestAdapter;
    private TextView TITLE;
    private ProgressBar PB;
    private RecyclerView RV;
    private List<User> userList;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference1,databaseReference2;
    String Batch;
    String sp_email,sp_password;
    String selectedBatch;
    User user;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Admin");
        RV=findViewById(R.id.recycler_view_id_admin1);
        RV.setHasFixedSize(true);
        RV.setLayoutManager(new LinearLayoutManager(RequestActivityAdmin.this));
        TITLE=findViewById(R.id.text_view_id_admin1);
        PB=findViewById(R.id.progress_bar_id_admin1);
        RV=findViewById(R.id.recycler_view_id_admin1);
        PD = new ProgressDialog(RequestActivityAdmin.this);
        PD.setTitle("Registering");
        PD.setMessage("Please wait...");
        PD.setCancelable(false);
        userList=new ArrayList<>();
        SP=getSharedPreferences("User", Context.MODE_PRIVATE);
        editor=SP.edit();
        Batch=SP.getString("user_batch","not found!");
        sp_email=SP.getString("user_email","not found!");
        sp_password=SP.getString("user_password","not found!");
        //the stored password was encrypted, so we need to decrypt it
        //sp_password=decryptPassword(sp_password);
        String secretKey = "mbstuCseSecretKey";
        sp_password=AES.decrypt(sp_password, secretKey) ;
        mAuth=FirebaseAuth.getInstance();
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("account_request");

        //by default showing the 13th Batch
        TITLE.setText("Requests of 13th Batch");
        getDataBySelectedBatch("13th Batch");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_admin1,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.menu_admin_batch_13_id)
        {
            TITLE.setText("Requests of 13th Batch");
            getDataBySelectedBatch("13th Batch");
        }
        if (item.getItemId()==R.id.menu_admin_batch_14_id)
        {
            TITLE.setText("Requests of 14th Batch");
            getDataBySelectedBatch("14th Batch");
        }
        if (item.getItemId()==R.id.menu_admin_batch_15_id)
        {
            TITLE.setText("Requests of 15th Batch");
            getDataBySelectedBatch("15th Batch");
        }
        if (item.getItemId()==R.id.menu_admin_batch_16_id)
        {
            TITLE.setText("Requests of 16th Batch");
            getDataBySelectedBatch("16th Batch");
        }
        if (item.getItemId()==R.id.menu_admin_batch_17_id)
        {
            TITLE.setText("Requests of 17th Batch");
            getDataBySelectedBatch("17th Batch");
        }
        if (item.getItemId()==R.id.menu_admin_batch_18_id)
        {
            TITLE.setText("Requests of 18th Batch");
            getDataBySelectedBatch("18th Batch");
        }
        if (item.getItemId()==R.id.menu_admin_batch_19_id)
        {
            TITLE.setText("Requests of 19th Batch");
            getDataBySelectedBatch("19th Batch");
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDataBySelectedBatch(final String s) {
        selectedBatch=s;
        databaseReference1.child(selectedBatch).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                PB.setVisibility(View.GONE);
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    User user=dataSnapshot1.getValue(User.class);
                    userList.add(user);
                    user.setKey(dataSnapshot1.getKey());
                }
                requestAdapter=new RequestAdapter(RequestActivityAdmin.this,userList);
                RV.setAdapter(requestAdapter);
                requestAdapter.setOnItemClickListener(new RequestAdapter.OnItemClickListener() {
                    @Override
                    public void onAcceptRequest(int position) {
                        PD.show();
                        User selectedItem=userList.get(position);
                        //final String key_selectedItem=selectedItem.getKey();
                        String id=selectedItem.getId();
                        String name=selectedItem.getName();
                        String mobile=selectedItem.getMobile();
                        String home=selectedItem.getHome();
                        String batch=selectedItem.getBatch();
                        String blood=selectedItem.getBlood();
                        final String email=selectedItem.getEmail();
                        final String key=makeKeyFromEmail(email);
                        password=selectedItem.getPassword();
                        String secretKey = "mbstuCseSecretKey";
                        password = AES.decrypt(password, secretKey) ;
                        //setting default image
                        //we are taking last char of Id and evaluating his default image
                        char c=id.charAt(id.length()-1);
                        int defaultImage;
                        if (c>='0'&&c<='9')
                        {
                            int i=c-'0';
                            defaultImage=i%4;
                        }
                        else
                        {
                            defaultImage=1;
                        }
                        //User(id,name,mobile,home,batch,blood,email,isModerator,isAdmin,imageUrl)
                         user= new User(id,name,mobile,home,batch,blood,email,"0","0",Integer.toString(defaultImage),"empty");
                        //adding new data in  real time data base
                        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("users").child(selectedBatch);
                        //check whether this email is aleady used as a key,,,
                        databaseReference2.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() == null) {
                                    //There is no data under this email,,
                                    //create user
                                    //if successfully able to create user then add to database
                                    createUserAuth(email,password);
                                    /*databaseReference2.child(key).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(),"Added to database",Toast.LENGTH_SHORT).show();
                                        }
                                    });*/
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "A account under this email already exist.", Toast.LENGTH_SHORT).show();
                                    PD.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Database Error "+databaseError, Toast.LENGTH_SHORT).show();
                                PD.dismiss();
                            }
                        });
                    }
                    @Override
                    public void onIgnoreRequest(int position) {
                        //getting the clicked user's request in selected item
                        User selectedItem=userList.get(position);
                        //Toast.makeText(getApplicationContext(),selectedItem.getEmail(),Toast.LENGTH_SHORT).show();
                        String key=makeKeyFromEmail(selectedItem.getEmail());
                        databaseReference1.child(selectedBatch).child(key).removeValue();
                    }

                    @Override
                    public void onItemClick(int position) {
                        User selectedItem=userList.get(position);
                        String id=selectedItem.getId();
                        String name=selectedItem.getName();
                        String mobile=selectedItem.getMobile();
                        String batch=selectedItem.getBatch();
                        String blood=selectedItem.getBlood();
                        String email=selectedItem.getEmail();
                        AlertDialog.Builder ADB;
                        ADB= new AlertDialog.Builder(RequestActivityAdmin.this);
                        ADB.setMessage("Id : " +id+"\n"+"Name : " +name+"\n"+"Mobile : " +mobile+"\n" +"Batch : " +batch+"\n"+"Blood : " +blood+"\n"+"Email : " +email+"\n" );
                        /*ADB.setMessage("Id :" +id+"\n");
                        ADB.setMessage("Name :" +name+"\n");
                        ADB.setMessage("Mobile :" +mobile+"\n");
                        ADB.setMessage("Batch :" +batch+"\n");
                        ADB.setMessage("Blood :" +blood+"\n");
                        ADB.setMessage("Email :" +email+"\n");*/
                        AlertDialog AD = ADB.create();
                        AD.show();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database Error "+databaseError, Toast.LENGTH_SHORT).show();
                PD.dismiss();
            }
        });

    }

    private void logIn(String sp_email, String sp_password) {
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(sp_email,sp_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //Toast.makeText(getApplicationContext(),"Log in successfully in My Account"+ authResult,Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "Checking after my log in: "+firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                PD.dismiss();
            }
        });

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
    private void createUserAuth(String email, String password) {
        final String key=makeKeyFromEmail(email);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Successfully registered!", Toast.LENGTH_SHORT).show();
                                databaseReference2.child(key).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),"Added to database",Toast.LENGTH_SHORT).show();

                                    }
                                });
                                databaseReference1.child(selectedBatch).child(key).removeValue();
                                //trying to get current log in
                                //FA is used for testing current user
                                // FirebaseAuth FA=FirebaseAuth.getInstance();
                                //Toast.makeText(getApplicationContext(), "Checking : "+FA.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                //log in Again in moderator account (as the current user is changed )
                                logIn(sp_email,sp_password);
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Failed to Approve "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    PD.dismiss();
                }
            }
        });
    }
}
