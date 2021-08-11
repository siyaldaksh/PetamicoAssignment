package com.assignment.petamicoassignment.ui.addnewuser

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.assignment.petamicoassignment.ui.database.UserDao
import com.assignment.petamicoassignment.ui.userlist.UserListViewModel

class AddNewUserViewModelFactory(
) : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNewUserViewModel::class.java)) {
            return AddNewUserViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}