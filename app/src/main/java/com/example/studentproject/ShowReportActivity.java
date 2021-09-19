package com.example.studentproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ShowReportActivity extends AppCompatActivity {

    private TextView ID,NAME,MOBILE,SUBJECT,DESCRIPTION;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Report");
        ID=findViewById(R.id.report_show_id_user);
        NAME=findViewById(R.id.report_show_name_user);
        MOBILE=findViewById(R.id.report_show_mobile_user);
        SUBJECT=findViewById(R.id.report_show_subject);
        DESCRIPTION=findViewById(R.id.report_show_description);
        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        String name=intent.getStringExtra("name");
        String mobile=intent.getStringExtra("mobile");
        String subject=intent.getStringExtra("subject");
        String description=intent.getStringExtra("description");
        ID.setText("ID : "+id);
        NAME.setText("Name : "+name);
        MOBILE.setText("Mobile No. : "+mobile);
        SUBJECT.setText("Subject : "+subject);
        DESCRIPTION.setText(description);

    }
}
