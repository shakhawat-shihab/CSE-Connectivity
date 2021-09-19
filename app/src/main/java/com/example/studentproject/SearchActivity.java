package com.example.studentproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {
    private  int selected,chosen;
    private TextView TV;
    private Spinner SP;
    private RecyclerView RV;
    private SearchView SV;
    private String[] searchOption;
    private Typeface TF1;
    DatabaseHelper13 myDatabaseHelp13;
    DatabaseHelper14 myDatabaseHelp14;
    DatabaseHelper15 myDatabaseHelp15;
    DatabaseHelper16 myDatabaseHelp16;
    DatabaseHelper17 myDatabaseHelp17;
    DatabaseHelper18 myDatabaseHelp18;
    DatabaseHelper19 myDatabaseHelp19;
    MyAdapterWithSearch myAdapterWithSearch=null;
    public List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Search");
        myDatabaseHelp13=new DatabaseHelper13(this);
        myDatabaseHelp14=new DatabaseHelper14(this);
        myDatabaseHelp15=new DatabaseHelper15(this);
        myDatabaseHelp16=new DatabaseHelper16(this);
        myDatabaseHelp17=new DatabaseHelper17(this);
        myDatabaseHelp18=new DatabaseHelper18(this);
        myDatabaseHelp19=new DatabaseHelper19(this);
        userList =new ArrayList<>();
        userList.clear();
        //TV=findViewById(R.id.search_by_text_view);
        SP=findViewById(R.id.search_by_spinner);
        SV=findViewById(R.id.search_view_id);
        TF1=Typeface.createFromAsset(getAssets(),"font/arbutus.ttf");
        //TV.setTypeface(TF1);
        RV=findViewById(R.id.recycler_id_search);
        RV.setHasFixedSize(true);
        //recycler view er element gula k horizontally dekhannr jnno,,,
        //LinearLayoutManager layoutManager=new LinearLayoutManager(LoadActivity.this,LinearLayoutManager.HORIZONTAL,false);
       // double d=getDp(getApplicationContext());
       // int n= (int) (d/106);
        Display display=getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics= new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density=getResources().getDisplayMetrics().density;
        float dpHeight=outMetrics.heightPixels/density;
        float dpWidth=outMetrics.widthPixels/density;
        int num=(int) dpWidth/106;
        RV.setLayoutManager(new GridLayoutManager(this,num));

        searchOption=getResources().getStringArray(R.array.search_option);
       // ArrayAdapter<String> adapter_home = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,searchOption);
        ArrayAdapter<String> adapter_home = new ArrayAdapter<String>(this,R.layout.spinner_text,searchOption);
        SP.setAdapter(adapter_home);
        SP.setOnItemSelectedListener(this);
        //showing all data
        showAllForAllBatch();

    }

    private void searchCall() {
        String selected=searchOption[SP.getSelectedItemPosition()];
        //Toast.makeText(getApplicationContext(),selected,Toast.LENGTH_SHORT).show();
        if (selected.equals("Select Searching Key")||selected.equals("Search By ID"))
        {
            //TV.setText("Search By ID");
            chosen=1;
            searchMethod(chosen);
        }
        else if (selected.equals("Search By Blood Group"))
        {
           // TV.setText("Search By Blood");
            chosen=2;
            searchMethod(chosen);
        }
        else if (selected.equals("Search By Name"))
        {
            //TV.setText("Search By Name");
            chosen=3;
            searchMethod(chosen);
        }
        else if (selected.equals("Search By Mobile No."))
        {
            //TV.setText("Search By mobile No.");
            chosen=4;
            searchMethod(chosen);
        }
        else if (selected.equals("Search By Hometown"))
        {
            //TV.setText("Search By Hometown");
            chosen=5;
            searchMethod(chosen);
        }
    }

    private void showAllForAllBatch() {
        Cursor c = myDatabaseHelp13.showAllData();
        while (c.moveToNext()) {
            User user = new User(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4),
                    c.getString(5), c.getString(6),c.getString(7),c.getString(8));
            userList.add(user);
        }
        Cursor d = myDatabaseHelp14.showAllData();
        while (d.moveToNext()) {
            User user = new User(d.getString(0), d.getString(1), d.getString(2), d.getString(3), d.getString(4),
                    d.getString(5), d.getString(6),d.getString(7),d.getString(8));
            userList.add(user);
        }

        Cursor e = myDatabaseHelp15.showAllData();
        while (e.moveToNext()) {
            User user = new User(e.getString(0), e.getString(1), e.getString(2), e.getString(3), e.getString(4),
                    e.getString(5), e.getString(6),e.getString(7),e.getString(8));
            userList.add(user);
        }

        Cursor f = myDatabaseHelp16.showAllData();
        while (f.moveToNext()) {
            User user= new User(f.getString(0), f.getString(1), f.getString(2), f.getString(3), f.getString(4),
                    f.getString(5), f.getString(6),f.getString(7),f.getString(8));
            userList.add(user);
        }

        Cursor g = myDatabaseHelp17.showAllData();
        while (g.moveToNext()) {
            User user= new User(g.getString(0), g.getString(1), g.getString(2), g.getString(3), g.getString(4),
                    g.getString(5), g.getString(6),g.getString(7),g.getString(8));
            userList.add(user);
        }
        Cursor h = myDatabaseHelp18.showAllData();
        while (h.moveToNext()) {
            User user= new User(h.getString(0), h.getString(1), h.getString(2), h.getString(3), h.getString(4),
                    h.getString(5), h.getString(6),h.getString(7),h.getString(8));
            userList.add(user);
        }
        Cursor i = myDatabaseHelp19.showAllData();
        while (i.moveToNext()) {
            User user= new User(i.getString(0), i.getString(1), i.getString(2), i.getString(3), i.getString(4),
                    i.getString(5), i.getString(6),i.getString(7),i.getString(8));
            userList.add(user);
        }

        myAdapterWithSearch = new MyAdapterWithSearch(SearchActivity.this, userList);
        //myAdapterWithSearch = new MyAdapterWithSearch( studentListSQ);
        RV.setAdapter(myAdapterWithSearch);
        myAdapterWithSearch.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id = userList.get(position).getId();
                String name = userList.get(position).getName();
                String mobile = userList.get(position).getMobile();
                String home = userList.get(position).getHome();
                String batch = userList.get(position).getBatch();
                String blood = userList.get(position).getBlood();
                String url = userList.get(position).getImageUrl();
                String email = userList.get(position).getEmail();
                String facebook = userList.get(position).getFacebook();
                Intent intent = new Intent(SearchActivity.this, ShowInfoActivity.class);
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
    private double getDp(Context context) {
        float density=context.getResources().getDisplayMetrics().density;
        //Toast.makeText(getApplicationContext()," "+density,Toast.LENGTH_SHORT).show();
        //evaluating pixel
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height=displayMetrics.heightPixels;
        int width=displayMetrics.widthPixels;
        // Toast.makeText(getApplicationContext()," "+density+" "+width,Toast.LENGTH_SHORT).show();
        double Dp;
        if(density>=4.0)
        {
            Dp=width/4.0;
        }
        else if (density>=3.0)
        {
            Dp=width/3.0;
        }
        else if (density>=2.0)
        {
            Dp=width/2.0;
        }
        else if (density>=1.5)
        {
            Dp=width/1.5;
        }
        else if (density>=1.0)
        {
            Dp=width/1.0;
        }
        else
        {
            Dp=width/0.75;
        }
        Dp=Math.floor(Dp);
        return Dp;
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_search,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.search_by_id)
        {
            selected=1;
            searchMethod(selected);
        }
        if (item.getItemId()==R.id.search_by_blood)
        {
            selected=2;
            searchMethod(selected);
        }
        if (item.getItemId()==R.id.search_by_name)
        {
            selected=3;
            searchMethod(selected);
        }
        if (item.getItemId()==R.id.search_by_mobile)
        {
            selected=4;
            searchMethod(selected);
        }
        if (item.getItemId()==R.id.search_by_home)
        {
            selected=5;
            searchMethod(selected);
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void searchMethod(int selected) {
        //Toast.makeText(getApplicationContext(),"search1",Toast.LENGTH_SHORT).show();
        if (selected==1)
        {
            //for id searching
            myAdapterWithSearch.setSelect(1);
            //TV.setText("Search By Id");
            SV.setQueryHint("Ex: CE17001");
            SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    myAdapterWithSearch.getFilter().filter(newText);
                    return false;
                }
            });
        }
        if (selected==2)
        {
            //for blood group searching
            myAdapterWithSearch.setSelect(2);
            //TV.setText("Search By Blood Group");
            SV.setQueryHint("Enter Blood Group");
            SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    myAdapterWithSearch.getFilter().filter(newText);
                    return false;
                }
            });
        }
        if (selected==3)
        {
            //for blood group searching
            myAdapterWithSearch.setSelect(3);
            //TV.setText("Search By Name");
            SV.setQueryHint("Enter Name");
            SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    myAdapterWithSearch.getFilter().filter(newText);
                    return false;
                }
            });
        }
        if (selected==4)
        {
            //for blood group searching
            myAdapterWithSearch.setSelect(4);
            //TV.setText("Search By Mobile No.");
            SV.setQueryHint("Enter Mobile Number");
            SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    myAdapterWithSearch.getFilter().filter(newText);
                    return false;
                }
            });
        }
        if (selected==5)
        {
            //for blood group searching
            myAdapterWithSearch.setSelect(5);
           // TV.setText("Search By Hometown");
            SV.setQueryHint("Enter District Name");
            SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    myAdapterWithSearch.getFilter().filter(newText);
                    return false;
                }
            });
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        searchCall();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //from on create go to  searchCall() method to search by id
        searchCall();

    }
}
