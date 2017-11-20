package com.mad.chatmessenger.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mad.chatmessenger.R;
import com.mad.chatmessenger.firebase.FirebaseService;
import com.mad.chatmessenger.model.User;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.UUID;

public class ProfileUpdateActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private EditText firstNameEditText, lastNameEditText;
    private TextView fullNameTextView;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private User user=null;
    private String userId=FirebaseService.GetCurrentUser().getUid();
    private String firstName, lastName, gender;
    private ProgressDialog progressDialog;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    private Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        user = new User();
        firstNameEditText = (EditText) findViewById(R.id.editTextFirstName);
        lastNameEditText = (EditText) findViewById(R.id.editTextLastName);
        genderRadioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        maleRadioButton= (RadioButton) findViewById(R.id.radioButtonMale);
        femaleRadioButton = (RadioButton) findViewById(R.id.radioButtonFemale);
        profileImageView = (ImageView) findViewById(R.id.imageViewProfile);
        fullNameTextView=(TextView) findViewById(R.id.textViewFullName);
        progressDialog = new ProgressDialog(this);

        FirebaseService.getRootRef().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if( user!=null)
                {
                    if( user.getLastName()!=null
                            && user.getFirstName()!=null
                            && user.getGender()!=null) {
                        progressDialog.show();

                        firstNameEditText.setText(user.getFirstName());
                        lastNameEditText.setText(user.getLastName());
                        fullNameTextView.setText(user.getFirstName() + " " + user.getLastName());
                        if (user.getGender().equals(RegistrationActivity.MALE)) {
                            maleRadioButton.setChecked(true);
                            femaleRadioButton.setChecked(false);
                        } else {
                            maleRadioButton.setChecked(false);
                            femaleRadioButton.setChecked(true);
                        }


                        if( user.getImagePath() !=null) {
                            progressDialog.show();
                            Picasso.with(ProfileUpdateActivity.this)
                                    .load(user.getImagePath())
                                    .into(profileImageView, new com.squareup.picasso.Callback() {
                                        @Override
                                        public void onSuccess() {
                                            progressDialog.dismiss();
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }else {
                            progressDialog.dismiss();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void actionUpdate(View view) {
        firstName=firstNameEditText.getText().toString();
        lastName=lastNameEditText.getText().toString();
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();

        if(selectedId == maleRadioButton.getId()) {
            gender=RegistrationActivity.MALE;

        } else if(selectedId == femaleRadioButton.getId()) {
            gender=RegistrationActivity.FEMALE;
        } else {
            Toast.makeText(this, "Please select A Gender!", Toast.LENGTH_SHORT).show();
        }

        if(checkForUpdate() && validateForm())
        {
            user.setUserID(userId);
            user.setGender(gender);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            FirebaseService.getRootRef().child("Users").child(userId).setValue(user);
            Toast.makeText(this, "Profile Details Updated Successfully!!", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean checkForUpdate() {
        if( user.getGender() == gender && user.getFirstName()==firstName && user.getLastName() ==lastName)
        {
            return false;
        }else
        {
            return true;
        }

    }

    public void actionCancel(View view) {
        if( (MainActivity.firstTimeGoogleUser|| MainActivity.firstTimeGoogleUser )&& (firstNameEditText.getText().toString())=="")
        {
            Toast.makeText(this, "Please update your profile first", Toast.LENGTH_SHORT).show();
        }
        else if(MainActivity.firstTimeFacebookUser || MainActivity.firstTimeGoogleUser)
        {
            Intent intent = new Intent(ProfileUpdateActivity.this, UserListActivity.class);
            startActivity(intent);
            MainActivity.firstTimeFacebookUser=false;
            MainActivity.firstTimeGoogleUser=false;
        }else
        {
            this.onBackPressed();
        }
    }


    /**
     * Validates all form elements
     * @return
     */
    private boolean validateForm() {
        if (firstName.equals("")) {
            Toast.makeText(this, "Please enter full name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (lastName.equals("")) {
            Toast.makeText(this, "Please enter full name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void imageUpload(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
                try {
                    Bitmap bitmap = getBitmapFromUri(uri, getContentResolver());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] byteArray = baos.toByteArray();

                    String path = "profileImages/" + UUID.randomUUID() + ".png";
                    StorageReference ref = firebaseStorage.getReference(path);

                    progressDialog.show();
                    UploadTask uploadTask = ref.putBytes(byteArray);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // stop progress bar
                            progressDialog.dismiss();
                            Uri uploadedImageUri = taskSnapshot.getDownloadUrl();
                            user.setImagePath(uploadedImageUri.toString());
                            FirebaseService.getRootRef().child("Users").child(userId).setValue(user);
                            Toast.makeText(ProfileUpdateActivity.this, "Profile Image Updated Successfully!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    Toast.makeText(ProfileUpdateActivity.this, "Problem in fetching Image path", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public static Bitmap getBitmapFromUri(Uri uri, ContentResolver cr) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = cr.openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
