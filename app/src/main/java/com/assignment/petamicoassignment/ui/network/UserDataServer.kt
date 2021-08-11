package com.assignment.petamicoassignment.ui.network

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_data")
data class UserDataServer(
    @PrimaryKey @field:SerializedName("id") val id : Int,
    @field:SerializedName("email") val email : String,
    @field:SerializedName("first_name") val first_name : String,
    @field:SerializedName("last_name") val last_name : String,
    @field:SerializedName("avatar") val avatar : String)