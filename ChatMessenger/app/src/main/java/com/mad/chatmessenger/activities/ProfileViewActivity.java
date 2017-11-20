package com.mad.chatmessenger.activities;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mad.chatmessenger.R;
import com.mad.chatmessenger.firebase.FirebaseService;
import com.mad.chatmessenger.model.User;
import com.squareup.picasso.Picasso;

public class ProfileViewActivity extends MenuBaseActivity {

    private String userId;
    private TextView fullNameTextView;
    private TextView genderTextView;
    private ImageView imageView;
    private User user=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        fullNameTextView=(TextView) findViewById(R.id.textViewFullName);
        genderTextView = (TextView) findViewById(R.id.textGender);
        imageView = (ImageView) findViewById(R.id.imageViewProfile);
        user = new User();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                userId = extras.getString(UserListActivity.USER_ID);
            }
        }

        FirebaseService.getRootRef().child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if( user!=null) {
                    if( user.getLastName()!=null
                            && user.getFirstName()!=null
                            && user.getGender()!=null ) {
                        fullNameTextView.setText(user.getFirstName() + " " + user.getLastName());
                        genderTextView.setText(user.getGender().toUpperCase());

                        if(user.getImagePath() !=null)
                        {
                            Picasso.with(ProfileViewActivity.this).load(user.getImagePath()).into(imageView);
                        }

                    }else
                    {
                        Toast.makeText(ProfileViewActivity.this, "Profile Currently Not available!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    }
                }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void actionBack(View view) {
        finish();
    }
}
