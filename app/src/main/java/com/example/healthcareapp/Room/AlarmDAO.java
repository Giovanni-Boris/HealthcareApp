package com.example.healthcareapp.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.healthcareapp.Entity.Alarm;

import java.util.List;

@Dao
public interface AlarmDAO {
    @Insert
    void insertAlarm(Alarm alarm);

    @Query("SELECT * FROM alarm")
    List<Alarm> readAllDAta();

    @Query("DELETE FROM alarm WHERE id=:id")
    void deleteAlarmById(Long id);
}
