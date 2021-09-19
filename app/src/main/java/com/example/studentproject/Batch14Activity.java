package com.example.studentproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Batch14Activity extends AppCompatActivity {
    private RecyclerView RV;
    private ProgressBar PB;
    DatabaseHelper14 databaseHelper_14;
    public List<User> userList;
    MyAdapter myAdapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch14);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("14th Batch");
        userList=new ArrayList<>();
        PB=findViewById(R.id.batch_14_progress_bar);
        PB.setVisibility(View.GONE);
        databaseHelper_14=new DatabaseHelper14(this);
        RV=findViewById(R.id.recycler_id_14);
        RV.setHasFixedSize(true);
        Display display=getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics= new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density=getResources().getDisplayMetrics().density;
        float dpHeight=outMetrics.heightPixels/density;
        float dpWidth=outMetrics.widthPixels/density;
        int num=(int) dpWidth/106;
        //Toast.makeText(getApplicationContext(),dpWidth+" "+num,Toast.LENGTH_SHORT).show();
        RV.setLayoutManager(new GridLayoutManager(this,num));
        showAll();

    }

    private void showAll() {
        Cursor c = databaseHelper_14.showAllData_s_17();
        while (c.moveToNext()) {
            User user = new User(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4),
                    c.getString(5), c.getString(6),c.getString(7),c.getString(8));
            userList.add(user);
        }
        Cursor cc = databaseHelper_14.showAllData_s_16();
        while (cc.moveToNext()) {
            User user = new User(cc.getString(0), cc.getString(1), cc.getString(2), cc.getString(3), cc.getString(4),
                    cc.getString(5), cc.getString(6),cc.getString(7),cc.getString(8));
            userList.add(user);
        }
        Cursor d = databaseHelper_14.showAllData_s_15();
        while (d.moveToNext()) {
            User user = new User(d.getString(0), d.getString(1), d.getString(2), d.getString(3), d.getString(4),
                    d.getString(5), d.getString(6),d.getString(7),d.getString(8));
            userList.add(user);
        }
        Cursor e = databaseHelper_14.showOther();
        while (e.moveToNext()) {
            User user = new User(e.getString(0), e.getString(1), e.getString(2), e.getString(3), e.getString(4),
                    e.getString(5), e.getString(6),e.getString(7),e.getString(8));
            userList.add(user);
        }
        myAdapter = new MyAdapter(Batch14Activity.this, userList);
        RV.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id = userList.get(position).getId();
                String name = userList.get(position).getName();
                String mobile =userList.get(position).getMobile();
                String home = userList.get(position).getHome();
                String batch = userList.get(position).getBatch();
                String blood = userList.get(position).getBlood();
                String url = userList.get(position).getImageUrl();
                String email = userList.get(position).getEmail();
                String facebook = userList.get(position).getFacebook();
                Intent intent = new Intent(Batch14Activity.this, ShowInfoActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("mobile", mobile);
                intent.putExtra("home", home);
                intent.putExtra("batch", batch);
                intent.putExtra("blood", blood);
                intent.putExtra("url", url);
                intent.putExtra("email", email);
                intent.putExtra("facebook", facebook);
                startActivity(intent);
            }
        });

    }
}
