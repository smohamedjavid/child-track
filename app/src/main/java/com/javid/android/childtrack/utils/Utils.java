package com.javid.android.childtrack.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.javid.android.childtrack.R;
import com.javid.android.childtrack.UI.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Javid on 20/1/18.
 */

public class Utils {
    private String TAG = Utils.class.getSimpleName();
    private Context context;

    public ProgressDialog progressDialog;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;

   // private static UserProfile userProfile;
    private Gson gson;
    private SharedPreferences sharedPreferences;

    public Utils(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        sharedPreferences = context.getSharedPreferences(DefaultCode.PREF_STRING,Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public DatabaseReference getDataBaseReference(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(context.getString(R.string.FireBaseMainRef));
        return databaseReference;
    }

    public void signOut(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(DefaultCode.PREF_USER);
        editor.apply();
        FirebaseAuth.getInstance().signOut();
        if(!(context instanceof HomeActivity)){
            Goto(HomeActivity.class);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setProgressDialogMessage(String Msg){
        progressDialog.setMessage(Msg);
    }

    public void showProgressDialog(){
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    public void hideProgressDialog(){
        if(progressDialog.isShowing()){
            progressDialog.hide();
        }
    }

    public void closeProgressDialog(){
        progressDialog.dismiss();
    }

    public void Goto(Class activityClass){
        Intent i = new Intent(context,activityClass);
        context.startActivity(i);
    }

    public void Toast(String Msg){
        Toast.makeText(context,Msg,Toast.LENGTH_SHORT).show();
    }


    public String getTimeStamp(){
        String timeStamp =new SimpleDateFormat(DefaultCode.DEFAULT_TIMESTAMP_FORMAT, Locale.getDefault()).format(new Date());
        return timeStamp;
    }

    public String getUserUid(){
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            signOut();
            return "";
        }else {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

    }

    public boolean isLoggedIn(){
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public boolean isEmptyString(String... Args){
        for (String value: Args) {
            if(value.equals("") || value.isEmpty()){
                return true;
            }
        }
        return false;
    }



}
