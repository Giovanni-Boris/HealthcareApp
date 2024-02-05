package com.example.healthcareapp.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarm")
public class Alarm {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int day;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
