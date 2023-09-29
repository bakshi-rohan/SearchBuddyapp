package com.searchbuddy.searchbuddy.Login

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.searchbuddy.databinding.ActivityLoginWithOtpBinding
import com.searchbuddy.searchbuddy.Forms.Form_One
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager


class LoginWithOTP : AppCompatActivity() {
    private lateinit var binding: ActivityLoginWithOtpBinding
    lateinit var activityLoginViewModel: LoginWithOtpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginWithOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activityLoginViewModel = ViewModelProvider(this).get(LoginWithOtpViewModel::class.java)

        binding.verifyNumber.setOnClickListener {
           requestLogin(binding.progress)
        }
        activityLoginViewModel.errorMessage()?.observe(this, {
            var intent=Intent(this, Form_One::class.java)
            startActivity(intent)
            finish()
        })
    }
    private fun requestLogin(
        progress: ProgressBar
    ) {

        var mobileNo=object {
            var mobileNo=binding.etMail.text.toString()
        }

        activityLoginViewModel.requestLogin(this, mobileNo, progress).observe(this, {
            var intent=Intent(this,EnterOtp::class.java)
            startActivity(intent)
            LocalSessionManager.saveValue(Constant.OtpID,it.id,this)
        })

    }

}