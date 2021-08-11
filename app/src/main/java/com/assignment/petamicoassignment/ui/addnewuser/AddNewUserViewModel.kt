package com.assignment.petamicoassignment.ui.addnewuser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.assignment.petamicoassignment.apputils.MyCallback
import com.assignment.petamicoassignment.apputils.NewUserCallback
import com.assignment.petamicoassignment.ui.database.UserDao
import com.assignment.petamicoassignment.ui.network.ApiUtil
import com.assignment.petamicoassignment.ui.network.CreateUser
import com.assignment.petamicoassignment.ui.network.CreatedUserResponse
import com.assignment.petamicoassignment.ui.network.UserDataServer
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewUserViewModel (
)
    : ViewModel(){

    val _nameEditText = MutableLiveData<String>("")
    val _jobEditText = MutableLiveData<String>("")

    var _isDataGetSuccessfully = MutableLiveData<Boolean>()
    val isDataGetSuccessfully : LiveData<Boolean>
        get() = _isDataGetSuccessfully

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var newUser : CreatedUserResponse? =null




    fun createUser(){

        val name = _nameEditText.value?.trim().toString()
        val job = _jobEditText.value?.trim().toString()

        if (name.isNotEmpty()&&job.isNotEmpty()) {
            val apiService = ApiUtil.getRetroInstance()

            val user = CreateUser(name, job)
            val call = apiService.createUser(user)

            call.enqueue(object : Callback<CreatedUserResponse> {
                override fun onResponse(
                    call: Call<CreatedUserResponse>,
                    response: Response<CreatedUserResponse>
                ) {
                    _isDataGetSuccessfully.value = true
                    newUser = response.body()
    
                }

                override fun onFailure(call: Call<CreatedUserResponse>, t: Throwable) {
                    _isDataGetSuccessfully.value = false
                }

            })
        }
    }

}