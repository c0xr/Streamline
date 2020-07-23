package com.cory.streamline.login.ui_login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cory.streamline.R
import com.cory.streamline.login.data.model.LoggedInUser
import com.cory.streamline.register.RegisterActivity
import com.cory.streamline.util.initLoginContext
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private val TAG="myTag"
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initLoginContext(applicationContext)
        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val usernameEditText = findViewById<EditText>(R.id.username)

        val passwordEditText = findViewById<EditText>(R.id.password)

        val loginButton = findViewById<Button>(R.id.login)


        val loadingProgressBar = findViewById<ProgressBar>(R.id.loading)

        val register:TextView=findViewById(R.id.register)



        loginViewModel.loginFormState.observe(this,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(this,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
                asyncValidate(usernameEditText.text.toString(),
                    passwordEditText.text.toString())
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                    loginViewModel.login()
            }
            false
        }

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
                loginViewModel.login()
            val x=loginButton.width/2
            val y=loginButton.height/2
            val r: Float=loginButton.width.toFloat()
            val animator= ViewAnimationUtils.createCircularReveal(loginButton,x,y,0.0F,r)
            animator.setDuration(350).start()
        }

        supportActionBar?.title = "登录"

        register.setOnClickListener {
            val intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }



    }
    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(this, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(this, errorString, Toast.LENGTH_LONG).show()
    }

    private fun asyncValidate(username: String, password: String){
        val okhttpClient= OkHttpClient()
        val requestBody= FormBody.Builder().add("username",username)
            .add("password",password).build()
        val request= Request.Builder().url("https://run.mocky.io/v3/09c08bb9-1174-45b2-bab6-3f025ef7803d")
            .post(requestBody).build()
        okhttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.toString().contains("200")){
                    val resBody= response.body?.string()

                    val gson= Gson()
                    val logged=gson.fromJson(resBody, LoggedInUser::class.java)

                    val editor=getSharedPreferences("login_info", Context.MODE_PRIVATE)
                        .edit()
                    editor.putString("token",logged.token)
                    editor.putString("name",logged.displayName)
                    editor.putString("id",logged.userId)
                    editor.apply()


                }else{
                    val editor=getSharedPreferences("login_info", Context.MODE_PRIVATE)
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