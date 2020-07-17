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
        asyncValidate(username,password,context)
        val prefs=context.getSharedPreferences("login_info",Context.MODE_PRIVATE)
        val userId=prefs.getString("id",null)
        val name=prefs.getString("name",null)
        val token=prefs.getString("token",null)
        val u: LoggedInUser
        if (userId!=null&&name!=null&&token!=null){
            u=LoggedInUser(userId,name,token)
            return Result.Success(u)
        }

        return Result.Error(IOException("Error logging in", Throwable()))
        }



    fun logout(context: Context) {
        // TODO: revoke authentication
        val editor=context.getSharedPreferences("login_info",Context.MODE_PRIVATE)
            .edit()
        editor.putString("token",null)
        editor.putString("name",null)
        editor.putString("id",null)
        editor.apply()
    }

    private fun asyncValidate(username: String, password: String, context: Context){
        val okhttpClient=OkHttpClient()
        val requestBody=FormBody.Builder().add("username",username)
            .add("password",password).build()
        val request=Request.Builder().url("https://run.mocky.io/v3/09c08bb9-1174-45b2-bab6-3f025ef7803d")
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

                    val editor=context.getSharedPreferences("login_info",Context.MODE_PRIVATE)
                        .edit()
                    editor.putString("token",logged.token)
                    editor.putString("name",logged.displayName)
                    editor.putString("id",logged.userId)
                    editor.apply()


                }else{
                    val editor=context.getSharedPreferences("login_info",Context.MODE_PRIVATE)
                        .edit()
                    editor.putString("token",null)
                    editor.putString("name",null)
                    editor.putString("id",null)
                    editor.apply()
                }
            }
        })

    }
}