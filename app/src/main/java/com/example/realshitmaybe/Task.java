
package com.example.realshitmaybe;

import java.util.ArrayList;
import java.util.List;

interface maybe{
    List<Task> data = new ArrayList<>();
}
public class Task {
    String ID;
    String taskN;
    String taskD;
    String dateTime;
    String latitude;
    String longitude;

    Task(String ID,String taskN,String taskD,String dateTime,String latitude, String longitude){
        this.ID=ID;
        this.taskN=taskN;
        this.taskD=taskD;
        this.dateTime=dateTime;
        this.latitude=latitude;
        this.longitude=longitude;
    }

}