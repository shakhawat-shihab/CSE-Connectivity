package com.example.studentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class ReportWriteActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences SP;
    SharedPreferences.Editor editor;
    private TextView C_ID,C_NAME,C_MOBILE;
    private EditText ET_Id,ET_Subject,ET_Description;
    private Spinner SPN_Batch;
    private String[] batch;
    private Button B;
    private ProgressDialog PD;
    String sp_email,sp_password,sp_batch;
    String id,name,mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_write);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Report");
        PD = new ProgressDialog(ReportWriteActivity.this);
        PD.setTitle("Submitting");
        PD.setMessage("Please wait...");
        PD.setCancelable(true);
        SP=getSharedPreferences("User", Context.MODE_PRIVATE);
        editor=SP.edit();
        sp_batch=SP.getString("user_batch","not found!");
        sp_email=SP.getString("user_email","not found!");
        sp_password=SP.getString("user_password","not found!");
        //the stored password was encrypted, so we need to decrypt it
        //sp_password=decryptPassword(sp_password);
        String secretKey = "mbstuCseSecretKey";
        sp_password=AES.decrypt(sp_password, secretKey) ;
        C_ID=findViewById(R.id.report_write_id_user);
        C_NAME=findViewById(R.id.report_write_name_user);
        C_MOBILE=findViewById(R.id.report_write_mobile_user);
        //ET_Id=findViewById(R.id.report_write_id);
        ET_Subject=findViewById(R.id.report_write_subject);
        ET_Description=findViewById(R.id.report_write_description);
        //SPN_Batch=findViewById(R.id.report_write_batch);
       // batch=getResources().getStringArray(R.array.batch_list);
        //ArrayAdapter<String> adapter_batch=new ArrayAdapter<String>(this,R.layout.spinner_text,batch);
        //SPN_Batch.setAdapter(adapter_batch);
        B=findViewById(R.id.report_write_submit);
        B.setOnClickListener(this);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("users").child(sp_batch).child(makeKeyFromEmail(sp_email));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                id=dataSnapshot.child("id").getValue(String.class);
                name=dataSnapshot.child("name").getValue(String.class);
                mobile=dataSnapshot.child("mobile").getValue(String.class);
                C_ID.setText("ID : "+id);
                C_NAME.setText("Name : "+name);
                C_MOBILE.setText("Mobile : "+mobile);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database Error "+databaseError, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.report_write_submit)
        {
            //String r_id=ET_Id.getText().toString().trim();
            String r_subject=ET_Subject.getText().toString().trim();
            String r_description=ET_Description.getText().toString().trim();
            //String r_batch=batch[SPN_Batch.getSelectedItemPosition()];

            if (r_subject.equals(""))
            {
                ET_Subject.setError("can't be null");
                ET_Subject.requestFocus();
                return;
            }
            if (id.equals("")||name.equals("")||mobile.equals(""))
            {
                //the person who is submitting report,
                //this person's information is failed to get
                AlertDialog.Builder ADB1;
                ADB1 = new AlertDialog.Builder(ReportWriteActivity.this);
                ADB1.setTitle("Warning");
                ADB1.setIcon(R.drawable.ic_warning);
                ADB1.setMessage("Sorry failed to load your information");
                AlertDialog AD1 = ADB1.create();
                AD1.show();
                return;
            }
            PD.show();
            DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("Reports");
            Report report=new Report(r_subject,r_description,id,name,mobile);
            databaseReference1.push().setValue(report).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(),"Your report is submitted to admin", LENGTH_SHORT).show();
                    PD.dismiss();
                }
            });
            /*databaseReference1.child("batch").setValue(r_batch);
            databaseReference1.child("subject").setValue(r_subject);
            databaseReference1.child("description").setValue(r_description);
            databaseReference1.child("reportBy_id").setValue(id);
            databaseReference1.child("reportBy_name").setValue(name);
            databaseReference1.child("reportBy_mobile").setValue(mobile);*/
        }
    }
}
