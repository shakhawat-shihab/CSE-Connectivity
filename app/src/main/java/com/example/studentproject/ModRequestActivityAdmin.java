package com.example.studentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ModRequestActivityAdmin extends AppCompatActivity {
    SharedPreferences SP;
    SharedPreferences.Editor editor;
    ProgressDialog PD;
    private RecyclerView RV;
    private RequestAdapter requestAdapter;
    private ProgressBar PB;
    private List<User> userList;
    String Batch;
    String sp_email,sp_password;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_request_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Admin");
        PB=findViewById(R.id.progress_bar_id_admin);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("moderator_request");
        RV=findViewById(R.id.recycler_view_id_admin);
        RV.setHasFixedSize(true);
        RV.setLayoutManager(new LinearLayoutManager(ModRequestActivityAdmin.this));
        PD = new ProgressDialog(ModRequestActivityAdmin.this);
        PD.setTitle("Registering");
        PD.setMessage("Please wait...");
        PD.setCancelable(false);
        SP=getSharedPreferences("User", Context.MODE_PRIVATE);
        editor=SP.edit();
        Batch=SP.getString("user_batch","not found!");
        sp_email=SP.getString("user_email","not found!");
        sp_password=SP.getString("user_password","not found!");
        //the stored password was encrypted, so we need to decrypt it
        //sp_password=decryptPassword(sp_password);
        String secretKey = "mbstuCseSecretKey";
        sp_password=AES.decrypt(sp_password, secretKey);

        userList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
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
                requestAdapter=new RequestAdapter(ModRequestActivityAdmin.this,userList);
                RV.setAdapter(requestAdapter);
                requestAdapter.setOnItemClickListener(new RequestAdapter.OnItemClickListener() {
                    @Override
                    public void onAcceptRequest(int position) {
                        PD.show();
                        User selectedItem=userList.get(position);
                        String id=selectedItem.getId();
                        String name=selectedItem.getName();
                        String mobile=selectedItem.getMobile();
                        String email=selectedItem.getEmail();
                        String batch=selectedItem.getBatch();
                        final String key=makeKeyFromEmail(email);
                        DatabaseReference data=FirebaseDatabase.getInstance().getReference().child("users").child(batch).child(key);
                        data.child("isModerator").setValue("1").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                PD.dismiss();
                                Toast.makeText(getApplicationContext(),"Approved as moderator",Toast.LENGTH_SHORT).show();
                                databaseReference.child(key).removeValue();
                            }
                        });
                    }

                    @Override
                    public void onIgnoreRequest(int position) {
                        User selectedItem=userList.get(position);
                        //Toast.makeText(getApplicationContext(),selectedItem.getEmail(),Toast.LENGTH_SHORT).show();
                        String key=makeKeyFromEmail(selectedItem.getEmail());
                        databaseReference.child(key).removeValue();
                    }
                    @Override
                    public void onItemClick(int position) {
                        User selectedItem=userList.get(position);
                        String id=selectedItem.getId();
                        String name=selectedItem.getName();
                        String mobile=selectedItem.getMobile();
                        String batch=selectedItem.getBatch();
                        String email=selectedItem.getEmail();
                        AlertDialog.Builder ADB;
                        ADB= new AlertDialog.Builder(ModRequestActivityAdmin.this);
                        ADB.setMessage("Id : " +id+"\n"+"Name : " +name+"\n"+"Mobile : " +mobile+"\n" +"Batch : " +batch+"\n"+"Email : " +email+"\n" );
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_admin,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.menu_admin_moderator_request)
        {
            Intent intent=new Intent(ModRequestActivityAdmin.this,RequestActivityAdmin.class);
            startActivity(intent);
        }
        if (item.getItemId()==R.id.menu_admin_report)
        {
            Intent intent=new Intent(ModRequestActivityAdmin.this, ReportViewRecyclerActivity.class);
            startActivity(intent);
        }
        if (item.getItemId()==R.id.menu_admin_delete)
        {
            Intent intent=new Intent(ModRequestActivityAdmin.this, DeleteActivity.class);
            startActivity(intent);
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
