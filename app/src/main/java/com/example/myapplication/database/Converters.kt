package com.example.myapplication.database

import androidx.room.TypeConverter
import java.util.*

/*
* room에서 데이터 저장/불러올때 자동으로 변환할 타입
* */
class Converters {
    @TypeConverter
    fun timestampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}