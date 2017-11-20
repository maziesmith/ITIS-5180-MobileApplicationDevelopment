package com.mad.inclass11;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SignUp extends Activity {
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPassword;

    ProgressDialog progressDialog;

    String fullName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextFirstName = (EditText) findViewById(R.id.edit_text_first_name);
        editTextLastName = (EditText) findViewById(R.id.edit_text_last_name);
        editTextEmail = (EditText) findViewById(R.id.edit_text_email);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
    }

    public void SignUpAction(View view) {
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (firstName.equals("")) {
            Toast.makeText(SignUp.this, "Please enter full name", Toast.LENGTH_SHORT).show();
        } else if (lastName.equals("")) {
            Toast.makeText(SignUp.this, "Please enter full name", Toast.LENGTH_SHORT).show();
        } else if (email.equals("")) {
            Toast.makeText(SignUp.this, "Please enter email", Toast.LENGTH_SHORT).show();
        } else if (!isValidEmail(email)) {
            Toast.makeText(SignUp.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
        } else if (!email.contains(".com")) {
            Toast.makeText(SignUp.this, "Please enter valid email (xyz@xyx.com)", Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(SignUp.this, "Please enter password", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(SignUp.this, "Please enter password with length > 6.", Toast.LENGTH_SHORT).show();
        } else {
            fullName = firstName + " " + lastName;
            MainActivity.isSignUpInProgress = true;
            progressDialog.show();
            MainActivity.firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = MainActivity.firebaseAuth.getCurrentUser();
                                if (user != null) {
                                    String userId = user.getUid();
                                    MainActivity.rootRef.child("Users").child(userId).setValue(fullName);
                                    MainActivity.firebaseAuth.signOut();
                                    MainActivity.isSignUpInProgress = false;
                                    progressDialog.dismiss();
                                    finish();
                                }
                            } else {
                                Toast.makeText(SignUp.this, "User already Exist.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void CancelSignUpAction(View view) {
        finish();
    }

    private boolean isValidEmail(CharSequence emailString) {
        if (emailString == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(emailString).matches();
        }
    }
}
