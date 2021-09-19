package com.example.studentproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    TextView TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About");
        //TV=findViewById(R.id.about_text_view);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            TV.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }*/


        //we can create a web view and then add this with the linear layout.
        //but we can't able to do set the font-family.
       /* WebView view=new WebView(this);
        view.setVerticalScrollBarEnabled(false);
        ((LinearLayout)findViewById(R.id.linear_layout_for_web_view_id)).addView(view);
        Typeface  TF1=Typeface.createFromAsset(getAssets(),"font/delius.ttf");
        view.loadData(getString(R.string.about_text_html),"text/html; charset=utf-8","utf-8");*/
    }
}
