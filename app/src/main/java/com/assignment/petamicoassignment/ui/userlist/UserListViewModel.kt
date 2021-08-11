package com.assignment.petamicoassignment.ui.userlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.assignment.petamicoassignment.ui.database.UserDao
import com.assignment.petamicoassignment.ui.network.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListViewModel(private val dataSource: UserDao,
application: Application
)
: AndroidViewModel(application) {

    private var job = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    var _isDataGetSuccessfully = MutableLiveData<Boolean>()
    val isDataGetSuccessfully : LiveData<Boolean>
        get() = _isDataGetSuccessfully

    var _userDeletedSuccessfully = MutableLiveData<Boolean>()
    val userDeletedSuccessfully : LiveData<Boolean>
        get() = _userDeletedSuccessfully

    var _isButtonClicked = MutableLiveData<Boolean>()
    val isButtonClicked : LiveData<Boolean>
        get() = _isButtonClicked

    var arrayList : List<UserDataServer> = emptyList()

    var service : Api = ApiUtil.getRetroInstance()


    fun getSearchResultStream(): Flow<PagingData<UserDataServer>> {

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory =
            { UserPagingSource(service) }
        ).flow.cachedIn(viewModelScope)
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 2
    }

    fun getLocalData(){
        uiScope.launch {
            withContext(Dispatchers.IO){
                arrayList =  dataSource.getAllUserData()

            }

            _isDataGetSuccessfully.value = true
        }
    }

    fun addUserFragment(){
        _isButtonClicked.value = true
    }

    suspend fun fillDataToDatabase(arrayList : List<UserDataServer>){
        uiScope.launch {
            arrayList.let {
                for(i:Int in 0 until it.size){
                    val userData = UserDataServer(arrayList[i].id,arrayList[i].email
                        ,arrayList[i].first_name,arrayList[i].last_name,arrayList[i].avatar)
                    insert(userData)
                }
            }
        }

    }

    private suspend fun insert(userDataServer: UserDataServer){
        withContext(Dispatchers.IO){
            dataSource.insertUser(userDataServer)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun deleteUser(id: Int) {
        val apiService = ApiUtil.getRetroInstance()
        val call = apiService.deleteUser(id)

        call.enqueue(object : Callback<Int> {
            override fun onResponse(
                call: Call<Int>,
                response: Response<Int>
            )
            {
                _userDeletedSuccessfully.value = true
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                _userDeletedSuccessfully.value = false
            }

        })

    }
}