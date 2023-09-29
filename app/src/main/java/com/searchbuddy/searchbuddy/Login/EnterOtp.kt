package com.searchbuddy.searchbuddy.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.auth0.android.jwt.JWT
import com.bumptech.searchbuddy.databinding.ActivityEnterOtpBinding
import com.searchbuddy.searchbuddy.Dashboard.Dashboard
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager


class EnterOtp : AppCompatActivity() {
    private lateinit var binding: ActivityEnterOtpBinding
    lateinit var activityLoginViewModel: LoginWithOtpViewModel
    lateinit var ids:String
    lateinit var mobile:String
    lateinit var mToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ids= LocalSessionManager.getStringValue("OtpID","",this)!!
        mobile= LocalSessionManager.getStringValue("OtpMobile","",this)!!
        activityLoginViewModel = ViewModelProvider(this).get(LoginWithOtpViewModel::class.java)
binding.verifyOtp.setOnClickListener {
    verifyOtp(binding.progress)
}
//        binding.resend.setTextColor(Color.parseColor("#bab8b8"))


//        object : CountDownTimer(30000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                // Used for formatting digit to be in 2 digits only
//                val f: NumberFormat = DecimalFormat("00")
//                val hour = millisUntilFinished / 3600000 % 24
//                val min = millisUntilFinished / 60000 % 60
//                val sec = millisUntilFinished / 1000 % 60
//                binding.timer.setText(
//                   "("   + f.format(
//                        sec
//                    ) +")")
//
//            }
//
//            // When the task is over it will print 00:00:00 there
//            override fun onFinish() {
//                binding.timer.setText("("+"00"+")")
//                binding.timer.isClickable=true
//                binding.resend.setTextColor(Color.parseColor("#936030"))
//                binding.resend.setOnClickListener {
//                    requestLogin(binding.progress)
//                }
//            }
//        }.start()
    }
    private fun verifyOtp(
        progress: ProgressBar
    ) {

        var mobileNo=object {
            var id=ids
            var otp=binding.pinView.text.toString()
        }

        activityLoginViewModel.verifyOtp(this, mobileNo, progress).observe(this, {
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

            val intent = Intent(this, Dashboard::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            val token =
                it.jwttoken
            val jwt = JWT(token)

            val issuer = jwt.issuer //get registered claims

            val claim = jwt.getClaim("USER").asString() //get custom claims
            Log.i("nnnnnnnnn", claim.toString())
            LocalSessionManager.saveValue(Constant.UserID, claim.toString(), this)
        })

    }
    private fun requestLogin(
        progress: ProgressBar
    ) {

        var mobileNo=object {
            var mobileNo=mobile
        }

        activityLoginViewModel.requestLogin(this, mobileNo, progress).observe(this, {

            Toast.makeText(this,"OTP resent successfully",Toast.LENGTH_SHORT).show()
        })

    }
}