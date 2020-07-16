package com.cory.streamline.login.data

import android.content.Context
import android.util.Log
import com.cory.streamline.login.data.model.LoggedInUser
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private val TAG="myTag"
    fun login(username: String, password: String,context: Context): Result<LoggedInUser> {
        return try {
            asyncValidate(username,password,context)
            val prefs=context.getSharedPreferences("login_info",Context.MODE_PRIVATE)
            val userId=prefs.getString("id","id not found")
            val name=prefs.getString("name","name not found")
            val token=prefs.getString("token","token not found")
            var u=LoggedInUser("null","null","null")
            if (userId!=null&&name!=null&&token!=null){
                u=LoggedInUser(userId,name,token)
            }
            Result.Success(u)

        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }

    private fun asyncValidate(username: String, password: String, context: Context){
            val okhttpClient=OkHttpClient()
            val requestBody=FormBody.Builder().add("username",username)
                .add("password",password).build()
            val request=Request.Builder().url("https://run.mocky.io/v3/f19c6a2d-bd2e-40bb-bb35-47f677cb7204")
                .post(requestBody).build()
            okhttpClient.newCall(request).enqueue(object :Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, "onFailure: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.toString().contains("200")){
                        val resBody= response.body?.string()

                        val gson=Gson()
                        val logged=gson.fromJson(resBody,LoggedInUser::class.java)
                        Log.d(TAG,  logged.toString())
                        val editor=context.getSharedPreferences("login_info",Context.MODE_PRIVATE)
                            .edit()
                        editor.putString("token",logged.token)
                        editor.putString("name",logged.displayName)
                        editor.putString("id",logged.userId)
                        editor.apply()
                    }
                }
            })

    }
}