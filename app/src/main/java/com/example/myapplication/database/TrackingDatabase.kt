package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.model.LocationLog
import com.example.myapplication.model.TrackingLog
import com.example.myapplication.others.Constants.DB_NAME

@Database(entities = [TrackingLog::class, LocationLog::class], version = 1)
abstract class TrackingDatabase : RoomDatabase() {

    abstract fun trackingDao(): TrackingDao

    companion object {
        fun getInstance(getApplicationContext: Context): TrackingDatabase {
            return Room.databaseBuilder(
                getApplicationContext,
                TrackingDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}

//https://medium.com/harrythegreat/%EB%B2%88%EC%97%AD-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-room-7%EA%B0%80%EC%A7%80-%EC%9C%A0%EC%9A%A9%ED%95%9C-%ED%8C%81-18252a941e27