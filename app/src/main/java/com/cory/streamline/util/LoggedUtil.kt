package com.cory.streamline.util

import com.cory.streamline.login.data.model.LoggedInUser

private lateinit var user: LoggedInUser

fun initLoggedInUser(loggedInUser: LoggedInUser){
    user=loggedInUser
}

fun loggedInUser():LoggedInUser{
    return user
}