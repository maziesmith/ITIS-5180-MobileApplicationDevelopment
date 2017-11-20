package com.mad.finalexam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.finalexam.firabase.FirebaseService;

/**
 * Created by Sanket on 12/11/2016.
 */

public class BaseActivity extends AppCompatActivity {


    public static boolean userSignedIn = false;
    private FirebaseAuth auth = FirebaseService.getFirebaseAuth();
    private Intent intentObj;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    userSignedIn=true;
                    intentObj = new Intent(BaseActivity.this, HomeActivity.class);
                } else {
                    intentObj = new Intent(BaseActivity.this, MainActivity.class);
                }
                startActivity(intentObj);

            }
        });
    }
}
