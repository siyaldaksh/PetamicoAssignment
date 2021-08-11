package com.assignment.petamicoassignment.ui.network

class ApiUtil {

    companion object{
        private val BASE_URL = "https://reqres.in/"

        fun getRetroInstance(): Api {
            return RetrofitClient.getClient(BASE_URL).create(Api::class.java)
        }
    }


}