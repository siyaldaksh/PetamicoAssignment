package com.assignment.petamicoassignment.ui.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.assignment.petamicoassignment.ui.network.UserDataServer


@Dao
interface UserDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserDataServer)

    @Query("DELETE FROM user_data WHERE id = :id")
    fun deleteContact(id:Int)

    @Query("SELECT * FROM user_data")
    fun getAllUserData(): List<UserDataServer>

    @Query("DELETE FROM user_data")
    suspend fun clear()
}