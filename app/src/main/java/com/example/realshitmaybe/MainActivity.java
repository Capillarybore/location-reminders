package com.example.realshitmaybe;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.SimpleTimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnNoteListener , maybe, View.OnClickListener {

    private static final String TAG = "MainActivity";

    databasehelper myDb;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;
    CoordinatorLayout coordinatorLayout;

    //vars
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mTime = new ArrayList<>();
    private ArrayList<String> mLocation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new databasehelper(this);
        FloatingActionButton button;
        coordinatorLayout = findViewById(R.id.coordinator_Layout);

        mName.clear();
        mTime.clear();
        mLocation.clear();
        data.clear();
        viewAll();
        initRecyclerView();

        FloatingActionButton fabadd = (FloatingActionButton) findViewById(R.id.fabadd);
        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InputData.class));
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("Group Members:", "\nSamvit Singh Jain - 16BCE0832\n\nRajat Agarwal - 16BCE0604\n\nParva Jain - 16BCE0093");
            }
        });

        button = findViewById(R.id.fab1);
        button.setOnClickListener(this);
    }

    private void initRecyclerView() {
        //Adapter
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,data,this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Divider
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        itemDecoration.setDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.divider_white));
        recyclerView.addItemDecoration(itemDecoration);

        //Swipe to delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, Details.class);
        intent.putExtra("selected", data.get(position).taskN);
        startActivity(intent);
    }

    public void viewAll() {
        Cursor res = myDb.getAllData();
        while (res.moveToNext()) {
            String ID = res.getString(0);
            String taskN = res.getString(1);
            String taskD = res.getString(2);
            String date = res.getString(3);
            String month = res.getString(4);
            String year = res.getString(5);
            String h = res.getString(6);
            String m = res.getString(7);
            String lati = res.getString(8);
            String longi = res.getString(9);
            Task t1 = new Task(ID, taskN, taskD, date + "/" + month + "/" + year + ", " + h + ":" + m, lati, longi);
            data.add(t1);
        }
    }

    public void onClick(View view) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double lati = location.getLatitude();
                double longi = location.getLongitude();
                latitude = String.valueOf(lati);
                longitude = String.valueOf(longi);

            } else if (location1 != null) {
                double lati = location1.getLatitude();
                double longi = location1.getLongitude();
                latitude = String.valueOf(lati);
                longitude = String.valueOf(longi);

            } else if (location2 != null) {
                double lati = location2.getLatitude();
                double longi = location2.getLongitude();
                latitude = String.valueOf(lati);
                longitude = String.valueOf(longi);

            } else {

                Toast.makeText(this, "Unable to Trace your location", Toast.LENGTH_SHORT).show();

            }
            Calendar calendar = Calendar.getInstance();
            int Cd = calendar.get(Calendar.DAY_OF_MONTH);
            int Cm = calendar.get(Calendar.MONTH);
            Cm++;
            int Th = calendar.get(Calendar.HOUR_OF_DAY);
            int Tm = calendar.get(Calendar.MINUTE);
            int T = (Th * 60) + Tm;
            float latr = Float.parseFloat(latitude);
            float lonr = Float.parseFloat(longitude);
            DecimalFormat df = new DecimalFormat("#.####");
            float latrr = Float.parseFloat(df.format(latr));
            float lonrr = Float.parseFloat(df.format(lonr));
            for (Task x : data) {
                float lats = Float.parseFloat(x.latitude);
                float lons = Float.parseFloat(x.longitude);
                String dat[] = x.dateTime.split("/");
                String tim[] = dat[2].split(" ");
                String tim1[] = tim[1].split(":");
                int th = Integer.parseInt(tim1[0]);
                int tm = Integer.parseInt(tim1[1]);
                int t = (th * 60) + tm;
                if (lats == latrr && lons == lonrr && ("" + Cd).equals(dat[0]) && ("" + Cm).equals(dat[1]) && t <= T) {
                    Toast.makeText(this, "Detected.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Details.class);
                    intent.putExtra("selected", x.taskN);
                    startActivity(intent);
                }
            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message)
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }
}