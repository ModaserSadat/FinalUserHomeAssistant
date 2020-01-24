package com.example.homeassistant;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
   private FirebaseAuth mAuth;
   private FirebaseDatabase mFirebaseDatabase;
   private DatabaseReference mDatabaseReference;
   FirebaseUser user;
   CircleImageView mCircleImageView;
   private NavigationView mNavigationView;
   private DrawerLayout mDrawerLayout;
   private ActionBarDrawerToggle mActionBarDrawerToggle;
   private Toolbar mToolbar;
   private TextView NavProfileName;
   ImageView teacher;
   ImageView carpenter;
   ImageView mechanic;
   ImageView plumber;
   ImageView electrician;
   ImageView engineer;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar= findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        //init
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mFirebaseDatabase.getReference("Users");

        teacher=findViewById(R.id.teacher);
        carpenter = findViewById(R.id.carpenter);
        mechanic = findViewById(R.id.mechanic);
        plumber = findViewById(R.id.plumber);
        electrician = findViewById(R.id.electrician);
        engineer = findViewById(R.id.engineer);


        mDrawerLayout=findViewById(R.id.drawable_layout);
        //to show hamburger
        mActionBarDrawerToggle=new ActionBarDrawerToggle(MainActivity.this,mDrawerLayout,R.string.drawer_open,R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavigationView=findViewById(R.id.navigation_view);
        View navView=mNavigationView.inflateHeaderView(R.layout.navigation_header);
        NavProfileName=navView.findViewById(R.id.nav_user_fullname);
        mCircleImageView=navView.findViewById(R.id.nav_profile_image);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //to include Navigation header in navagation drawer
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                UserMenuSelector(menuItem);
                //when menu item is clicked we want to close the navigation drawer
                DrawerLayout drawer =  findViewById(R.id.drawable_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


teacher.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i=new Intent(MainActivity.this,ItemsActivity.class);
        startActivity(i);
    }
});
carpenter.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i = new Intent(MainActivity.this,CarpenterViewItemActivity.class);
        startActivity(i);
    }
});
        mechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,MechanicViewItemActivity.class);
                startActivity(i);
            }
        });
        plumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,PlumberViewItemActivity.class);
                startActivity(i);
            }
        });
        electrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,ElectricianViewItemActivity.class);
                startActivity(i);
            }
        });
        engineer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,EngineerViewItemActivity.class);
                startActivity(i);
            }
        });



    }

    private void setProfileDetails() {
        if(user!=null) {

            Query query = mDatabaseReference.orderByChild("Email").equalTo(user.getEmail());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //check until required data get
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //get Data
                        String name = "" + ds.child("Name").getValue();
                        //String email = "" + ds.child("Email").getValue();
                        String image=""+ds.child("image").getValue();

                        
                        //set Data
                        NavProfileName.setText(name);
                        if (image!=null) {
                            Picasso.get().load(image).placeholder(R.drawable.profile).into(mCircleImageView);
                        }




                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            //user not signed in, go to Login Activity
            goToLoginActivity();
        }
    }

    private void UserMenuSelector(MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
            case R.id.nav_about_us:
                startActivity(new Intent(getApplicationContext(),AboutUsActivity.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
            case R.id.nav_rate:
                rateApp();
                break;
            case R.id.nav_contact:
                startActivity(new Intent(getApplicationContext(),ContactUsActivity.class));
                break;
            case R.id.nav_share:
                shareApp();
                break;
            case R.id.nav_logout:
               mAuth.signOut();
               startActivity(new Intent(getApplicationContext(),LoginActivity.class));
               finish();
                break;

        }

    }

    private void shareApp() {
        Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
        String app_url = " https://play.google.com/store/apps/details?id=com.example.homeassistant";
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,app_url);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    //This meyhod will launch the Play Store with your App page already opened. The user can rate it there.
    private void rateApp() {

        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
    }


    @Override
    protected void onStart() {
        //check on start of app
        setProfileDetails();
        super.onStart();
    }

    /*inflate Options menu*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating menu
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    /*handle menu item clicks*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get item id
        int id=item.getItemId();
        if(id==R.id.action_logout){
            mAuth.signOut();
            goToLoginActivity();
        }
        if(id==R.id.changePassword)
        {
            showRecoverPasswordDialog();
        }
        //to make hamburger worked
        if(mActionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showRecoverPasswordDialog() {
        //AlertDialog
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("         Change Password");
        //set layout linear layout
        LinearLayout linearLayout=new LinearLayout(MainActivity.this);
        final EditText eTextEmail=new EditText(MainActivity.this);
        eTextEmail.setHint("Email");
        eTextEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        /*sets the min width of a EditView to fit a text of n 'M' letters regardless of the actual text extension and text size*/
        eTextEmail.setMinEms(16);
        linearLayout.addView(eTextEmail);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        //button Recover
        builder.setPositiveButton("Send me Password Reset Link", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String RecoverEmail=eTextEmail.getText().toString().trim();
                beginRecovery(RecoverEmail);




            }
        });
        //button Cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();


            }
        });
        //show dialog
        builder.create().show();

    }

    private void beginRecovery(String recoverEmail) {
        final ProgressDialog progressdialog;
        //show ProgressDialog
        progressdialog=new ProgressDialog(this);
        progressdialog.setTitle("");
        progressdialog.setMessage("Sending Email...");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.show();
        mAuth.sendPasswordResetEmail(recoverEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            progressdialog.dismiss();
                            Toast.makeText(MainActivity.this, "Password reset link has been sent to your e-mail address!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressdialog.dismiss();
                            Toast.makeText(MainActivity.this, "Failed...", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressdialog.dismiss();
                        //get and Show proper error message
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void goToLoginActivity() {
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }

}
