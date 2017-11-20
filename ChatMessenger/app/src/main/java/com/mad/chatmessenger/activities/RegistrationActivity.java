package com.mad.chatmessenger.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mad.chatmessenger.R;
import com.mad.chatmessenger.firebase.FirebaseService;
import com.mad.chatmessenger.model.User;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private ImageView profileImageView;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private ProgressDialog progressDialog;


    private String firstName, lastName, email, password, confirmPassword, gender;

    public static final String MALE="male";
    public static final String FEMALE="female";
    private static final String EMPTY="";
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri uri = null;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private FirebaseAuth auth= FirebaseService.getFirebaseAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firstNameEditText = (EditText) findViewById(R.id.editTextFirstName);
        lastNameEditText = (EditText) findViewById(R.id.editTextLastName);
        emailEditText = (EditText) findViewById(R.id.editTextEmail);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        confirmPasswordEditText = (EditText) findViewById(R.id.editTextConfirmPassword);
        profileImageView = (ImageView) findViewById(R.id.imageViewProfile);
        genderRadioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        maleRadioButton =  (RadioButton) findViewById(R.id.radioButtonMale);
        femaleRadioButton = (RadioButton) findViewById(R.id.radioButtonFemale);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);

    }


    /**
     * Sign Up event Handler
     *
     * @param view
     */
    public void actionSignUp(View view) {

        firstName = firstNameEditText.getText().toString();
        lastName = lastNameEditText.getText().toString();
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        confirmPassword = confirmPasswordEditText.getText().toString();

        int selectedId = genderRadioGroup.getCheckedRadioButtonId();

        if(selectedId == maleRadioButton.getId()) {
            gender=MALE;

        } else if(selectedId == femaleRadioButton.getId()) {
            gender=FEMALE;
        } else {
            Toast.makeText(this, "Please select Gender!", Toast.LENGTH_SHORT).show();
        }




        if (validateForm()) {
            progressDialog.show();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    final User userPOJO = new User();
                                    final String userId = user.getUid();
                                    try {
                                        Bitmap bitmap = getBitmapFromUri(uri, getContentResolver());
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                        byte[] byteArray = baos.toByteArray();

                                        String path = "profileImages/" + UUID.randomUUID() + ".png";
                                        StorageReference ref = firebaseStorage.getReference(path);

                                        UploadTask uploadTask = ref.putBytes(byteArray);
                                        progressDialog.dismiss();
                                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                progressDialog.show();
                                                // stop progress bar
                                                Uri uploadedImageUri = taskSnapshot.getDownloadUrl();
                                                userPOJO.setImagePath(uploadedImageUri.toString());
                                                userPOJO.setFirstName(firstName);
                                                userPOJO.setLastName(lastName);
                                                userPOJO.setUserID(userId);
                                                userPOJO.setGender(gender);
                                                FirebaseService.getRootRef().child("Users").child(userId).setValue(userPOJO);
                                                progressDialog.dismiss();
                                                Toast.makeText(RegistrationActivity.this, "Sign Up Successful!", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    } catch (IOException e) {
                                        Toast.makeText(RegistrationActivity.this, "Problem in fetching Image path", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            } else {
                                Toast.makeText(RegistrationActivity.this, "User already Exist.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /**
     * Cancel event Handler
     *
     * @param view
     */
    public void actionCancel(View view) {
        finish();
    }

    /**
     * imageUpload Event Handler
     *
     * @param view
     */
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, "Image selection failed, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Reset Button Event Handler
     *
     * @param view
     */
    public void actionReset(View view) {
        firstNameEditText.setText(EMPTY);
        lastNameEditText.setText(EMPTY);
        emailEditText.setText(EMPTY);
        passwordEditText.setText(EMPTY);
        confirmPasswordEditText.setText(EMPTY);
        profileImageView.setImageResource(R.drawable.user_upload);
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
        } else if (email.equals("")) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!email.contains(".com")) {
            Toast.makeText(this, "Please enter valid email (xyz@xyx.com)", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.equals("")) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 6) {
            Toast.makeText(this, "Please enter password with length > 6.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password don't match", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidEmail(CharSequence emailString) {
        if (emailString == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(emailString).matches();
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
