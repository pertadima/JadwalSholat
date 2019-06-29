package com.jadwalshlat.www.jetpackcontroller

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = REPLACE)
    fun createUser(user: User)

    // -----------------------------------------------------------------------------------------------------------------
    @Query("SELECT * FROM user WHERE name = :name")
    fun readUser(name: String) : LiveData<User>

    // -----------------------------------------------------------------------------------------------------------------
    @Query("DELETE FROM user WHERE name = :name")
    fun deleteUser(name: String)

}