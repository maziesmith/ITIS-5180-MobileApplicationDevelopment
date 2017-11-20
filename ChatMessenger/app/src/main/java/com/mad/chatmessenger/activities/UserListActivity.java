package com.mad.chatmessenger.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.mad.chatmessenger.R;
import com.mad.chatmessenger.firebase.FirebaseService;
import com.mad.chatmessenger.model.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UserListActivity extends MenuBaseActivity {

    private RecyclerView mRecyclerView;
    public static final String USER_ID = "user_id";
    private static String currentUSerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recylerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        currentUSerId = FirebaseService.GetCurrentUser().getUid();

        FirebaseRecyclerAdapter<User, UserViewHolder> adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(
                User.class,
                R.layout.user_list_recycler_layout,
                UserViewHolder.class,
                FirebaseService.getUSerListRef().orderByChild("firstName")
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, final User model, int position) {
                TextView fullNameTextView = viewHolder.fullName;
                ImageView thumbnailImageView = viewHolder.displayPicThumbnail;
                TextView unreadMessageCount = viewHolder.unreadMessageCount;

                final View view = viewHolder.view;
                fullNameTextView.setText(model.getFirstName() + " " + model.getLastName());

                HashMap<String, Integer> unreadMessageInfo = model.getUnreadMessageInfo();
                if (unreadMessageInfo != null) {
                    for (Map.Entry<String, Integer> entry : unreadMessageInfo.entrySet()) {
                        String key = entry.getKey();
                        if (key.equals(FirebaseService.getFirebaseAuth().getCurrentUser().getUid())) {
                            if (entry.getValue().equals(0)) {
                                unreadMessageCount.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(UserListActivity.this, entry.getValue().toString(), Toast.LENGTH_SHORT).show();
                                unreadMessageCount.setText(entry.getValue().toString());
                            }
                        }
                    }
                } else {
                    // This is magic
                }

                if (unreadMessageCount.getText().toString().equals("")) {
                    unreadMessageCount.setVisibility(View.INVISIBLE);
                }

                Picasso.with(UserListActivity.this).load(model.getImagePath()).into(thumbnailImageView);
                thumbnailImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(view.getContext(), ProfileViewActivity.class);
                        intent.putExtra(USER_ID, model.getUserID());
                        startActivity(intent);
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(view.getContext(), ChatActivity.class);
                        intent.putExtra(USER_ID, model.getUserID());
                        startActivity(intent);
                    }
                });
            }

        };

        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        if (BaseActivity.userSignedIn) {
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
        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView displayPicThumbnail;
        TextView fullName;
        TextView unreadMessageCount;

        public UserViewHolder(final View itemView) {
            super(itemView);
            displayPicThumbnail = (ImageView) itemView.findViewById(R.id.imageViewThumbnail);
            fullName = (TextView) itemView.findViewById(R.id.textViewFullName);
            unreadMessageCount = (TextView) itemView.findViewById(R.id.text_view_unread_count);
            view = itemView;
        }
    }
}
