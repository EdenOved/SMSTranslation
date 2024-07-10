package com.kalman_aovid_arges.smstranslation;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SMSGroupDao {
    @Query("SELECT * FROM SMSGroup")
    List<SMSGroup> getAll();

    @Insert
    void insertAll(SMSGroup... smsGroups);

    @Update
    void update(SMSGroup smsGroup);
    
}