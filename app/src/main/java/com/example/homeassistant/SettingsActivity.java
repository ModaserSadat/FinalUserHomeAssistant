package com.example.homeassistant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private CircleImageView profileImageView;
    private EditText fullNameEditText, userPhoneEditText,addressEditText;
    private TextView profileChangeTextView;
    private Button saveTextButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    FirebaseUser user;
    private StorageTask uploadTask;
    private String currentUserId;
    private ProgressDialog progressDialog;
    private Toolbar mToolbar;

    private Uri imageUri;
    private String myUrl="";
    private StorageReference storageProfilePictureRef;
    private String checker="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mToolbar=findViewById(R.id.app_bar_settings);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Settings");

        profileImageView=findViewById(R.id.settings_profile_image);
        fullNameEditText=findViewById(R.id.settings_fullname);
        userPhoneEditText =findViewById(R.id.settings_phone);
        addressEditText=findViewById(R.id.settings_Address);
        profileChangeTextView=findViewById(R.id.profile_image_change_btn);

        saveTextButton=findViewById(R.id.update_account_settings);
        storageProfilePictureRef= FirebaseStorage.getInstance().getReference().child("Users");

        progressDialog=new ProgressDialog(SettingsActivity.this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);


        mAuth= FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        currentUserId=user.getUid();
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        mDatabaseReference=mFirebaseDatabase.getReference().child("Users");

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });
        profileChangeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker="clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)

                        .start(SettingsActivity.this);
            }
        });



        userInfoDisplay(profileImageView,fullNameEditText, userPhoneEditText,addressEditText);


    }

    //-------------------------------------------------------------------------------------------------------------------------------------


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            imageUri=result.getUri();
            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error, Try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
            finish();
        }

    }

    private void updateOnlyUserInfo()
    {

        String name=fullNameEditText.getText().toString().trim();
        String phone= userPhoneEditText.getText().toString().trim();
        String address=addressEditText.getText().toString().trim();

        if(name.isEmpty())
        {
            Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(phone.isEmpty())
        {
            Toast.makeText(this, "Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if(address.isEmpty())
        {
            Toast.makeText(this, "Address is mandatory", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.show();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("Name", name);
            userMap.put("Address", address);
            userMap.put("Phone", phone);
            ref.updateChildren(userMap).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                Toast.makeText(SettingsActivity.this, "Profile Info Updated Successfuly", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(SettingsActivity.this, "Error! Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



        }


    }

    private void userInfoSaved() {
        String name=fullNameEditText.getText().toString().trim();
        String phone= userPhoneEditText.getText().toString().trim();
        String address=addressEditText.getText().toString().trim();

        if(name.isEmpty())
        {
            Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(phone.isEmpty())
        {
            Toast.makeText(this, "Enter a Valid Phone number", Toast.LENGTH_SHORT).show();
        }
        else if(address.isEmpty())
        {
            Toast.makeText(this, "Address is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }



    }

    private void uploadImage() {


        progressDialog.show();
        if(imageUri!=null)
        {
            final StorageReference fileRef=storageProfilePictureRef.child(currentUserId+".jpg");
            uploadTask=fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();

                    }


                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful())
                            {
                                Uri downloadUrl=task.getResult();
                                myUrl=downloadUrl.toString();
                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
                                HashMap<String,Object> userMap=new HashMap<>();
                                userMap.put("Name",fullNameEditText.getText().toString());
                                userMap.put("Address",addressEditText.getText().toString());
                                userMap.put("Phone", userPhoneEditText.getText().toString());
                                userMap.put("image",myUrl);
                                ref.child(currentUserId).updateChildren(userMap);
                                progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                Toast.makeText(SettingsActivity.this, "Profile Info Updated Successfuly", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(SettingsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
        else
        {
            progressDialog.dismiss();
            Toast.makeText(this, "Image is not selected", Toast.LENGTH_SHORT).show();
        }


    }


    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText)
    {
        if(user!=null)
        {
            DatabaseReference UserRef=mDatabaseReference.child(currentUserId);
            UserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        if(dataSnapshot.child("image").exists())
                        {
                            String image=dataSnapshot.child("image").getValue().toString();
                            String name=dataSnapshot.child("Name").getValue().toString();
                            String address=dataSnapshot.child("Address").getValue().toString();
                            String phone=dataSnapshot.child("Phone").getValue().toString();
                            Picasso.get().load(image).into(profileImageView);
                            fullNameEditText.setText(name);
                            userPhoneEditText.setText(phone);
                            addressEditText.setText(address);
                        }
                        else if(dataSnapshot.child("Phone").exists()) {
                            String name=dataSnapshot.child("Name").getValue().toString();
                            String address=dataSnapshot.child("Address").getValue().toString();
                            String phone=dataSnapshot.child("Phone").getValue().toString();

                            fullNameEditText.setText(name);
                            userPhoneEditText.setText(phone);
                            addressEditText.setText(address);


                        }
                        else
                        {
                            String name=dataSnapshot.child("Name").getValue().toString();
                            fullNameEditText.setText(name);


                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });





        }


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
