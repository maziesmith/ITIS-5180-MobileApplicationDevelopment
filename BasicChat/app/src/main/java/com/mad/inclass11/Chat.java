package com.mad.inclass11;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mad.inclass11.adapters.MessageChatAdapter;
import com.mad.inclass11.model.Chats;
import com.mad.inclass11.model.MessageChat;
import com.mad.inclass11.utility.TimeUtility;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Chat extends AppCompatActivity {
    private MessageChatAdapter chatAdapter;
    private ArrayList<Chats> chatList;
    private RecyclerView chatRecyclerView;

    String userName;
    ArrayList<MessageChat> messageChats;

    TextView textViewUserName;

    EditText editTextMessageText;

    private static final int SELECT_PICTURE = 1;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /*
        chatList = new ArrayList<Chats>();
        Chats chat= new Chats();
        chat.setUserName("Bob");
        chat.setComment(false);
        chat.setMessage("This is awesome");
        chat.setMessageType("Text");
        chat.setTime(TimeUtility.prettyDate(new Date()));
        chatList.add(chat);
        */

        messageChats = new ArrayList<MessageChat>();
        chatList = new ArrayList<Chats>();

        editTextMessageText = (EditText) findViewById(R.id.edit_text_message_text);
        textViewUserName = (TextView)  findViewById(R.id.textViewName);
        MainActivity.rootRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = (String) dataSnapshot.child(MainActivity.firebaseAuth.getCurrentUser().getUid()).getValue();
                if( userName!=null)
                {
                    textViewUserName.setText(userName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        MainActivity.rootRef.child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<MessageChat>> t = new GenericTypeIndicator<ArrayList<MessageChat>>() {};
                messageChats = dataSnapshot.getValue(t);

                if (messageChats == null) {
                    messageChats = new ArrayList<MessageChat>();
                }

                createChatsFromMessageChats();

                chatRecyclerView =  (RecyclerView)findViewById(R.id.chat_recycler_view);

                chatAdapter = new MessageChatAdapter(Chat.this, chatList, messageChats);
                chatRecyclerView.setAdapter(chatAdapter);
                chatRecyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void createChatsFromMessageChats() {
        chatList = new ArrayList<Chats>();
        Chats chat;
        Chats msgComment;

        for (int i = 0; i < messageChats.size(); i++) {
            chat = new Chats();
            chat.setMessageType(messageChats.get(i).getMessageType());
            chat.setMessage(messageChats.get(i).getMessage());
            chat.setImageUrl(messageChats.get(i).getImageUrl());
            chat.setTime(messageChats.get(i).getTime());
            chat.setUserName(messageChats.get(i).getUserName());
            chat.setComment(false);

            chatList.add(chat);

            if (messageChats.get(i).getComments() != null) {
                ArrayList<MessageChat> comments = messageChats.get(i).getComments();

                for (int j = 0; j < comments.size(); j++) {
                    msgComment = new Chats();
                    msgComment.setMessageType(comments.get(j).getMessageType());
                    msgComment.setMessage(comments.get(j).getMessage());
                    msgComment.setTime(comments.get(j).getTime());
                    msgComment.setUserName(comments.get(j).getUserName());
                    msgComment.setComment(true);
                    chatList.add(msgComment);
                }
            }
        }
    }

    public void LogoutAction(View view) {
        MainActivity.firebaseAuth.signOut();
        finish();
    }

    public void actionSend(View view) {
        String messageText = editTextMessageText.getText().toString();

        MessageChat messageChat = new MessageChat();
        messageChat.setMessageType("Text");
        messageChat.setUserName(userName);
        messageChat.setTime(Calendar.getInstance().getTime().toString());
        messageChat.setMessage(messageText);

        messageChats.add(messageChat);

        MainActivity.rootRef.child("Messages").setValue(messageChats);

        editTextMessageText.setText("");
    }

    public void actionAddPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
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

                    String path = "messageChatImages/" + UUID.randomUUID() + ".png";
                    StorageReference ref = firebaseStorage.getReference(path);

                    // start progress bar
                    UploadTask uploadTask = ref.putBytes(byteArray);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // stop progress bar
                            Uri uploadedImageUri = taskSnapshot.getDownloadUrl();

                            MessageChat messageChat = new MessageChat();
                            messageChat.setMessageType("Image");
                            messageChat.setUserName(userName);
                            messageChat.setTime(Calendar.getInstance().getTime().toString());
                            messageChat.setImageUrl(uploadedImageUri.toString());

                            messageChats.add(messageChat);

                            MainActivity.rootRef.child("Messages").setValue(messageChats);
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
}
