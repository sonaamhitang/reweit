package com.skybasestudios.infoaround;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.skybasestudios.infoaround.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public static String KEY_EVENTS = "events_yo_nachala";
    private StorageReference mStorageRef;
    private GoogleMap mMap;
    private Context context;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        context = MapsActivity.this;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        myRef = database.getReference();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMapStyle(new MapStyleOptions())
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        LatLng home = new LatLng(28.2659496, 83.9721731);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        android.location.Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double latitude=28.2659496,longitude=83.9721731;
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("old", "lat :  " + latitude);
            Log.d("old", "long :  " + longitude);
//            this.onLocationChanged(location);
        }
        LatLng latLng = new LatLng(latitude, longitude);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
//                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker near Ghar"));
//                if(auth.getCurrentUser()!=null)
                addEventDialog(latLng);
//                else startActivity(new Intent(MapsActivity.this,LoginActivity.class));
            }
        });
//        mMap.addMarker(new MarkerOptions().position(home).title("Marker in Ghar"));
//        mMap.moveCamera();
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(home));
//        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                   if(addresses.size()>0) System.out.println(""+addresses.get(0).getPostalCode());
        } catch (IOException e) {
            e.printStackTrace();
        }

        myRef.child(KEY_EVENTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    System.out.println(noteDataSnapshot.toString());
//                    Event event = noteDataSnapshot.child()
                    Event event = new Event();
                    event.setEvent_title((String) noteDataSnapshot.child("event_title").getValue());
                    event.setEvent_latitude((Double) noteDataSnapshot.child("event_latitude").getValue());
                    event.setEvent_longitude((Double) noteDataSnapshot.child("event_longitude").getValue());
//                    MarkerOptions marker=;
                    mMap.addMarker(new MarkerOptions().position(new LatLng(event.getEvent_latitude(), event.getEvent_longitude())).
                            title(event.getEvent_title()));
//                    items.add(item);
                }
//                adapter.notifyDataSetChanged();
//                recyclerView.smoothScrollToPosition(items.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addEvent(String title, String description, LatLng latLng, String type, String startTime, String endTime) {
        Event event = new Event();
//        Location location = new Location();

        //add location to db first
//        location.setId(myRef.child(KEY_LOCATIONS).push().getKey());
        event.setEvent_latitude(latLng.latitude);
        event.setEvent_longitude(latLng.longitude);

//        event.setId(myRef.child(KEY_EVENTS).push().getKey());

//        location.setEvent_id(event.getId());
//        myRef.child(KEY_LOCATIONS).child(location.getId()).setValue(location);

        //add event next and set location id of uploaded location
        event.setEvent_id(myRef.child(KEY_EVENTS).push().getKey());
        event.setEvent_title(title);
        event.setEvent_description(description);
//        event.setLocation_id(location.getId());
        event.setEvent_creator_id("");
        event.setEvent_start_time(startTime);
        event.setEvent_end_time(endTime);
        event.setEvent_type(type);
//        event.setCreated_time("");
        myRef.child(KEY_EVENTS).child(event.getEvent_id()).setValue(event);
        myRef.child(KEY_EVENTS).child(event.getEvent_id()).child("created_time").setValue(ServerValue.TIMESTAMP);

//        {
//            "rules": {
//            ".read": "auth != null",
//                    ".write": "auth != null"
//        }
//        }

    }

    public void addEventDialog(final LatLng latLng) {
        LayoutInflater li = LayoutInflater.from(context);
        View feedbackview = li.inflate(R.layout.add_event_dialog, null);
        final EditText et_title = (EditText) feedbackview.findViewById(R.id.et_title);
        final EditText et_description = (EditText) feedbackview.findViewById(R.id.et_description);
        FancyButton btn_add = (FancyButton) feedbackview.findViewById(R.id.btn_add);
        FancyButton btn_start_time = (FancyButton) feedbackview.findViewById(R.id.btn_start_time);
        FancyButton btn_end_time = (FancyButton) feedbackview.findViewById(R.id.btn_end_time);
        btn_start_time.setVisibility(View.GONE);
        btn_end_time.setVisibility(View.GONE);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(feedbackview);
//        alertDialogBuilder
//                .setCancelable(false)
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString();
                String description = et_description.getText().toString();
                addEvent(title, description, latLng, "Sale", "2017-1-19T00:00", "2017-1-19T00:00");
                alertDialog.dismiss();
            }
        });
    }
//    private void marshmallowGPSPremissionCheck() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                && getActivity().checkSelfPermission(
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && getActivity().checkSelfPermission(
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(
//                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
//                            Manifest.permission.ACCESS_FINE_LOCATION},
//                    MY_PERMISSION_LOCATION);
//        } else {
//            //   gps functions.
//        }
//    }
}
