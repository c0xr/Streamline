package com.cory.streamline.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cory.streamline.R
import com.cory.streamline.register.model.User
import com.cory.streamline.util.log
import com.cory.streamline.util.toast
import com.github.ybq.android.spinkit.style.CubeGrid
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class RegisterActivity : AppCompatActivity() {
    private val TAG="registerTag"
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var user:User
    private lateinit var registerProgress:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val username:TextView=findViewById(R.id.username_r)
        val password:TextView=findViewById(R.id.password_r)
        val repeat:TextView=findViewById(R.id.confirm_password)
        val registerButton:Button=findViewById(R.id.register_button)
        registerProgress=findViewById(R.id.register_progress)
        registerProgress.indeterminateDrawable=CubeGrid()
        registerViewModel=ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())
            .get(RegisterViewModel::class.java)

        val afterTextChangedListener=object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                registerViewModel.registerDataChanged(
                    username.text.toString(),
                    password.text.toString(),
                    repeat.text.toString()
                )
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        }
        username.addTextChangedListener(afterTextChangedListener)
        password.addTextChangedListener(afterTextChangedListener)
        repeat.addTextChangedListener(afterTextChangedListener)
        registerViewModel.registerFromState.observe(this, Observer {
            registerFormState->
            if (registerFormState==null){
                return@Observer
            }
            registerButton.isEnabled=registerFormState.isDataValid

            registerFormState.usernameError?.let {
                username.error=getString(it)
            }
            registerFormState.passwordError?.let {
                password.error=getString(it)
            }
            registerFormState.passwordMatchError?.let {
                repeat.error=getString(it)
            }


        })

        registerButton.setOnClickListener {
            user= User(username.text.toString(),password.text.toString())
            registerProgress.visibility=View.VISIBLE
            register(user)
            val x=registerButton.width/2
            val y=registerButton.height/2
            val r: Float=registerButton.width.toFloat()
            val animator= ViewAnimationUtils.createCircularReveal(registerButton,x,y,0.0F,r)
            animator.setDuration(350).start()
        }



    }

    private fun register(user: User){
        val okHttpClient=OkHttpClient()
        val requestBody=FormBody.Builder()
            .add("username",user.username).add("password",user.password).build()
        val request=Request.Builder().url("http://cory0511.xyz:8080/Streamline/RegisterServlet").post(requestBody).build()
        okHttpClient.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.toString().contains("200")){
                    val resBody= response.body?.string()
                    var registerState="error"
                    log(resBody)
                    val jsonObject=JSONObject(resBody)
                    if(resBody!=null){
                        registerState=jsonObject.getString("state")
                    }

                    runOnUiThread {
                        when(registerState){
                            "success"-> toast("注册成功！请登录")
                            "duplicate"-> toast("该邮箱已被注册！")
                            "error"-> toast("请检查网络连接")
                        }
                        registerProgress.visibility=View.GONE
                    }


                }else{
                    runOnUiThread{
                        toast("服务器错误")
                        registerProgress.visibility=View.GONE
                    }

                }
            }


        })

    }
}