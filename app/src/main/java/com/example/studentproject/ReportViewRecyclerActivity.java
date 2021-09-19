package com.example.studentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReportViewRecyclerActivity extends AppCompatActivity {
    private RecyclerView RV;
    ProgressDialog PD;
    private ProgressBar PB;
    private DatabaseReference databaseReference,databaseReference1;
    private List<Report> reportList;
    private ReportAdapter reportAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view_recycler);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Report");
        RV=findViewById(R.id.recycler_view_id_report);
        RV.setHasFixedSize(true);
        RV.setLayoutManager(new LinearLayoutManager(ReportViewRecyclerActivity.this));
        PB=findViewById(R.id.progress_bar_id_report);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Reports");
        reportList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reportList.clear();
                PB.setVisibility(View.GONE);
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Report report=dataSnapshot1.getValue(Report.class);
                    reportList.add(report);
                    report.setKey(dataSnapshot1.getKey());
                }
                reportAdapter=new ReportAdapter(ReportViewRecyclerActivity.this,reportList);
                RV.setAdapter(reportAdapter);
                reportAdapter.setOnItemClickListener(new ReportAdapter.OnItemClickListener() {
                    @Override
                    public void onDelete(int position) {
                        Report selectedItem=reportList.get(position);
                        String key=selectedItem.getKey();
                        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Reports");
                        databaseReference1.child(key).removeValue();
                    }

                    @Override
                    public void onItemClick(int position) {
                        Report selectedItem=reportList.get(position);
                        String id=selectedItem.getReportBy_id();
                        String name=selectedItem.getReportBy_name();
                        String mobile=selectedItem.getReportBy_mobile();
                        String subject=selectedItem.getSubject();
                        String description=selectedItem.getDescription();
                        Intent intent = new Intent(ReportViewRecyclerActivity.this, ShowReportActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("name", name);
                        intent.putExtra("mobile", mobile);
                        intent.putExtra("subject", subject);
                        intent.putExtra("description", description);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error "+databaseError,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
