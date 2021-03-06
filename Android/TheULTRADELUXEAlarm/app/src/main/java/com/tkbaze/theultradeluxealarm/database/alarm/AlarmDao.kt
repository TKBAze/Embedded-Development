package com.tkbaze.theultradeluxealarm.database.alarm

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alarm: Alarm): Long

    @Delete
    suspend fun delete(alarm: Alarm)

    @Update
    suspend fun update(alarm: Alarm)

    @Query("SELECT COUNT(*) FROM alarms WHERE id=:id")
    suspend fun exist(id: Int): Int

    @Query("SELECT * FROM alarms ORDER BY alarm_hour, alarm_minute")
    fun getAll(): Flow<List<Alarm>>

    @Query("SELECT * FROM alarms ORDER BY alarm_hour, alarm_minute")
    fun getList(): List<Alarm>

    @Query("SELECT * FROM alarms WHERE id=:id")
    suspend fun getAlarm(id: Long): Alarm
/*
    @Query("UPDATE alarms SET alarm_set=:set WHERE id = :id")
    suspend fun updateSet(id: Int, set: Boolean)

    @Query("UPDATE alarms SET alarm_recurring=:recurring WHERE id =:id")
    suspend fun updateRecurring(id: Int, recurring: Boolean)

    @Query("UPDATE alarms SET alarm_hour=:hour, alarm_minute=:minute WHERE id =:id")
    suspend fun updateTime(id: Int, hour: Int, minute: Int)
 */
}