package com.assignment.petamicoassignment.ui.network

import kotlinx.android.parcel.Parcelize


data class CreatedUserResponse(
    val id : Int,
    val name : String,
    val job : String,
    val createdAt : String
)

data class CreateUser(val name: String,val job: String)