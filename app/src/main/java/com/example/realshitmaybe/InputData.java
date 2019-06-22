package com.example.realshitmaybe;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class InputData extends AppCompatActivity implements View.OnClickListener {
    databasehelper myDb;
    TextInputEditText nameText, descriptionText, dayText, monthText, yearText, hourText, minuteText;
    Button buttonadd;
    private int mYear,mMonth,mDay,mHour,mMinute;

    private static final int REQUEST_LOCATION = 1;
    MaterialButton locationButton;
    TextView textView;
    LocationManager locationManager;
    String latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        myDb = new databasehelper(this);
        nameText = findViewById(R.id.nameText);
        descriptionText = findViewById(R.id.descriptionText);
        dayText = findViewById(R.id.dayText);
        monthText = findViewById(R.id.monthText);
        yearText = findViewById(R.id.yearText);
        hourText = findViewById(R.id.hourText);
        minuteText = findViewById(R.id.minuteText);
        dayText.setOnClickListener(this);
        monthText.setOnClickListener(this);
        yearText.setOnClickListener(this);
        hourText.setOnClickListener(this);
        minuteText.setOnClickListener(this);

        buttonadd = (Button)findViewById(R.id.addButton);
        AddData();

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        textView = (TextView) findViewById(R.id.text_location);
        locationButton = findViewById(R.id.button_location);
        locationButton.setOnClickListener(this);
    }

    public void AddData() {
        buttonadd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View rootView = findViewById(android.R.id.content);
                        final List<TextInputLayout> textInputLayouts = Utils.findViewsWithType(rootView, TextInputLayout.class);
                        boolean noErrors = true;
                        for (TextInputLayout textInputLayout : textInputLayouts) {
                            String editTextString = textInputLayout.getEditText().getText().toString();
                            if (editTextString.isEmpty()) {
                                textInputLayout.setError("Field must not be empty.");
                                noErrors = false;
                            } else {
                                textInputLayout.setError(null);
                            }
                        }

                        if (noErrors) {
                            boolean isInserted = myDb.insertData(nameText.getText().toString(),
                                    descriptionText.getText().toString(),
                                    dayText.getText().toString(),
                                    monthText.getText().toString(),
                                    yearText.getText().toString(),
                                    hourText.getText().toString(),
                                    minuteText.getText().toString(),
                                    latitude,
                                    longitude
                            );
                            if (isInserted == true)
                                Toast.makeText(InputData.this, "Data Inserted", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(InputData.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(InputData.this, MainActivity.class));
                        }
                    }
                }
        );
    }


    public void onClick(View view){

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
        if (ActivityCompat.checkSelfPermission(InputData.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (InputData.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(InputData.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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

}