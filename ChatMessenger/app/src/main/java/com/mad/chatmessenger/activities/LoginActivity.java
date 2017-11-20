package com.mad.chatmessenger.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.chatmessenger.R;
import com.mad.chatmessenger.firebase.FirebaseService;

public class LoginActivity extends AppCompatActivity  {

    private Button signInButton, signUpButton;
    private EditText emailEditText, passwordEditText;
    private ProgressDialog progressDialog;

    private FirebaseAuth auth= FirebaseService.getFirebaseAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton= (Button) findViewById(R.id.buttonLogin);
        signUpButton= (Button) findViewById(R.id.buttonSignUp);
        emailEditText= (EditText) findViewById(R.id.editTextEmail);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);



        //FirebaseUser user = auth.getCurrentUser();

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);

/*        MainActivity.firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && !MainActivity.isSignUpInProgress) {
                    progressDialog.dismiss();
                    userSignedIn=true;
                    Toast.makeText(LoginActivity.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                    //TODO add activity reference later
                    //Intent intentObj = new Intent(MainActivity.this, Chat.class);
                    //  startActivity(intentObj);
                }
            }
        });*/

    }

    /**
     * Handler for Login Button
     * @param view
     */
    public void actionLogin(View view) {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.equals("")) {
            Toast.makeText(LoginActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    @Override
    public void onBackPressed() {
       if(BaseActivity.userSignedIn) {
           new AlertDialog.Builder(this)
                   .setTitle("Really Exit?")
                   .setMessage("Are you sure you want to exit?")
                   .setNegativeButton(android.R.string.no, null)
                   .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface arg0, int arg1) {
                           Intent intent = new Intent(Intent.ACTION_MAIN);
                           intent.addCategory(Intent.CATEGORY_HOME);
                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(intent);
                           finish();

                       }
                   }).create().show();
       }else
       {
           Intent intent = new Intent(LoginActivity.this, MainActivity.class);
           startActivity(intent);
       }
    }




}
