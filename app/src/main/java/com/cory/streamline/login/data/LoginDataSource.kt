package com.cory.streamline.login.data

import android.content.Context
import android.util.Log
import com.cory.streamline.login.data.model.LoggedInUser
import com.cory.streamline.util.initLoggedInUser
import com.cory.streamline.util.loginContext
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(): Result<LoggedInUser> {
        val prefs= loginContext().getSharedPreferences("login_info",Context.MODE_PRIVATE)
        val userId=prefs.getString("id","null")
        val name=prefs.getString("name","null")
        val token=prefs.getString("token","null")
        val u: LoggedInUser
        if (userId!=null&&name!=null&&token!=null){
            u=LoggedInUser(userId,name,token)
            initLoggedInUser(u)
            return Result.Success(u)
        }

        return Result.Error(IOException("Error logging in", Throwable()))
    }



    fun logout() {
        // TODO: revoke authentication
        val editor= loginContext().getSharedPreferences("login_info",Context.MODE_PRIVATE)
            .edit()
        editor.putString("token","null")
        editor.putString("name","null")
        editor.putString("id","null")
        editor.apply()
    }


}