package com.example.studentproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class MyProfileEditActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView IV;
    private EditText NAME, MOBILE,  FACEBOOK;
    private AutoCompleteTextView HOME, BLOOD;
    private TextView ID, BATCH , EMAIL;
    private Button IMAGE,SAVE;
    SharedPreferences SP;
    private SharedPreferences.Editor editor;
    String Id,Name,Mobile,Batch,Home,Blood,Email,Facebook,Image;
    private String[] blood_ar,district_ar;
    String name,mobile,home,blood,facebook;
    private final int PICK_IMAGE_GALLERY_REQUEST_CODE=1;
    private  Uri uploadUri=null;
    static final int READ_STORAGE_PERMISSION_REQUEST_CODE=1;
    private StorageReference storageReference;
    private  DatabaseReference databaseReference;
    private ProgressDialog PD;
    String imageLink_previous;
    int cnt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Profile");
        PD = new ProgressDialog(MyProfileEditActivity.this);
        PD.setTitle("Saving");
        PD.setMessage("Please wait...");
        PD.setCancelable(true);
        SP=getSharedPreferences("User", Context.MODE_PRIVATE);
        editor=SP.edit();
        Id= SP.getString("user_id","not found!");
        Name= SP.getString("user_name","not found!");
        Mobile= SP.getString("user_mobile","not found!");
        Batch=SP.getString("user_batch","not found!");
        Home=SP.getString("user_home","not found!");
        Blood=SP.getString("user_blood","not found!");
        Email=SP.getString("user_email","not found!");
        Facebook=SP.getString("user_facebook","not found!");
        Image=SP.getString("user_image","not found!");

        IV = findViewById(R.id.image_show_info_profile_edit);
        ID = findViewById(R.id.id_show_info_profile_edit);
        NAME = findViewById(R.id.name_show_info_profile_edit);
        MOBILE = findViewById(R.id.mobile_show_info_profile_edit);
        HOME = findViewById(R.id.home_show_info_profile_edit);
        BATCH = findViewById(R.id.batch_show_info_profile_edit);
        BLOOD = findViewById(R.id.blood_show_info_profile_edit);
        EMAIL = findViewById(R.id.email_show_info_profile_edit);
        FACEBOOK = findViewById(R.id.facebook_show_info_profile_edit);
        IMAGE=findViewById(R.id.my_profile_chose_image_id);
        SAVE=findViewById(R.id.my_profile_save_id);
        IMAGE.setOnClickListener(this);
        SAVE.setOnClickListener(this);

        ID.setText("" + Id);
        NAME.setText("" + Name,TextView.BufferType.EDITABLE);
        MOBILE.setText(""+Mobile);
        HOME.setText("" + Home);
        BATCH.setText("" + Batch);
        BLOOD.setText("" + Blood);
        EMAIL.setText("" + Email);


        storageReference= FirebaseStorage.getInstance().getReference("Images");
        databaseReference=FirebaseDatabase.getInstance().getReference().child("users").child(Batch).child(makeKeyFromEmail(Email));

        if (!Facebook.equals("empty"))
        {
            FACEBOOK.setText(Facebook);
        }
        if (Image.equals("0"))
        {
            IV.setImageResource(R.drawable.guest_red);
        }
        else if (Image.equals("1"))
        {
            IV.setImageResource(R.drawable.guest_blue);
        }
        else if (Image.equals("2"))
        {
            IV.setImageResource(R.drawable.guest_yellow);
        }
        else if (Image.equals("3"))
        {
            IV.setImageResource(R.drawable.guest_green);
        }
        else
        {
            Picasso.get().load(Image).placeholder(R.drawable.default_pic).into(IV);

        }

        //getting suggestion in writing text in auto complete text view
        district_ar=getResources().getStringArray(R.array.district_name);
        ArrayAdapter<String> adapter_home = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,district_ar);
        HOME.setAdapter(adapter_home);

        blood_ar=getResources().getStringArray(R.array.blood_group);
        ArrayAdapter<String> adapter_blood = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,blood_ar);
        BLOOD.setAdapter(adapter_blood);



    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.my_profile_save_id) {
            name = NAME.getText().toString().trim();
            mobile = MOBILE.getText().toString().trim();
            home = HOME.getText().toString().trim();
            blood = BLOOD.getText().toString().trim();
            facebook = FACEBOOK.getText().toString().trim();

            if (name.length() < 8) {
                NAME.setError("Enter Full Name");
                NAME.requestFocus();
                return;
            }
            if (mobile.length() !=11) {
                MOBILE.setError("Mobile Number must be 11 digits");
                MOBILE.requestFocus();
                return;
            }
            if (home.equals("")) {
                HOME.setError("Hometown can't be empty");
                HOME.requestFocus();
                return;
            }
            if (blood.equals("")) {
                BLOOD.setError("Blood Group can't be empty");
                BLOOD.requestFocus();
                return;
            }
            if (facebook.equals(""))
            {
                facebook+="empty";
            }
            PD.show();
            //User user=new User(Id,name,mobile,home,Batch,blood,Image,Email,Facebook);
            //if we are doing update data by User class, then remaining child (ex: isModerator,isAdmin) become deleted
            databaseReference.child("name").setValue(name).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    editor.putString("user_name",name);
                    editor.commit();
                    callTaskComplete();
                    //Toast.makeText(getApplicationContext(),"name Updated",Toast.LENGTH_SHORT).show();
                }
            });
            databaseReference.child("mobile").setValue(mobile).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    editor.putString("user_mobile",mobile);
                    editor.commit();
                    callTaskComplete();
                   // Toast.makeText(getApplicationContext(),"mobile Updated",Toast.LENGTH_SHORT).show();
                }
            });
            databaseReference.child("home").setValue(home).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    editor.putString("user_home",home);
                    editor.commit();
                    callTaskComplete();
                    //Toast.makeText(getApplicationContext(),"home Updated",Toast.LENGTH_SHORT).show();
                }
            });
            databaseReference.child("blood").setValue(blood).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    editor.putString("user_blood",blood);
                    editor.commit();
                    callTaskComplete();
                    //Toast.makeText(getApplicationContext(),"blood Updated",Toast.LENGTH_SHORT).show();
                }
            });
            final String finalFacebook = facebook;
            databaseReference.child("facebook").setValue(facebook).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    editor.putString("user_facebook", facebook);
                    editor.commit();
                    callTaskComplete();
                    //Toast.makeText(getApplicationContext(),"facebook Updated",Toast.LENGTH_SHORT).show();
                }
            });

            //now upload the new image
            if (uploadUri!=null)
            {
                //getting the link of previous image
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        imageLink_previous=dataSnapshot.child("imageUrl").getValue(String.class);
                       // Toast.makeText(getApplicationContext(), "got", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                StorageReference ref=storageReference.child(Id+"_"+System.currentTimeMillis());
                ref.putFile(uploadUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //successfully uploaded the image
                        Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            final String imageLink = downloadUrl.toString();
                            Toast.makeText(getApplicationContext(), "image Uploaded", Toast.LENGTH_SHORT).show();
                            //now set the link og new image to database
                            databaseReference.child("imageUrl").setValue(imageLink).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //successfully added the new image's link to database
                                    editor.putString("user_image", imageLink);
                                    editor.commit();
                                    //Toast.makeText(getApplicationContext(), "image link added with data base", Toast.LENGTH_SHORT).show();
                                    //if there is no previous image (that means we have 0/1/2/3 this values in the RTD)
                                    //so at that time the length is small and there could not be any delete operation
                                    //but if we have a real imageUrl in the RTD then we have to do delete operation
                                    if (imageLink_previous.length() > 2) {
                                        StorageReference SF = FirebaseStorage.getInstance().getReferenceFromUrl(imageLink_previous);
                                        SF.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Toast.makeText(getApplicationContext(), "previous image deleted", Toast.LENGTH_SHORT).show();
                                                //uploadUri = null;
                                                callTaskComplete();
                                            }
                                        });
                                    }
                                    //no real imageUrl in the RTD (0/1/2/3 are in the RTD)
                                    else {
                                        //Toast.makeText(getApplicationContext(), "no previous image", Toast.LENGTH_SHORT).show();
                                        //uploadUri = null;
                                        callTaskComplete();
                                    }
                                }
                            });
                    }
                });
            }
            //No Image is selected, by clicking "Chose Image" button
            else
            {
                callTaskComplete();
            }

        }
        if(v.getId()==R.id.my_profile_chose_image_id)
        {
            //at first check storage permission
            boolean b=checkPermissionForReadExternalStorage();
            if (b==false)
            {
                try {
                    requestPermissionForReadExternalStorage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                startActivityForResult(new Intent()
                        .setAction(Intent.ACTION_GET_CONTENT)
                        .setType("image/*"),PICK_IMAGE_GALLERY_REQUEST_CODE);
            }
        }

    }

    private void callTaskComplete() {
        cnt++;
        if(cnt==6)
        {
            PD.dismiss();
            Intent intent =new Intent(MyProfileEditActivity.this,MyProfileActivity.class);
            finish();
            startActivity(intent);
            cnt=0;
        }
    }

    private boolean checkPermissionForReadExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
    public void requestPermissionForReadExternalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getApplicationContext(),requestCode+requestCode,Toast.LENGTH_SHORT).show();
        if (requestCode==PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode==RESULT_OK)
        {
            Uri imageUri=data.getData();
            if (imageUri!=null)
            {
                startCrop(imageUri);
            }

        }
        else if(requestCode== UCrop.REQUEST_CROP && resultCode==RESULT_OK )
        {
            Uri imageUriResultCrop= UCrop.getOutput(data);
            uploadUri=imageUriResultCrop;
            //Toast.makeText(getApplicationContext(),imageUriResultCrop.toString(),Toast.LENGTH_SHORT).show();
            if (imageUriResultCrop!=null)
            {
                IV.setImageURI(imageUriResultCrop);
            }

        }
    }
    public void startCrop(@NonNull Uri uri)
    {
        long time=System.currentTimeMillis();
        String destinationFileName=Id+"_"+String.valueOf(time);
        destinationFileName+=".jpg";
        UCrop uCrop=UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationFileName)));
        //uCrop.withAspectRatio(1,1);
        uCrop.withAspectRatio(7,8);
        //uCrop.useSourceImageAspectRatio();
        //uCrop.withAspectRatio(2,3);
        //uCrop.withAspectRatio(16,9);
        uCrop.withMaxResultSize(450,450);
        uCrop.withOptions(getCropOptions());
        // uCrop.start(MainActivity.this);
        uCrop.start(MyProfileEditActivity.this);
        //the above line calls  onActivityResult() method
    }

    private UCrop.Options getCropOptions()
    {
        UCrop.Options options=new UCrop.Options();
        options.setCompressionQuality(70);
        //compress Type
        // options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        // options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        //Ui
        options.setHideBottomControls(false);
        //options.setFreeStyleCropEnabled(true);
        //colors
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarTitle("Edit Image");
        return  options;
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
    private String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        //Toast.makeText(getApplicationContext(),"File Ex",Toast.LENGTH_SHORT).show();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

}
