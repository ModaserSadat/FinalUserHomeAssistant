package com.example.homeassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null)
        {

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("About Us");}

       View aboutPage=new AboutPage(this)
               .isRTL(false)
               .setImage(R.drawable.home_about_logo)
               .setDescription("Home Assistant App\n...\nHome Assistant App Mission\nTo provide fast , free and reliable information to our users and connect local Service providers to Users.")
               .addItem(new Element().setTitle("Version 1.0"))

               .addGroup("Connect With Us")
               .addEmail("modasersadat2017@gmail.com")
               .addWebsite("https://developer.android.com/")
               .addFacebook("Modaser.Sadat2")
               .addTwitter("Modaser_Sadat1")
               .addYoutube("UCostmdCsWjzTm0_LY0DEzpg?view_as=subscriber")
               .addInstagram("modaser_sadat/")
               .addGitHub("ModaserSadat")
               .addItem(createCopyright())
               .create();
       setContentView(aboutPage);



    }
/*----------------------------------------------------------------------------------------------------------*/

    private Element createCopyright() {
        Element copyright=new Element();
        final String copyrightString=String.format("Copyright %d by MOMND", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        copyright.setIconDrawable(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AboutUsActivity.this, copyrightString, Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
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
