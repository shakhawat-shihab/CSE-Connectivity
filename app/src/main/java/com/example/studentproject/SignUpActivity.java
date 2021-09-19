package com.example.studentproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText ID,NAME,MOBILE,EMAIL,PASSWORD;
    private AutoCompleteTextView HOMETOWN;
    private Spinner BATCH, BLOOD;
    private Button B;
    private DatabaseReference databaseReference;
    private String[] blood,district,batch;
    private String key,Id, Name, Mobile, Home, Batch, Blood, Email, Password;
    ProgressDialog PD;
    AlertDialog AD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("CSE Connectivity");
        PD = new ProgressDialog(SignUpActivity.this);
        PD.setTitle("Signing up");
        PD.setMessage("Please wait...");
        PD.setCancelable(false);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("account_request");
        ID=findViewById(R.id.data_id);
        NAME=findViewById(R.id.data_name);
        MOBILE=findViewById(R.id.data_mobile);
        HOMETOWN=findViewById(R.id.data_hometown);
        BATCH=findViewById(R.id.data_batch);
        BLOOD=findViewById(R.id.data_blood);
        EMAIL=findViewById(R.id.data_email);
        PASSWORD=findViewById(R.id.data_password);
        B=findViewById(R.id.data_sign_up);
        district=getResources().getStringArray(R.array.district_name);
        ArrayAdapter<String> adapter_home = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,district);
        HOMETOWN.setAdapter(adapter_home);
        batch=getResources().getStringArray(R.array.batch_list);
        ArrayAdapter<String>adapter_batch=new ArrayAdapter<String>(this,R.layout.spinner_text,batch);
        BATCH.setAdapter(adapter_batch);
        blood=getResources().getStringArray(R.array.blood_group);
        ArrayAdapter<String>adapter_blood=new ArrayAdapter<String>(this,R.layout.spinner_text,blood);
        BLOOD.setAdapter(adapter_blood);
        B.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.data_sign_up)
        {
            Id = ID.getText().toString().trim();
            Name = NAME.getText().toString().trim();
            Mobile = MOBILE.getText().toString().trim();
            Home = HOMETOWN.getText().toString().trim();
            Batch = batch[BATCH.getSelectedItemPosition()];
            Blood = blood[BLOOD.getSelectedItemPosition()];
            Email = EMAIL.getText().toString().trim();
            Password = PASSWORD.getText().toString().trim();
            key=makeKeyFromEmail(Email);
            if (Id.equals("")||Id.charAt(0) != 'C' || Id.charAt(1) != 'E' || (Id.charAt(2) != '0' && Id.charAt(2) != '1' && Id.charAt(2) != '2') || Id.length() != 7) {
                ID.setError("The Format is 'CE17054' ");
                ID.requestFocus();
                return;
            }
            if (Name.length() < 8) {
                NAME.setError("Enter Full Name");
                NAME.requestFocus();
                return;
            }
            if (Mobile.length() != 11) {
                MOBILE.setError("Mobile Number must be 11 digits");
                MOBILE.requestFocus();
                return;
            }
            if (Batch.equals("null")) {
                AlertDialog.Builder ADB1;
                ADB1 = new AlertDialog.Builder(SignUpActivity.this);
                ADB1.setTitle("Warning");
                ADB1.setIcon(R.drawable.ic_warning);
                ADB1.setMessage("Batch Should not be null");
                AlertDialog AD1 = ADB1.create();
                AD1.show();
                return;
            }
            if (Blood.equals("null")) {
                AlertDialog.Builder ADB1;
                ADB1 = new AlertDialog.Builder(SignUpActivity.this);
                ADB1.setTitle("Warning");
                ADB1.setIcon(R.drawable.ic_warning);
                ADB1.setMessage("Blood Group Should not be null");
                AlertDialog AD1 = ADB1.create();
                AD1.show();
                return;
            }
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
            PD.show();
            //encrypting the password
            String secretKey = "mbstuCseSecretKey";
            Password = AES.encrypt(Password, secretKey) ;
            DatabaseReference ref= databaseReference.child(Batch).child(key);
            User user =new User(Id, Name, Mobile, Home, Batch, Blood, Email, Password);
            ref.setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            AlertDialog.Builder ADB3;
                            ADB3= new AlertDialog.Builder(SignUpActivity.this);
                            View mView=getLayoutInflater().inflate(R.layout.alert_dialog_sign_up,null);
                            final TextView H=mView.findViewById(R.id.sign_alert_heading);
                            final TextView M=mView.findViewById(R.id.sign_alert_message);
                            final TextView Pos_B=mView.findViewById(R.id.sign_alert_positive);
                            Pos_B.setText("Ok");
                            Pos_B.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AD.dismiss();
                                    Toast.makeText(getApplicationContext(),"Keep checking your mail box or spam for verification email",Toast.LENGTH_LONG).show();
                                }
                            });
                            M.setText("A verification email will send to you, When our Moderator accept your request.");
                            ADB3.setView(mView);
                            AD = ADB3.create();
                            AD.show();
                            PD.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to upload",Toast.LENGTH_LONG).show();
                            PD.dismiss();
                        }
                    });

        }
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
