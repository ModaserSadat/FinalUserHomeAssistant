package com.example.homeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

//import info.najeeb.firebaserecyclerimagesuploaddownload.R;

public class DetailsActivity extends AppCompatActivity {

    TextView nameDetailTextView,descriptionDetailTextView,dateDetailTextView,categoryDetailTextView;
    ImageView teacherDetailImageView;
    public TextView phoneDetailTextView;
    public ImageButton imbtn;






    private void initializeWidgets(){
        nameDetailTextView= findViewById(R.id.nameDetailTextView);
        descriptionDetailTextView= findViewById(R.id.descriptionDetailTextView);
        phoneDetailTextView=findViewById(R.id.phonedetailTextView);
        dateDetailTextView= findViewById(R.id.dateDetailTextView);
        categoryDetailTextView= findViewById(R.id.categoryDetailTextView);
        teacherDetailImageView=findViewById(R.id.teacherDetailImageView);
        imbtn=findViewById(R.id.phonecall);
    }
    private String getDateToday(){
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String today= dateFormat.format(date);
        return today;
    }
    private String getRandomCategory(){
        String[] categories={"teacher","teacher","teacher"};
        Random random=new Random();
        int index=random.nextInt(categories.length-1);
        return categories[index];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initializeWidgets();

        //RECEIVE DATA FROM ITEMSACTIVITY VIA INTENT
        Intent i=this.getIntent();
        String name=i.getExtras().getString("NAME_KEY");
        String description=i.getExtras().getString("DESCRIPTION_KEY");
        final String phone_key=i.getExtras().getString("PHONE_KEY");
        final String imageURL=i.getExtras().getString("IMAGE_KEY");



        //SET RECEIVED DATA TO TEXTVIEWS AND IMAGEVIEWS
        nameDetailTextView.setText(name);
        descriptionDetailTextView.setText(description);
        phoneDetailTextView.setText(phone_key);
        dateDetailTextView.setText("DATE: "+getDateToday());
        categoryDetailTextView.setText("CATEGORY: "+getRandomCategory());
        Picasso.get()
                .load(imageURL)
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(teacherDetailImageView);







        imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = "tel:" + phone_key;
                i.setData(Uri.parse(p));
                startActivity(i);

            }
        });

    }

}
