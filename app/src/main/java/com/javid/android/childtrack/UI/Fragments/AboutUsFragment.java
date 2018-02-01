package com.javid.android.childtrack.UI.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javid.android.childtrack.R;
import com.javid.android.childtrack.interfaces.FragmentSetListener;
import com.javid.android.childtrack.utils.Utils;
import com.google.android.gms.tasks.RuntimeExecutionException;

public class AboutUsFragment extends Fragment {
    String TAG = AboutUsFragment.class.getSimpleName();
    Context context;
    Utils utils;

    FragmentSetListener fragmentSetListener;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        context = view.getContext();
        utils = new Utils(context);

        fragmentSetListener.onInitialise();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FragmentSetListener){
            fragmentSetListener = (FragmentSetListener)context;
            fragmentSetListener.setTitleOfActivity("About Us");
        }else{
            throw new RuntimeExecutionException(new Throwable("FragmentSetListener Interface not implemented"));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentSetListener = null;
    }
}
