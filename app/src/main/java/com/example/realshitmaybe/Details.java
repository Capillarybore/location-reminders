package com.example.realshitmaybe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Details extends AppCompatActivity implements maybe{
    databasehelper myDb;
    Button buttondelete;
    Button buttonupdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        myDb = new databasehelper(this);
        buttondelete = (Button)findViewById(R.id.delete);

        TextView name = (TextView) findViewById(R.id.fname);
        TextView description = (TextView) findViewById(R.id.fdescription);
        TextView datetime = (TextView) findViewById(R.id.fdatetime);
        TextView location = (TextView) findViewById(R.id.flocation);

        String tname = getIntent().getStringExtra("selected");
        Task disp=new Task("666","Error","Error","Error","Error","");
        for(Task x:data){
            if(x.taskN .equals(tname)){
                disp.taskN = x.taskN;
                disp.taskD = x.taskD;
                disp.ID = x.ID;
                disp.dateTime = x.dateTime;
                disp.latitude = x.latitude;
                disp.longitude = x.longitude;
            }
        }

        name.setText(disp.taskN);
        description.setText(disp.taskD);
        datetime.setText(disp.dateTime);
        location.setText(disp.latitude+", "+disp.longitude);

        UpdateData(disp.ID);
        DeleteData(disp.ID);

    }

    public void DeleteData(final String id){
        buttondelete.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Integer deleteRows = myDb.DeleteData(id);
                        if(deleteRows>0)
                            Toast.makeText(getApplicationContext(),"Data Deleted",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(),"Data Not Deleted",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Details.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void UpdateData(final String id){
        buttonupdate = (Button) findViewById(R.id.update);
        buttonupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Details.this, UpdateData.class);
                intent.putExtra("idupdate",id);
                startActivity(intent);
            }
        });
    }

}
