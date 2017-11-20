package com.mad.chatmessenger.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.chatmessenger.R;
import com.mad.chatmessenger.firebase.FirebaseService;

public class BaseActivity extends AppCompatActivity {


    public static boolean userSignedIn = false;
    private FirebaseAuth auth= FirebaseService.getFirebaseAuth();
    private Intent intentObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);

        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null ) {
                    userSignedIn=true;
                    if( MainActivity.firstTimeFacebookUser || MainActivity.firstTimeGoogleUser )
                    {
                        intentObj = new Intent(BaseActivity.this, ProfileUpdateActivity.class);

                    }else {

                        intentObj = new Intent(BaseActivity.this, UserListActivity.class);
                    }startActivity(intentObj);

                }else {
                    Intent intentObj = new Intent(BaseActivity.this, MainActivity.class);
                    startActivity(intentObj);
                }
            }
        });

    }


}
