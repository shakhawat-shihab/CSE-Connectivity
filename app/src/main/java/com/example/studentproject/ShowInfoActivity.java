package com.example.studentproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.webkit.URLUtil;
import android.widget.Button;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static android.Manifest.*;

public class ShowInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView IV;
    private TextView Id, Name, Mobile, Home, Batch, Blood, EMAIL, FACEBOOK;
    private TextView IdT, NameT, MobileT, HomeT, BatchT, BloodT, EMAILT, FACEBOOKT;
    private Button Call,facebookButton,gmailButton;
    private String s;
    private Typeface TF1,TF2;
    private String facebook,email;
    static final int READ_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        setTitle("Details Information");

        //can't able to implement back on action bar..(as the previous activity of ShowInfoActivity can be two )
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        IV = findViewById(R.id.image_show_info);
        Id = findViewById(R.id.id_show_info);
        Name = findViewById(R.id.name_show_info);
        Mobile = findViewById(R.id.mobile_show_info);
        Home = findViewById(R.id.home_show_info);
        Batch = findViewById(R.id.batch_show_info);
        Blood = findViewById(R.id.blood_show_info);
        EMAIL = findViewById(R.id.email_show_info);
        FACEBOOK = findViewById(R.id.facebook_show_info);
        FACEBOOK.setOnClickListener(this);




        IdT = findViewById(R.id.id_show_info_title);
        NameT = findViewById(R.id.name_show_info_title);
        MobileT = findViewById(R.id.mobile_show_info_title);
        HomeT = findViewById(R.id.home_show_info_title);
        BatchT = findViewById(R.id.batch_show_info_title);
        BloodT = findViewById(R.id.blood_show_info_title);
        EMAILT = findViewById(R.id.email_show_info_title);
        FACEBOOKT = findViewById(R.id.facebook_show_info_title);

        TF1=Typeface.createFromAsset(getAssets(),"font/delius.ttf");
        TF2=Typeface.createFromAsset(getAssets(),"font/acme.ttf");

        Id.setTypeface(TF1);
        Name.setTypeface(TF1);
        Mobile.setTypeface(TF1);
        Home.setTypeface(TF1);
        Batch.setTypeface(TF1);
        Blood.setTypeface(TF1);
        EMAIL.setTypeface(TF1);
        FACEBOOK.setTypeface(TF1);

        IdT.setTypeface(TF2);
        NameT.setTypeface(TF2);
        MobileT.setTypeface(TF2);
        HomeT.setTypeface(TF2);
        BatchT.setTypeface(TF2);
        BloodT.setTypeface(TF2);
        EMAILT.setTypeface(TF2);
        FACEBOOKT.setTypeface(TF2);

        Call = findViewById(R.id.call_button_show_info);
        Call.setOnClickListener(this);
        facebookButton=findViewById(R.id.facebook_button_show_info);
        facebookButton.setOnClickListener(this);
        gmailButton=findViewById(R.id.gmail_button_show_info);
        gmailButton.setOnClickListener(this);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String mobile = intent.getStringExtra("mobile");
        String home = intent.getStringExtra("home");
        String batch = intent.getStringExtra("batch");
        String blood = intent.getStringExtra("blood");
        String url = intent.getStringExtra("url");
        email = intent.getStringExtra("email");
        facebook = intent.getStringExtra("facebook");

        //adding http:// before facebook,,
        if(facebook.length()>5)
        {
            if (facebook.charAt(0)!='h'&&facebook.charAt(1)!='t'&&facebook.charAt(2)!='t'&&facebook.charAt(3)!='p')
            {
                String S;
                S=facebook;
                facebook="";
                facebook="http://"+S;
            }
        }


        //Toast.makeText(getApplicationContext(),facebook,Toast.LENGTH_SHORT).show();
        //facebook link er niche dag howar jnno,,
        SpannableString content=new SpannableString(facebook);
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        if (!facebook.equals("empty")){
            //jkn sign up kortase tkn "empty" hisabe save hsse
            FACEBOOK.setText(content);
        }



        s = "tel:" + mobile;
        Id.setText("" + id);
        Name.setText("" + name);
        Mobile.setText("" + mobile);
        Home.setText("" + home);
        Batch.setText("" + batch);
        Blood.setText("" + blood);
        EMAIL.setText("" + email);
        //FACEBOOK.setText("" + facebook);
        //setting Image
        if (url.equals("0"))
        {
            IV.setImageResource(R.drawable.guest_red);
        }
        else if (url.equals("1"))
        {
            IV.setImageResource(R.drawable.guest_blue);
        }
        else if (url.equals("2"))
        {
            IV.setImageResource(R.drawable.guest_yellow);
        }
        else if (url.equals("3"))
        {
            IV.setImageResource(R.drawable.guest_green);
        }
        else
        {
            Picasso.get().load(url).placeholder(R.drawable.default_pic).into(IV);

        }

    }
    public void rPermission() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) this, new String[]{permission.CALL_PHONE},
                    READ_CALL);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.call_button_show_info) {
            if (checkSelfPermission(permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                try {
                    rPermission();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                // callIntent.setData(Uri.parse("tel:123456789"));
                callIntent.setData(Uri.parse(s));
                startActivity(callIntent);
            }

        }
        if (v.getId()==R.id.facebook_show_info)
        {
            if (URLUtil.isValidUrl(facebook))
            {
                Intent browser=new Intent(Intent.ACTION_VIEW,Uri.parse(facebook));
                startActivity(browser);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Invalid address!!", Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId()==R.id.facebook_button_show_info)
        {
            if (URLUtil.isValidUrl(facebook)) {
                Intent facebookIntent = openFacebook(ShowInfoActivity.this);
                startActivity(facebookIntent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Invalid address!!", Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId()==R.id.gmail_button_show_info)
        {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                String[] toEmail={email};
                Intent gmailIntent=new Intent(Intent.ACTION_SENDTO);
                gmailIntent.setData(Uri.parse("mailto:"));
                gmailIntent.putExtra(Intent.EXTRA_EMAIL,toEmail);
                //gmailIntent.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
                startActivity(Intent.createChooser(gmailIntent,"choose your email application"));
            }
            else
            {
                Toast.makeText(getBaseContext(),"No email address is Specified",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Intent openFacebook(Context context) {
        try {
            //PackageManager packageManager = context.getPackageManager();
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            String link="fb://facewebmodal/f?href="+ facebook;
            return new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            /* else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }*/
        } catch (Exception e){

            return new Intent(Intent.ACTION_VIEW, Uri.parse(facebook));
        }
    }


}
