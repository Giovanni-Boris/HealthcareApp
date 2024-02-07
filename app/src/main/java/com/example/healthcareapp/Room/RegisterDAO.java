package com.example.healthcareapp.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.healthcareapp.Entity.Register;

import java.util.List;

@Dao
public interface RegisterDAO {
    @Insert
    void insertRegister(Register register);

    @Query("SELECT * FROM register")
    List<Register> readAllData();

    @Query("SELECT * FROM register WHERE id = :itemId")
    Register getItemById(long itemId);

    @Insert
    void insertAll(List<Register> registers);

    @Query("SELECT glucemia FROM register")
    List<Double> readGlusemiaAllData();

}