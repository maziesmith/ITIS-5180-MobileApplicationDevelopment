package com.mad.finalexam.firabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        //TODO : add rootNode
        return rootRef.child("Messages").child(currentChatId).getRef();
    }

    public static StorageReference getStorageRef() {

        //TODO: add storage ref
        return firebaseStorage.getReference("messageImages");
    }
}
