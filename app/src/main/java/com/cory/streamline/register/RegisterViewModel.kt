package com.cory.streamline.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cory.streamline.R

class RegisterViewModel : ViewModel() {

    val registerFromState:LiveData<RegisterFromState>
        get() {
            return _registerFromState
        }
    private val _registerFromState= MutableLiveData<RegisterFromState>()

    fun registerDataChanged(username: String,password: String,repeat: String){
       if (!isUserNameValid(username)){
            _registerFromState.value= RegisterFromState(usernameError = R.string.invalid_username)
        }else if (!isPasswordValid(password)){
            _registerFromState.value= RegisterFromState(passwordError = R.string.invalid_password)
        }else if (!isPasswordMatched(password,repeat)){
            _registerFromState.value= RegisterFromState(passwordMatchError = R.string.mismatched_password)
        }else{
            _registerFromState.value= RegisterFromState(isDataValid = true)
        }
    }


    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()&&username.length<26

    }


    private fun isPasswordValid(password: String): Boolean {
        return password.length in 6..15
    }



    private fun isPasswordMatched(password: String,repeat:String):Boolean{
        return password==repeat
    }


}