package com.example.realshitmaybe;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class UpdateData extends AppCompatActivity implements View.OnClickListener, maybe{
    databasehelper myDb;
    EditText nameText, descriptionText, dayText, monthText, yearText, hourText, minuteText;
    Button buttonupdate;
    private int mYear,mMonth,mDay,mHour,mMinute;

    private static final int REQUEST_LOCATION = 1;
    Button locationButton;
    TextView textView;
    LocationManager locationManager;
    String latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        String id = getIntent().getStringExtra("idupdate");

        myDb = new databasehelper(this);
        nameText = (EditText)findViewById(R.id.nameText);
        descriptionText = (EditText)findViewById(R.id.descriptionText);
        dayText = (EditText)findViewById(R.id.dayText);
        monthText = (EditText)findViewById(R.id.monthText);
        yearText = (EditText)findViewById(R.id.yearText);
        hourText = (EditText)findViewById(R.id.hourText);
        minuteText = (EditText)findViewById(R.id.minuteText);
        dayText.setOnClickListener(this);
        monthText.setOnClickListener(this);
        yearText.setOnClickListener(this);
        hourText.setOnClickListener(this);
        minuteText.setOnClickListener(this);

        buttonupdate = (Button)findViewById(R.id.updateButton);


        for(Task x:data){
            if(id.equals(x.ID)){
                nameText.setText(x.taskN);
                descriptionText.setText(x.taskD);
            }
        }
        viewSome(id);
        Update(id);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        textView = (TextView) findViewById(R.id.text_location);
        locationButton = (Button)findViewById(R.id.button_location);
        locationButton.setOnClickListener(this);

    }

    public void Update(final String id) {
        buttonupdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdate = myDb.UpdateData(
                                id,
                                nameText.getText().toString(),
                                descriptionText.getText().toString(),
                                dayText.getText().toString(),
                                monthText.getText().toString(),
                                yearText.getText().toString(),
                                hourText.getText().toString(),
                                minuteText.getText().toString(),
                                latitude,
                                longitude
                        );
                        if (isUpdate == true)
                            Toast.makeText(UpdateData.this, "Data Updated", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(UpdateData.this, "Data not Updated", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UpdateData.this, MainActivity.class));
                    }
                }
        );
    }



    public void onClick(View view) {
        if(view==locationButton){
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocation();
            }
        }else if(view == dayText || view == monthText || view == yearText){
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            dayText.setText(String.valueOf(dayOfMonth));
                            monthText.setText(String.valueOf(monthOfYear+1));
                            yearText.setText(String.valueOf(year));
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        }else if(view == hourText || view == minuteText){
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            hourText.setText(String.valueOf(hourOfDay));
                            minuteText.setText(String.valueOf(minute));
                        }
                    }, mHour, mMinute,true);
            timePickerDialog.show();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(UpdateData.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (UpdateData.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(UpdateData.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double lati = location.getLatitude();
                double longi = location.getLongitude();
                latitude = String.valueOf(lati);
                longitude = String.valueOf(longi);
                locationButton.setVisibility(View.GONE);
                textView.setText("Latitude = " + latitude + "\n" + "Longitude = " + longitude);

            } else  if (location1 != null) {
                double lati = location1.getLatitude();
                double longi = location1.getLongitude();
                latitude = String.valueOf(lati);
                longitude = String.valueOf(longi);
                locationButton.setVisibility(View.GONE);
                textView.setText("Latitude = " + latitude + "\n" + "Longitude = " + longitude);


            } else  if (location2 != null) {
                double lati = location2.getLatitude();
                double longi = location2.getLongitude();
                latitude = String.valueOf(lati);
                longitude = String.valueOf(longi);
                locationButton.setVisibility(View.GONE);
                textView.setText("Latitude = " + latitude + "\n" + "Longitude = " + longitude);

            }else{

                Toast.makeText(this,"Unable to Trace your location",Toast.LENGTH_SHORT).show();

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

    public void viewSome(String id) {
        Cursor res = myDb.getSomeData(id);
        while (res.moveToNext()) {
            dayText.setText(res.getString(3));
            monthText.setText(res.getString(4));
            yearText.setText(res.getString(5));
            hourText.setText(res.getString(6));
            minuteText.setText(res.getString(7));
        }
    }

}