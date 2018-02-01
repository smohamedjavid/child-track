package com.javid.android.childtrack.UI.DialogBox;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.javid.android.childtrack.R;
import com.javid.android.childtrack.interfaces.MarkerDialogInterface;
import com.javid.android.childtrack.pojo.Place;
import com.javid.android.childtrack.utils.Utils;
import com.google.android.gms.tasks.RuntimeExecutionException;

/**
 * Created by Javid on 20/1/18.
 */

public class MarkerDetailsDialog extends DialogFragment {
    String TAG = MarkerDetailsDialog.class.getSimpleName();
    Context context;
    Utils utils;
    MarkerDialogInterface dialogInterface;

    Place place;

    TextView Txt_PlaceName, Txt_Distance,Txt_PhoneNo;
    Button Btn_Navigate;

    AlertDialog alertDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MarkerDialogInterface){
            dialogInterface = (MarkerDialogInterface) context;
        }else{
            throw new RuntimeExecutionException(new Throwable("MarkerDialogInterface Interface not implemented"));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        context = getActivity();
        utils = new Utils(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialogInterface.onLoaded();

        View view = (View) LayoutInflater.from(context).inflate(R.layout.single_marker_layout,null,false);
        Txt_PlaceName = (TextView)view.findViewById(R.id.txt_PlaceName);
        Txt_Distance = (TextView)view.findViewById(R.id.txt_Distance);
        Btn_Navigate = (Button)view.findViewById(R.id.btn_navigate);
        builder.setView(view);
        builder.setTitle(place.getChildname());
        alertDialog = builder.create();
        loadUI();
        return alertDialog;
    }

    public void setAlertDialog(AlertDialog alertDialog){
        this.alertDialog = alertDialog;

    }

    void loadUI(){

        Txt_PlaceName.setText(place.getPlacename());
        Txt_Distance.setText(place.getDistance());
        Txt_PhoneNo.setText(place.getPhoneNo());

        Btn_Navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"Clicked");
                String strUri = "http://maps.google.com/maps?q=loc:" + place.getLatitude() + "," + place.getLongitude() + " (" + place.getChildname() + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                startActivity(intent);

            }
        });
    }



    public void setDate(Place place){
        this.place = place;


    }
}
