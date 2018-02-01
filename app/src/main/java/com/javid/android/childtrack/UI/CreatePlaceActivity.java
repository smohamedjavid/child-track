package com.javid.android.childtrack.UI;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.javid.android.childtrack.ApplicationActivity;
import com.javid.android.childtrack.R;
import com.javid.android.childtrack.pojo.Place;
import com.javid.android.childtrack.utils.DefaultCode;
import com.javid.android.childtrack.utils.Utils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class CreatePlaceActivity extends AppCompatActivity {

    String TAG = CreatePlaceActivity.class.toString();
    Utils utils;
    Context context;

    LatLng latLng;

    TextInputLayout textInputLayout_Amount;
    EditText edit_ChildName, edit_PlaceName, edit_Distance,edit_PhoneNo;
    Button btnCreatePlace;
    RadioGroup radioGroup_PlaceType;

    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = this;
        utils = new Utils(this);
        place = new Place();

        if(!utils.isLoggedIn()){
            utils.Toast("You must login to create a Place for tracking");
            utils.Goto(LoginActivity.class);
            return;
        }

        if(getIntent().hasExtra(DefaultCode.Intent_MapLocation)){
            latLng = getIntent().getParcelableExtra(DefaultCode.Intent_MapLocation);
            place.setLatitude(latLng.latitude);
            place.setLongitude(latLng.longitude);
        }

        initView();


    }

    void initView(){
        textInputLayout_Amount.setVisibility(View.GONE);

        edit_ChildName = (EditText)findViewById(R.id.editChildName);
        edit_PlaceName = (EditText)findViewById(R.id.editPlaceName);
        edit_Distance = (EditText)findViewById(R.id.editDistance);
        edit_PhoneNo = (EditText)findViewById(R.id.editPhoneNo);

        radioGroup_PlaceType = (RadioGroup)findViewById(R.id.radioGroup_type);


        radioGroup_PlaceType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                Log.e(TAG,"radio checked "+i);
            }
        });
        radioGroup_PlaceType.check(R.id.radio_typeEducation);

        btnCreatePlace = (Button)findViewById(R.id.btnCreate);
        btnCreatePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPlace();
            }
        });


    }

    void createPlace(){
        String ChildName = edit_ChildName.getText().toString();
        if(utils.isEmptyString(ChildName)){
            utils.Toast("Child name cannot be empty");
            return;
        }
        String PlaceName = edit_PlaceName.getText().toString();
        if(utils.isEmptyString(PlaceName)){
            utils.Toast("Place name cannot be empty");
            return;
        }
        String Dist = edit_Distance.getText().toString();
        if(utils.isEmptyString(Dist)){
            utils.Toast("Distance cannot be empty");
            return;
        }
        String PhoneNo = edit_PhoneNo.getText().toString();
        if(utils.isEmptyString(PhoneNo)){
            utils.Toast("PhoneNo cannot be empty");
            return;
        }

        if(place.getLatitude() == 0 && place.getLongitude() == 0){
            utils.Toast("Please select the location");
            return;
        }
        place.setChildname(ChildName);
        place.setPlacename(PlaceName);
        place.setDistance(Dist);
        place.setPhoneNo(PhoneNo);
        place.setUserUid(utils.getUserUid());

        ApplicationActivity.databaseReference.push().setValue(place).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                utils.Toast("Place successfully added");
                startActivity(getIntent());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                utils.Toast("Error in creating the place");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onPlaceTypeButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_typeEducation:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.radio_typeFamily:
                if (checked)

                    break;
            case R.id.radio_typeEntertain:
                if (checked)

                    break;

        }
    }

}
