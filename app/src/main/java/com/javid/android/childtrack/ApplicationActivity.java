package com.javid.android.childtrack;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.javid.android.childtrack.pojo.Place;
import com.javid.android.childtrack.utils.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Javid on 20/1/18.
 */

public class ApplicationActivity extends Application {
    String TAG = ApplicationActivity.class.getSimpleName();
    Utils utils;
    Context context;

    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;

    public static ArrayList<Place> placeArrayList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        utils = new Utils(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference("PlaceList");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue() != null){
                    Place place = dataSnapshot.getValue(Place.class);
                    place.setKey(dataSnapshot.getKey());
                    Log.e(TAG,"Place added");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
