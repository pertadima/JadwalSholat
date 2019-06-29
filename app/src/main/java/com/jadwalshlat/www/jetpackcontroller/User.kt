package com.jadwalshlat.www.jetpackcontroller

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user")
data class User (
    @PrimaryKey var name: String,
    @ColumnInfo(name = "value") var value: String
)