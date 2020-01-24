package com.example.homeassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactUsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView contactEmail,contactPhone;
    private ImageView callBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        mToolbar=findViewById(R.id.contact_us_appbar_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Contact Us");

        contactEmail=findViewById(R.id.contact_email);
        contactPhone=findViewById(R.id.contact_phone);
        callBtn=findViewById(R.id.call_image_btn);

        contactEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                String aEmailList[] = { "modasersadat2017@gmail.com"};
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
                emailIntent.setType("plain/text");
                startActivity(emailIntent);

            }
        });

        contactPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "+917499782164";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);



            }
        });
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "+917499782164";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);


            }
        });

    }


    //to work the back button we should add onOptionsItemSelected() method
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        //if the user clicked on that back button, then we are going to send the user to the MainActivity
        if(id==android.R.id.home)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
