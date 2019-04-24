package com.maestromaster.foursquaremapexample;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.maestromaster.foursquaremapexample.items.Responce;
import com.maestromaster.foursquaremapexample.items.Venue;
import com.maestromaster.foursquaremapexample.items._MainResponce;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.TimeZone.SHORT;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback {

    private GoogleMap mMap;

    @BindView(R.id.refresh)
    ImageButton refresh;

    @BindView(R.id.bottomLayout)
    RelativeLayout bottomLayout;

    public static  int newWidth = 200;


    @BindView(R.id.viewPager)
    android.support.v4.view.ViewPager viewPager;

    MyPageAdapter pageAdapter;


    public static final String TAG = "myExample";


    public static final String CLIENT_ID = "NZ4WW5HNNFV5O1V1UBC3EB2HWELUHC32NABAK3HQROBSLQCE";
    public static final String CLIENT_SECRET = "EHU1DWTW2PEP21XZ2PMX44IHWK2AN5PIEPXGDNYLGNJCZKLH";


    double def_latval = 50.45466;
    double def_longval = 30.5238;

    public static final int RequestPermissionCode = 123;

    Location location;
    LocationManager locationManager;
    boolean GpsStatus = false;
    Criteria criteria;
    String Holder;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);

        viewPager.setClipChildren(false);

        int margins =  (int)convertDpToPixel(20, MapsActivity.this);
         newWidth = getScreenWidthDp() - 2 * margins;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewPager.getLayoutParams();
        params.width = newWidth;
        viewPager.setLayoutParams(params);

        refresh.setOnClickListener(v -> {
            checkRuntimePermission();
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // showLocation(def_latval, def_longval);

        // getVenues();

        Log.d(TAG, "onMapReady...");

        checkRuntimePermission();

    }


    private void getVenues(double lat, double lng) {


        String ll = Double.toString(lat) + "," + Double.toHexString(lng);  //"50.45466,30.5238";// "40.7,-74";
        String v = "20190416";

        App.getFoursquareApi().searchVenues(ll, CLIENT_ID, CLIENT_SECRET, v).enqueue(new Callback<_MainResponce>() {
            @Override
            public void onResponse(Call<_MainResponce> call, Response<_MainResponce> resop) {
                Log.d(TAG, "responce success");
                if (resop != null) {
                    Toast.makeText(MapsActivity.this, "responce success", SHORT);

                    Object respObj = resop.body();
                    if (respObj == null) {
                        showDialog("Responce is null");
                        return;
                    }
                    _MainResponce mainResponce = (_MainResponce) respObj;
                    if (mainResponce == null) {
                        showDialog("Responce is null");
                        return;
                    }
                    Responce responce = mainResponce.getResponse();

                    if (responce == null) return;

                    ArrayList<Venue> venues = responce.getVenues();

                    if (venues == null) return;

                    if (venues.size() == 0) return;

                    Log.d(TAG, "Venues list size = " + venues.size());

                    for (Venue venue : venues) {
                        // Log.d(TAG," venue: "+venue.getName()+" - "+venue.getLocation().getAddress());
                        createLocation(venue);
                    }

                    pageAdapter = new MyPageAdapter(venues,MapsActivity.this);

                    viewPager.setAdapter(pageAdapter);

                    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            if(position < (pageAdapter.getCount() - 1)){
                                Venue venue =  pageAdapter.getVenue(position);

                                Log.d(TAG, "We choose " + venue.getName());

                                com.maestromaster.foursquaremapexample.items.Location location = venue.getLocation();
                                showLocation(venue);
                            }
                        }

                        @Override
                        public void onPageSelected(int position) { }

                        @Override
                        public void onPageScrollStateChanged(int state) { }
                    });




                    viewPager.setOffscreenPageLimit(pageAdapter.getCount());
                    viewPager.setPageMargin(10);
                    viewPager.setClipChildren(false);







                    showLocation(venues.get(0));


                } else {
                    Log.d(TAG, "3");
                    Toast.makeText(MapsActivity.this, "Unknown error", SHORT);
                }
            }

            @Override
            public void onFailure(Call<_MainResponce> call, Throwable t) {
                Log.d(TAG, "responce error");
            }
        });
    }

    ArrayList<Marker> markers = new ArrayList<>();

    private Marker getSelectedMarker(double lat, double lng) {
        for (Marker marker : markers) {
            if (marker.getPosition().latitude == lat && marker.getPosition().longitude == lng)
                return marker;
        }
        return null;
    }


    private void createLocation(Venue venue) {

        com.maestromaster.foursquaremapexample.items.Location location = venue.getLocation();
        Marker locationMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLat(), location.getLng()))
                .title(venue.getName())
                .snippet(venue.getLocation().getAddress())

                //.rotation((float) -15.0)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .visible(true)
        );

        markers.add(locationMarker);



       /* mMap.addCircle(new CircleOptions()
                .center(new LatLng(location.getLat(),    location.getLng()))
                .radius(50)
                .strokeColor(Color.RED)
                .fillColor(Color.TRANSPARENT));*/
    }


    private void showLocation(Venue venue) {
        com.maestromaster.foursquaremapexample.items.Location location = venue.getLocation();
        double lat = location.getLat();
        double lng = location.getLng();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat, lng), 16));


        Marker marker = getSelectedMarker(lat, lng);
        if (marker != null) {
            marker.showInfoWindow();
        }
        //


    }


    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void checkRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            Log.d(TAG, "ACCESS_FINE_LOCATION permission allows us to Access GPS in app");
            getMyLocation();
            Toast.makeText(MapsActivity.this, "ACCESS_FINE_LOCATION permission allows us to Access GPS in app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MapsActivity.this, "Permission Granted, Now your application can access GPS.", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Permission Granted, Now your application can access GPS");
                    getMyLocation();

                } else {
                    Log.d(TAG, "Permission Canceled, Now your application cannot access GPS.");
                    Toast.makeText(MapsActivity.this, "Permission Canceled, Now your application cannot access GPS.", Toast.LENGTH_LONG).show();
                    getVenues(def_latval, def_longval);

                }
                break;
        }
    }

    private void getMyLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        Holder = locationManager.getBestProvider(criteria, false);

        checkGpsStatus();

        Log.d(TAG, "GpsStatus = " + GpsStatus);

        if (GpsStatus == true) {

            Location myLocation = getLastKnownLocation();
            if (myLocation != null) {
                Log.d(TAG, "location =  " + myLocation.getLatitude() + " : " + myLocation.getLongitude());

                getVenues(myLocation.getLatitude(), myLocation.getLongitude());

            } else {
                Log.d(TAG, "location is null  ");
                getVenues(def_latval, def_longval);
            }
        } else {

            Toast.makeText(MapsActivity.this, "Please Enable GPS First", Toast.LENGTH_LONG).show();

            getVenues(def_latval, def_longval);

        }
    }


    LocationManager mLocationManager;


    private Location getLastKnownLocation() {
        Log.d(TAG,"getLastKnownLocation");
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"no permissions");
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                Log.d(TAG,"l == null");
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                Log.d(TAG,"Found best last known location:");
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    public void checkGpsStatus(){

        locationManager = (LocationManager)getBaseContext().getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    private int getScreenWidthDp(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
       // int height = size.y;

        return  width;
    }

    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }



}
