package com.searchbuddy.searchbuddy.Login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.auth0.android.jwt.JWT
import com.bumptech.searchbuddy.databinding.ActivityLoginBinding
import com.searchbuddy.searchbuddy.Dashboard.Dashboard
import com.searchbuddy.searchbuddy.Forms.Form_One
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.Utils.Utils
import com.searchbuddy.searchbuddy.model.LoginRequest
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var activityLoginViewModel: LoginViewModel
    lateinit var mToken: String
    lateinit var currentDate: String
    lateinit var date: SimpleDateFormat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalSessionManager.removeValue(Constant.SliderStartValue, this)
        LocalSessionManager.removeValue(Constant.SliderEndValue, this)
        LocalSessionManager.removeValue(Constant.SalarySliderStartValue, this)
        LocalSessionManager.removeValue(Constant.SalarySliderEndValue, this)
        LocalSessionManager.removeValue(Constant.FilterLocation, this)
        LocalSessionManager.removeValue(Constant.DatePosted, this)
        LocalSessionManager.removeValue(Constant.DatePost, this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
//        binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT )
        setContentView(binding.root)
        activityLoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }
        binding.btnLoginOtp.setOnClickListener {
            val intent = Intent(this, LoginWithOTP::class.java)
            startActivity(intent)
        }
        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, Form_One::class.java)
            startActivity(intent)
        }

        date = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentDate = date.format(Date())
        Log.i("rohan", currentDate)
binding.browseJobs.setOnClickListener {
    var intent=Intent(this,BrowseJobs::class.java)
    startActivity(intent)
}
        binding.btnLogin.setOnClickListener {
//           doLogin()
//            val intent = Intent(this, Dashboard::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//            finish()
            var etMail = binding.etName
            if (isValidate()) {
                if (Utils.isNetworkAvailable(this)) {
                    val password = binding.etPassword!!.text.toString()
                    var userName = binding.etName!!.text.toString()
//                    emailValidator(etMail)
                    requestLogin(userName, password, this, binding.progress)
                } else {
                    Utils.notNetwork(this)
                }

            }
        }

        binding.etName.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    //Do something when EditText has focus
                    binding.etName.setError(null)
                } else {
                    // Do something when Focus is not on the EditText
                    validateUserName()

                }
            }
        })
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.tiUsername.setError(null)

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.tiPassword.setError(null)
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })


        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(com.bumptech.searchbuddy.R.color.grey)
        }

        activityLoginViewModel.errorMessage()?.observe(this, {
            showToast(it.toString())
        })
    }


    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

//    override fun onUserInteraction() {
//        super.onUserInteraction()
//        if (currentFocus != null) {
//            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
//        }
//    }

    private fun requestLogin(
        userName: String,
        password: String,
        loginActivity: Login,
        progress: ProgressBar
    ) {

        var loginParams = LoginRequest(password, userName)
        activityLoginViewModel.requestLogin(this, loginParams, progress).observe(this) {
            mToken = it.jwttoken
            Log.i("tokennnnn", it.jwttoken)
            LocalSessionManager.saveValue(Constant.IS_LOGED_IN, true, this)
            LocalSessionManager.saveValue(Constant.TOKEN, it.jwttoken, this)
            LocalSessionManager.saveValue(Constant.USER_EMAIL, it.userDTO.email, this)
            if (it.userDTO.profilePicName != null) {
                LocalSessionManager.saveValue(
                    Constant.ProfilePicName,
                    it.userDTO.profilePicName,
                    this
                )
            }
            LocalSessionManager.saveValue(Constant.USER_NAME, it.userDTO.name, this)
//            var m= LocalSessionManager.getStringValue(Constant.USER_NAME,"",this)
//            Log.i("xxxx",m.toString())

            val token =
                it.jwttoken
            val jwt = JWT(token)

            val issuer = jwt.issuer //get registered claims

            val claim = jwt.getClaim("USER").asString() //get custom claims
            Log.i("nnnnnnnnn", claim.toString())

            var m= claim
            Log.i("xxxx",m.toString())
            LocalSessionManager.saveValue(Constant.UserID,m.toString(),this)

            val intent = Intent(this, Dashboard::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()


//            val isExpired = jwt.isExpired(10) // Do time validation with 10 seconds leeway

        }

    }


    private fun isValidate(): Boolean =
        validateUserName() && validatePassword()


    fun validateUserName(): Boolean {
        if (binding.etName.text.toString().trim().isEmpty()) {
            binding.tiUsername.error = "Enter Username!"
            return false
        } else if (emailValidator(binding.etName) == false) {
            return false
        } else {
            binding.tiUsername.isErrorEnabled = false
        }
        return true
    }

    fun validatePassword(): Boolean {
        if (binding.etPassword.text.toString().trim().isEmpty()) {
            binding.tiPassword.error = "Enter Password !"
            binding.etPassword.requestFocus()
            return false
        } else if (binding.etPassword.text.toString().length < 5) {
            binding.tiPassword.error = "Password can't be less than 5 characters"
            binding.etPassword.requestFocus()
            return false
        } else {
            binding.tiPassword.isErrorEnabled = false

        }
        return true
    }

    private fun decodeToken(jwt: String): String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return "Requires SDK 26"
        val parts = jwt.split(".")
        return try {
            val charset = charset("UTF-8")
            val header =
                String(Base64.getUrlDecoder().decode(parts[0].toByteArray(charset)), charset)
            val payload =
                String(Base64.getUrlDecoder().decode(parts[1].toByteArray(charset)), charset)
            "$header"
            "$payload"
        } catch (e: Exception) {
            "Error parsing JWT: $e"
        }
    }

    fun emailValidator(etMail: EditText): Boolean {

        // extract the entered data from the EditText
        val emailToText = etMail.text.toString()

        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {

        } else {
            binding.tiUsername.error = "Enter a Valid Email Address!"
            return false
        }
        return true
    }

    /** SALVANDO ID_OPERADOR */

}


