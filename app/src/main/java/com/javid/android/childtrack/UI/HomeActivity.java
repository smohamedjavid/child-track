package com.javid.android.childtrack.UI;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.javid.android.childtrack.R;
import com.javid.android.childtrack.UI.Fragments.AboutUsFragment;
import com.javid.android.childtrack.UI.Fragments.HomeFragment;
import com.javid.android.childtrack.interfaces.FragmentSetListener;
import com.javid.android.childtrack.interfaces.MarkerDialogInterface;
import com.javid.android.childtrack.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FirebaseAuth.AuthStateListener, LocationListener,HomeFragment.StateChangeInterface,MarkerDialogInterface , FragmentSetListener{

    String TAG = HomeActivity.class.getSimpleName();
    Context context;
    Utils utils;

    Menu nav_Login;
    public static final int FragmentContainerLayout = R.id.fragmentContainer;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    String locationPermission = Manifest.permission.ACCESS_COARSE_LOCATION;
    String locationPermission1 = Manifest.permission.ACCESS_FINE_LOCATION;

    static final int MY_PERMISSIONS_REQUEST_LOCATION = 990;

    LocationManager locationManager;

    final long LOCATION_REFRESH_TIME = 5000;
    final float LOCATION_REFRESH_DISTANCE = 20;

    HomeFragment homeFragment;

    boolean mapReady = false;
    static  Location knownLocation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getString(R.string.locatePlace));

        utils = new Utils(this);
        context = this;

        homeFragment = new HomeFragment();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(FragmentContainerLayout,homeFragment);
        fragmentTransaction.commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nav_Login = navigationView.getMenu();
        nav_Login.findItem(R.id.nav_login).setVisible(!utils.isLoggedIn());

        checkPermission();
    }

    void loadLocationModule() {
        Log.e(TAG, "location module loaded");



        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
            return;
        }

        Criteria locationCritera = new Criteria();
        locationCritera.setAccuracy(Criteria.ACCURACY_COARSE);
        locationCritera.setAltitudeRequired(false);
        locationCritera.setBearingRequired(false);
        locationCritera.setCostAllowed(true);
        locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);

        String providerName = locationManager.getBestProvider(locationCritera, true);

        if (providerName != null && locationManager.isProviderEnabled(providerName)) {
            // Provider is enabled
            Log.e(TAG,"location enabled "+providerName);
            locationManager.requestLocationUpdates(providerName, 20000, 100, this);
        } else {
            // Provider not enabled, prompt user to enable it
            utils.Toast("Please turn on the GPS");
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(myIntent);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, this);
    }

    void checkPermission(){
        Log.e(TAG,"location check init");
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, locationPermission);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, locationPermission1);
        if(permissionCheck1 != PackageManager.PERMISSION_GRANTED && permissionCheck2 != PackageManager.PERMISSION_GRANTED){
            Log.e(TAG,"location request init");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, locationPermission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
            }
            ActivityCompat.requestPermissions(this, new String[]{locationPermission,locationPermission1}, MY_PERMISSIONS_REQUEST_LOCATION);

        }else{
            loadLocationModule();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    loadLocationModule();
                }else{
                    utils.Toast("Permission denied");
                }
                break;
            default:
                utils.Toast("Request Code not fount");
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG,"result");
        loadLocationModule();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            utils.signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_home:
                setTitle(getString(R.string.locatePlace));
                homeFragment = new HomeFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(FragmentContainerLayout,homeFragment);
                fragmentTransaction.addToBackStack(HomeFragment.class.getSimpleName());
                fragmentTransaction.commit();
                break;
            case R.id.nav_login:
                utils.Goto(LoginActivity.class);
                break;
            case R.id.nav_logout:
                utils.signOut();
                nav_Login.findItem(R.id.nav_login).setVisible(!utils.isLoggedIn());
                break;
            case R.id.nav_postPlace:
                setTitle(getString(R.string.postPlace));
                startActivity(new Intent(this,CreatePlaceActivity.class));
                break;
            case R.id.nav_aboutUs:
                AboutUsFragment aboutUsFragment = new AboutUsFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(FragmentContainerLayout,aboutUsFragment);
                fragmentTransaction.addToBackStack(HomeFragment.class.getSimpleName());
                fragmentTransaction.commit();
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        nav_Login.findItem(R.id.nav_login).setVisible(!utils.isLoggedIn());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG,"location "+location.toString());
        utils.Toast("location :"+location.toString());
        knownLocation = location;
        if(mapReady){
            homeFragment.focusMap(location);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.e(TAG,"Provider enebled");
    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onStateChanged(boolean state) {
        mapReady = state;
        Log.e(TAG,"mapstate "+state);
    }

    @Override
    public void onLoaded() {
        if(homeFragment!= null){
            homeFragment.onLoaded();
        }
    }

    @Override
    public void onInitialise() {

    }

    @Override
    public void setTitleOfActivity(String title) {
        setTitle(title);
    }
}
