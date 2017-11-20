package com.mad.finalexam.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.mad.finalexam.R;
import com.mad.finalexam.firabase.FirebaseService;
import com.mad.finalexam.model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private ProgressDialog progressDialog;


    private String firstName, lastName, email, password, confirmPassword, gender;

    public static final String MALE = "male";
    public static final String FEMALE = "female";
    private static final String EMPTY = "";
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private FirebaseAuth auth = FirebaseService.getFirebaseAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firstNameEditText = (EditText) findViewById(R.id.editTextFirstName);
        lastNameEditText = (EditText) findViewById(R.id.editTextLastName);
        emailEditText = (EditText) findViewById(R.id.editTextEmail);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        confirmPasswordEditText = (EditText) findViewById(R.id.editTextConfirmPassword);
        genderRadioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        maleRadioButton = (RadioButton) findViewById(R.id.radioButtonMale);
        femaleRadioButton = (RadioButton) findViewById(R.id.radioButtonFemale);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
    }


    /**
     * Validates all form elements
     *
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
        } else if (password.equals("")) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 6) {
            Toast.makeText(this, "Please enter password with length > 6.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(confirmPassword)) {
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

    public void actionSignUp(View view) {
        firstName = firstNameEditText.getText().toString();
        lastName = lastNameEditText.getText().toString();
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        confirmPassword = confirmPasswordEditText.getText().toString();

        int selectedId = genderRadioGroup.getCheckedRadioButtonId();

        if (selectedId == maleRadioButton.getId()) {
            gender = MALE;

        } else if (selectedId == femaleRadioButton.getId()) {
            gender = FEMALE;
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
                                    progressDialog.show();
                                    // stop progress bar
                                    userPOJO.setFirstName(firstName);
                                    userPOJO.setLastName(lastName);
                                    userPOJO.setUserID(userId);
                                    userPOJO.setGender(gender);
                                    FirebaseService.getRootRef().child("Users").child(userId).setValue(userPOJO);
                                    progressDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this, "Sign Up Successful!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(RegistrationActivity.this, "User already Exist.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }

    public void actionReset(View view) {
        firstNameEditText.setText("");
        lastNameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
    }

    public void actionCancel(View view) {
        finish();
    }
}
