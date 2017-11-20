package com.mad.chatmessenger.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.mad.chatmessenger.R;
import com.mad.chatmessenger.Utility.Util;
import com.mad.chatmessenger.firebase.FirebaseService;
import com.mad.chatmessenger.model.Message;
import com.mad.chatmessenger.model.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class ChatActivity extends MenuBaseActivity {
    private String currentUserId;
    private String currentUserName;
    private String peerUserId;
    private String peerUserName;
    private String currentChatId;

    private int unreadCount = 0;

    private static int SELECT_PICTURE = 1;

    private RecyclerView recyclerViewMessages;
    private TextView editTextPeerName;
    private ImageView imageViewPeerImage;
    private EditText editTextMessageToSend;
    private ProgressDialog progressDialog;

    FirebaseRecyclerAdapter<Message, ChatActivity.MessageViewHolder> adapter;

    ValueEventListener selfListner = null;
    ValueEventListener peerListner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerViewMessages = (RecyclerView) findViewById(R.id.recycler_view_messages);
        recyclerViewMessages.setHasFixedSize(true);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

        editTextMessageToSend = (EditText) findViewById(R.id.edit_text_message_to_send);
        editTextPeerName = (TextView) findViewById(R.id.text_view_peer_name);
        imageViewPeerImage = (ImageView) findViewById(R.id.image_view_user_profile_pic);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);

        if (getIntent().getExtras() != null) {
            currentUserId = FirebaseService.GetCurrentUser().getUid();
            peerUserId = getIntent().getExtras().getString(UserListActivity.USER_ID);

            //peerUserId = "GR8mPbrmq3QvARTBguTt1nIe6of1";

            currentChatId = Util.getMergedId(currentUserId, peerUserId);

            FirebaseService.getUSerListRef().child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    currentUserName = currentUser.getFirstName() + " " + currentUser.getLastName();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            FirebaseService.getUSerListRef().child(peerUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User peerUser = dataSnapshot.getValue(User.class);
                    peerUserName = peerUser.getFirstName() + " " + peerUser.getLastName();
                    editTextPeerName.setText(peerUserName);
                    Picasso.with(ChatActivity.this).load(peerUser.getImagePath()).into(imageViewPeerImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError() {
                            progressDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            selfListner = FirebaseService.getUSerListRef().child(currentUserId).child("unreadMessageInfo").child(peerUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String abc = "";
                    if (dataSnapshot.getValue() != null) {
                        unreadCount = Integer.parseInt(dataSnapshot.getValue().toString());
                    }
                    //User currentUser = dataSnapshot.getValue(User.class);
                    //currentUserName = currentUser.getFirstName() + " " + currentUser.getLastName();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            peerListner = FirebaseService.getUSerListRef().child(peerUserId).child("unreadMessageInfo").child(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String abc = "";
                    if (dataSnapshot.getValue() != null) {
                        FirebaseService.getUSerListRef().child(peerUserId).child("unreadMessageInfo").child(currentUserId).setValue(0);
                    }
                    //User currentUser = dataSnapshot.getValue(User.class);
                    //currentUserName = currentUser.getFirstName() + " " + currentUser.getLastName();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            adapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                    Message.class,
                    R.layout.layout_chat_item,
                    ChatActivity.MessageViewHolder.class,
                    FirebaseService.getCurrentChatRef(currentChatId)
            ) {
                @Override
                protected void populateViewHolder(ChatActivity.MessageViewHolder viewHolder, Message model, final int position) {
                    viewHolder.chatMesssage.setText("");
                    viewHolder.chatImage.setImageBitmap(null);
                    if (model.getType().equals("TEXT")) {
                        viewHolder.chatMesssage.setText(model.getText());
                    } else {
                        progressDialog.show();
                        Picasso.with(ChatActivity.this).load(model.getImageUrl()).into(viewHolder.chatImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onError() {
                                progressDialog.dismiss();
                            }
                        });
                    }

                    if (model.getUserId().equals(currentUserId)) {
                        viewHolder.userName.setText(currentUserName);
                    } else {
                        viewHolder.userName.setText(peerUserName);
                    }

                    try {
                        viewHolder.timeFromNow.setText(Util.prettyDate(model.getTime()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    viewHolder.deleteMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.getRef(position).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });
                }
            };

            recyclerViewMessages.setAdapter(adapter);
        }
    }

    public void actionSendTextMessage(View view) {
        String messageText = editTextMessageToSend.getText().toString();

        Message message = new Message();
        message.setId("1");
        message.setType("TEXT");
        message.setText(messageText);
        message.setUserId(currentUserId);
        message.setRead(false);
        message.setTime(Calendar.getInstance().getTime().toString());

        FirebaseService.getCurrentChatRef(currentChatId).push().setValue(message);

        unreadCount = unreadCount + 1;
        FirebaseService.getUSerListRef().child(currentUserId).child("unreadMessageInfo").child(peerUserId).setValue(unreadCount);

        editTextMessageToSend.setText("");
    }

    public void actionSendImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE && data != null) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = getBitmapFromUri(selectedImageUri, getContentResolver());

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] byteArray = baos.toByteArray();

                    //String path = "messageChatImages/" + UUID.randomUUID() + ".png";
                    String fileName = UUID.randomUUID() + ".png";
                    //StorageReference ref = firebaseStorage.getReference(path);

                    UploadTask uploadTask = FirebaseService.getStorageRef().child(fileName).putBytes(byteArray);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri uploadedImageUri = taskSnapshot.getDownloadUrl();

                            Message message = new Message();
                            message.setId("1");
                            message.setType("IMAGE");
                            message.setImageUrl(uploadedImageUri.toString());
                            message.setUserId(currentUserId);
                            message.setRead(false);
                            message.setTime(Calendar.getInstance().getTime().toString());

                            FirebaseService.getCurrentChatRef(currentChatId).push().setValue(message);

                            unreadCount = unreadCount + 1;
                            FirebaseService.getUSerListRef().child(currentUserId).child("unreadMessageInfo").child(peerUserId).setValue(unreadCount);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView chatImage;
        TextView chatMesssage;
        TextView userName;
        TextView timeFromNow;
        ImageView deleteMessage;

        public MessageViewHolder(final View itemView) {
            super(itemView);
            chatImage = (ImageView) itemView.findViewById(R.id.image_view_chat_image);
            chatMesssage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            userName = (TextView) itemView.findViewById(R.id.text_view_user_name);
            timeFromNow = (TextView) itemView.findViewById(R.id.text_view_time_from_now);
            deleteMessage = (ImageView) itemView.findViewById(R.id.image_view_delete_message);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (selfListner != null) {
            FirebaseService.getUSerListRef().child(currentUserId).child("unreadMessageInfo").child(peerUserId).removeEventListener(selfListner);
        }

        if (peerListner != null) {
            FirebaseService.getUSerListRef().child(peerUserId).child("unreadMessageInfo").child(currentUserId).removeEventListener(peerListner);
        }
        finish();
    }
}
