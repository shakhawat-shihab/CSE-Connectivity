package com.example.studentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class DeleteActivity extends AppCompatActivity implements View.OnClickListener {
    EditText EMAIL;
    Spinner BATCH;
    Button SHOW,DELETE;
    TextView ID,NAME,MOBILE,HOME,BLOOD;
    private String[] batch;
    ImageView IV;
    DatabaseReference databaseReference;
    ProgressBar PB;
    LinearLayout LL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Delete");
        LL=findViewById(R.id.linear_layout);
        LL.setVisibility(LinearLayout.GONE);
        PB=findViewById(R.id.progress_show_delete);
        PB.setVisibility(View.GONE);
        EMAIL=findViewById(R.id.delete_email);
        BATCH=findViewById(R.id.delete_batch);
        SHOW=findViewById(R.id.delete_show);
        DELETE=findViewById(R.id.delete_delete);
        IV=findViewById(R.id.image_show_delete);
        ID=findViewById(R.id.id_show_delete);
        NAME=findViewById(R.id.name_show_delete);
        MOBILE=findViewById(R.id.mobile_show_delete);
        HOME=findViewById(R.id.home_show_delete);
        BLOOD=findViewById(R.id.blood_show_delete);
        batch=getResources().getStringArray(R.array.batch_list);
        ArrayAdapter<String> adapter_batch=new ArrayAdapter<String>(this,R.layout.spinner_text,batch);
        BATCH.setAdapter(adapter_batch);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        SHOW.setOnClickListener(this);
        DELETE.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.delete_show)
        {
            String Email=EMAIL.getText().toString().trim();
            String Batch = batch[BATCH.getSelectedItemPosition()];
            String key=makeKeyFromEmail(Email);
            PB.setVisibility(View.VISIBLE);
            databaseReference.child(Batch).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        makeText(getApplicationContext(),"No such data", LENGTH_SHORT).show();
                        PB.setVisibility(View.GONE);
                        return;
                    }
                    PB.setVisibility(View.GONE);
                    LL.setVisibility(LinearLayout.VISIBLE);
                    String Image=dataSnapshot.child("imageUrl").getValue(String.class);
                    Picasso.get().load(Image).placeholder(R.drawable.default_pic).into(IV);
                    String Id=dataSnapshot.child("id").getValue(String.class);
                    String Name=dataSnapshot.child("name").getValue(String.class);
                    String Mobile=dataSnapshot.child("mobile").getValue(String.class);
                    String Home=dataSnapshot.child("home").getValue(String.class);
                    String Blood=dataSnapshot.child("blood").getValue(String.class);

                    ID.setText(Id);
                    NAME.setText(Name);
                    MOBILE.setText(Mobile);
                    HOME.setText(Home);
                    BLOOD.setText(Blood);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if(v.getId()==R.id.delete_delete)
        {
            String Email=EMAIL.getText().toString().trim();
            String Batch = batch[BATCH.getSelectedItemPosition()];
            String key=makeKeyFromEmail(Email);
            databaseReference.child(Batch).child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(),"Successfully Deleted",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext()," Not Successfully Deleted",Toast.LENGTH_SHORT).show();
                    }
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
