package com.mad.chatmessenger.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mad.chatmessenger.activities.LoginActivity;
import com.mad.chatmessenger.activities.MainActivity;

/**
 * Created by Sanket on 11/20/2016.
 */

public class FirebaseService {
    public static FirebaseAuth firebaseAuth;
    public static DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    public static FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

   public static FirebaseAuth getFirebaseAuth()
   {
       firebaseAuth = FirebaseAuth.getInstance();
       return  firebaseAuth;
   }

    public static DatabaseReference getRootRef(){
        return rootRef;
    }

    public static FirebaseUser GetCurrentUser()
    {
       return firebaseAuth.getCurrentUser();
    }

    public static DatabaseReference getUSerListRef()
    {
       return rootRef.child("Users");
    }

    public static DatabaseReference getCurrentChatRef(String currentChatId)
    {
        return rootRef.child("Messages").child(currentChatId).getRef();
    }

    public static StorageReference getStorageRef() {
        return firebaseStorage.getReference("messageImages");
    }
}
