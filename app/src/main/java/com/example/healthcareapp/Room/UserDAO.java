package com.example.healthcareapp.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.healthcareapp.Entity.User;

import io.reactivex.Single;

@Dao
public interface UserDAO {
    @Insert
    void insert(User user);
    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    Single<User> login(String username, String password);
}