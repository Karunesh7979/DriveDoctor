package com.csis.drivedoctor.postAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csis.drivedoctor.Helpers.Loader;
import com.csis.drivedoctor.Helpers.UsersDBHelper;
import com.csis.drivedoctor.preauthDatabase.TaskCompletion;
import com.csis.drivedoctor.R;
import com.csis.drivedoctor.model.MyUserData;
import com.csis.drivedoctor.preauthDatabase.DatabaseHelper;
import com.csis.drivedoctor.Helpers.ModelHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {

    ImageView imageProfile;
    Uri imageURI;
    String imageSelected = "";
    MyUserData mydata;
    TextView firstNameTextView;
    TextView lastNameTextView;
    TextView phoneTextView;
    TextView addressTextView;
    TextView cityTextView;
    Button submitButton;
    UsersDBHelper editProfileHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        MyUserData clientdata = (MyUserData) (getIntent().getSerializableExtra("user"));
        if(clientdata == null) {
            mydata = ModelHelper.getInstance().getUser();
        } else {
            mydata = clientdata;
        }

        editProfileHelper = new UsersDBHelper(this);

        imageProfile = findViewById(R.id.id_EditProfile_ImageView);
        lastNameTextView = findViewById(R.id.idEdiTextLnameEditProfile);
        firstNameTextView = findViewById(R.id.idEdiTextFnameEditProfile);
        phoneTextView = findViewById(R.id.idEditprofilePhoneNumberEditText);
        addressTextView = findViewById(R.id.idEditProfileAddressEditText);
        cityTextView = findViewById(R.id.idEditProfileCityEditText);

        submitButton = findViewById(R.id.idSubmitButtonEditProfile);

        firstNameTextView.setText(mydata.getFirstName());
        lastNameTextView.setText(mydata.getLastName());
        phoneTextView.setText(mydata.getPhone());
        addressTextView.setText(mydata.getAddress());
        cityTextView.setText(mydata.getCity());


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mydata.setPhone(phoneTextView.getText().toString().trim());
                mydata.setLastName(lastNameTextView.getText().toString().trim());
                mydata.setFirstName(firstNameTextView.getText().toString().trim());
                mydata.setAddress(addressTextView.getText().toString().trim());
                mydata.setCity(cityTextView.getText().toString().trim());

                editProfileHelper.UpdateUser(mydata);
                uploadImage();
            }
        });

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        if (!(mydata.getProfileURL().equals(""))) {
            Glide.with(this).load(mydata.getProfileURL()).into(imageProfile);
        }
    }


    private void selectImage() {

        //Setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Set title
        builder.setTitle("Notice");

        //Set message
        builder.setMessage("Choose an action");

        //Add the buttons
        builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                openGallery();
            }
        });
        builder.setNegativeButton("Cancel", null);

        //Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void openGallery() {


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);

        } else {
            // Permission has already been granted

            //Create an Intent with action as ACTION_PICK
            Intent intent = new Intent(Intent.ACTION_PICK);
            // Sets the type as image/*. This ensures only components of type image are selected
            intent.setType("image/*");
            //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            // Launching the Intent
            startActivityForResult(intent, 1);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 1:
                    try {
                        //data.getData returns the content URI for the selected Image
                        imageURI = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageURI);
                        final Bitmap selectedImageInBM = BitmapFactory.decodeStream(imageStream);
                        //image_view.setImageBitmap(selectedImage);
                        imageProfile.setImageBitmap(selectedImageInBM);

                    } catch (FileNotFoundException e) {
                        Log.d("Photo exception-->>" ,e.toString());
                    }
                    break;
            }
    }


    private void uploadImage() {

        if(imageURI != null) {
//            InputStream  inputStream = getAssets()
            Loader.show(this);
            //Upload image to Storage and get the url
            final StorageReference ref = DatabaseHelper.getInstance().storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("DATABASE: ", "Uploaded image URL: "+ uri.toString());
                            imageSelected = uri.toString();
                            mydata.setProfileURL(imageSelected);
                            //Create new user and use image URL
                            updateUserProfile();
                        }
                    });
                }
            });
        } else {
            updateUserProfile();
        }
    }

    private void updateUserProfile() {

        String name = mydata.getFirstName() + " " +mydata.getFirstName();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(imageSelected))
                .build();

        DatabaseHelper.getInstance().user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Loader.dismiss();
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor = DatabaseHelper.getInstance().getPref().edit();
                            editor.putString("fullName", name); // Storing string
                            editor.putString("imageURL",imageSelected); // Storing string
                            editor.apply(); // commit changes

                            DatabaseHelper.getInstance().updateUserData(mydata, new TaskCompletion() {
                                        @Override
                                        public void taskCompletion(boolean isSuccess) {
                                            ModelHelper.getInstance().setUser(mydata);
                                            finish();
                                        }
                                    });
                        }
                    }
                });
    }

}