package com.skybasestudios.infoaround;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import mehdi.sakout.fancybuttons.FancyButton;

import static com.skybasestudios.infoaround.R.id.map;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    public static int AUTH_REQUEST_CODE = 1089;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public static String KEY_EVENTS = "events_yo_nachala";
    private StorageReference mStorageRef;
    private GoogleMap mMap;
    private Context context;
    LocationManager locationManager;
    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Marker locationMarker;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        database = FirebaseDatabase.getInstance();
//        database.setPersistenceEnabled(true);
        context = MainActivity.this;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        myRef = database.getReference();
//        if (database == null) {
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            database.setPersistenceEnabled(true);
//            mDatabase = database.getReference();
//            // ...
//        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == AUTH_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        isGPSEnable();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
        final Location[] location = new Location[1];
        SmartLocation.with(context).location()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location0) {
                        location[0] = location0;
//                        location[0].getLatitude();
//                        location[0].getLongitude();
                        double latitude = 0, longitude = 0;
                        if (location[0] != null) {
                            latitude = location[0].getLatitude();
                            longitude = location[0].getLongitude();
                            Log.d("old", "lat :  " + latitude);
                            Log.d("old", "long :  " + longitude);
                        }
                        LatLng latLng = new LatLng(latitude, longitude);
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng)      // Sets the center of the map to Mountain View
                                .zoom(17)                   // Sets the zoom
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
//                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker near Ghar"));
                if (auth.getCurrentUser() != null)
                    addEventDialog(latLng);
                else
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), AUTH_REQUEST_CODE);
            }
        });
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        List<Address> addresses = null;
//        try {
//            addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            if (addresses.size() > 0) System.out.println("" + addresses.get(0).getPostalCode());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        myRef.child(KEY_EVENTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    mMap.clear();
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                        noteDataSnapshot.getKey()
                        System.out.println("Data:" + noteDataSnapshot.toString());
                        final Event event = new Event();

                        event.setEvent_id((String) noteDataSnapshot.child("event_id").getValue());
                        event.setEvent_title((String) noteDataSnapshot.child("event_title").getValue());
//                        double latitude = 28.2659496, longitude = 83.9721731;
                        if (noteDataSnapshot.child("event_latitude").getValue() != null || noteDataSnapshot.child("event_longitude").getValue() != null) {
                            event.setEvent_latitude((Double) noteDataSnapshot.child("event_latitude").getValue());
                            event.setEvent_longitude((Double) noteDataSnapshot.child("event_longitude").getValue());
                        }
//                            event.setEvent_latitude(latitude);
//                            event.setEvent_longitude(longitude);
                        event.setEvent_type((String) noteDataSnapshot.child("event_type").getValue());
                        BitmapDescriptor bitmapDescriptor;
                        String event_type = "" + event.getEvent_type();
                        switch (event_type) {
                            case "Event":
                                bitmapDescriptor = vectorToBitmap(R.drawable.ic_event, getResources().getColor(R.color.colorEvent));
                                break;
                            case "Weather":
                                bitmapDescriptor = vectorToBitmap(R.drawable.ic_weather, getResources().getColor(R.color.colorWeather));
                                break;
                            case "Information":
                                bitmapDescriptor = vectorToBitmap(R.drawable.ic_info, getResources().getColor(R.color.colorInfo));
                                break;
                            case "Emergency":
                                bitmapDescriptor = vectorToBitmap(R.drawable.ic_emergency, getResources().getColor(R.color.colorEmergency));
                                break;
                            case "Traffic":
                                bitmapDescriptor = vectorToBitmap(R.drawable.ic_traffic, getResources().getColor(R.color.colorTraffic));
                                break;
                            default:
                                bitmapDescriptor = vectorToBitmap(R.drawable.ic_marker, Color.BLACK);
                                break;
                        }
                        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(event.getEvent_latitude(), event.getEvent_longitude())).
                                title(event.getEvent_title())
                                .icon(bitmapDescriptor);
                        locationMarker = mMap.addMarker(markerOptions);
                        locationMarker.setTag(event.getEvent_id());
                    }
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            System.out.println(myRef.child(KEY_EVENTS).child("" + marker.getTag()));
                            String name = marker.getTitle();
                            if (name.equalsIgnoreCase("My Spot")) {
                                //write your code here
                            }
                            showDetailsDialog(marker);
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private BitmapDescriptor vectorToBitmap(@DrawableRes int id, @ColorInt int color) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void addEvent(String title, String description, LatLng latLng, String type, String startTime, String endTime) {
        Event event = new Event();
        event.setEvent_latitude(latLng.latitude);
        event.setEvent_longitude(latLng.longitude);

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
        myRef.child(KEY_EVENTS).child(event.getEvent_id()).child("event_timestamp").setValue(ServerValue.TIMESTAMP);
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
        final Spinner spinner1 = (Spinner) feedbackview.findViewById(R.id.event_choices);
        final EditText et_title = (EditText) feedbackview.findViewById(R.id.et_title);
        final EditText et_description = (EditText) feedbackview.findViewById(R.id.et_description);
        FancyButton btn_add = (FancyButton) feedbackview.findViewById(R.id.btn_add);
        FancyButton btn_start_time = (FancyButton) feedbackview.findViewById(R.id.btn_start_time);
        FancyButton btn_end_time = (FancyButton) feedbackview.findViewById(R.id.btn_end_time);
        final TextView tv_start_time = (TextView) feedbackview.findViewById(R.id.tv_start_time);
        final TextView tv_end_time = (TextView) feedbackview.findViewById(R.id.tv_end_time);
        final LinearLayout event_time_ll = (LinearLayout) feedbackview.findViewById(R.id.event_time_ll);
        final String[] start_date = {""}, end_date = {""};
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tv_start_time.setText(dateFormatter.format(newDate.getTime()));
                start_date[0] = dateFormatter.format(newDate.getTime());
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tv_end_time.setText(dateFormatter.format(newDate.getTime()));
                end_date[0] = dateFormatter.format(newDate.getTime());
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        btn_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePickerDialog.show();
            }
        });
        btn_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDatePickerDialog.show();
            }
        });
        event_time_ll.setVisibility(View.GONE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(feedbackview);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString();
                String description = et_description.getText().toString();
                addEvent(title, description, latLng, spinner1.getSelectedItem().toString(), start_date[0], end_date[0]);
                alertDialog.dismiss();
            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        event_time_ll.setVisibility(View.VISIBLE);
                        break;
                    default:
                        event_time_ll.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void showDetailsDialog(final Marker marker) {

//        String title = myRef.child(KEY_EVENTS).child("" + marker.getTag()).child("event_title").toString();
//        String description = myRef.child(KEY_EVENTS).child("" + marker.getTag()).child("event_description").toString();
        LayoutInflater li = LayoutInflater.from(context);
        View detailsView = li.inflate(R.layout.details_card, null);
        final TextView tv_title = (TextView) detailsView.findViewById(R.id.details_title);
        final TextView tv_description = (TextView) detailsView.findViewById(R.id.details_description);
//        tv_title.setText(title);
//        tv_description.setText(description);
        final FancyButton like = (FancyButton) detailsView.findViewById(R.id.details_like);
        final FancyButton dislike = (FancyButton) detailsView.findViewById(R.id.details_dislike);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(detailsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth.getCurrentUser()!=null)
                {
                    String like_id = myRef.child(KEY_EVENTS).child("" + marker.getTag()).child("likes").push().getKey();
                    myRef.child(KEY_EVENTS).child("" + marker.getTag()).child("likes").child(like_id).setValue(auth.getCurrentUser().getEmail());
                    alertDialog.dismiss();
                }
               else
                {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), AUTH_REQUEST_CODE);
                }
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                myRef.child(KEY_EVENTS).child(""+marker.getTag()).child("likes").child("test@gmail.com");
                if(auth.getCurrentUser()!=null)
                {
                    String like_id = myRef.child(KEY_EVENTS).child("" + marker.getTag()).child("dislikes").push().getKey();
                    myRef.child(KEY_EVENTS).child("" + marker.getTag()).child("dislikes").child(like_id).setValue(auth.getCurrentUser().getEmail());
                    alertDialog.dismiss();
                }
                else
                {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), AUTH_REQUEST_CODE);
                }

            }
        });

        myRef.child(KEY_EVENTS).child("" + marker.getTag()).child("event_title").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();
                tv_title.setText(value);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        myRef.child(KEY_EVENTS).child("" + marker.getTag()).child("event_description").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();
                tv_description.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        myRef.child(KEY_EVENTS).child("" + marker.getTag()).child("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                like.setText("Like[" + dataSnapshot.getChildrenCount() + "]");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        myRef.child(KEY_EVENTS).child("" + marker.getTag()).child("dislikes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                count = (int) dataSnapshot.getChildrenCount();
                dislike.setText("Dislike[" + count + "]");
                if (count > 10) myRef.child(KEY_EVENTS).child("" + marker.getTag()).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    /**
     * Function to show settings alert dialog
     */
    public void checkPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        REQUEST_CHECK_SETTINGS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("permission granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void isGPSEnable() {

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }



    }
}
