package com.assignment.petamicoassignment.apputils

import com.assignment.petamicoassignment.ui.network.CreatedUserResponse

interface NewUserCallback {

    fun userAdded(user:CreatedUserResponse)
}