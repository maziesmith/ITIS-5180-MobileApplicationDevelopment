package com.mad.finalexam.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.mad.finalexam.R;
import com.mad.finalexam.firabase.FirebaseService;

public class MenuBaseActivity extends AppCompatActivity {
    private ProgressDialog progressDiaglog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main_sign_out:
                if (BaseActivity.userSignedIn) {
                    progressDiaglog = new ProgressDialog(this);
                    progressDiaglog.setIndeterminate(true);
                    FirebaseService.getFirebaseAuth().signOut();
                    progressDiaglog.dismiss();
                } else {
                    Toast.makeText(MenuBaseActivity.this, "You are not logged In", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}