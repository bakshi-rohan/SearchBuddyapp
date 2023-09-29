package com.searchbuddy.searchbuddy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.searchbuddy.databinding.ActivityFirstScreenBinding
import com.searchbuddy.searchbuddy.Forms.Form_One
import com.searchbuddy.searchbuddy.Login.BrowseJobs
import com.searchbuddy.searchbuddy.Login.EnterOtp
import com.searchbuddy.searchbuddy.Login.Login
import com.searchbuddy.searchbuddy.Login.LoginWithOtpViewModel
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager


class FirstScreen : AppCompatActivity() {
    private lateinit var binding: ActivityFirstScreenBinding
    lateinit var activityLoginViewModel: LoginWithOtpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activityLoginViewModel = ViewModelProvider(this).get(LoginWithOtpViewModel::class.java)

        binding.verifyNumber.setOnClickListener {
            if (isValidate()) {
                requestLogin(binding.progress)
            }
        }

var text= "By clicking Continue, you agree to SearchBuddy Terms of Service and Privacy Policy"
        var ss:SpannableString= SpannableString(text)
        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://searchbuddy-assets.s3.ap-south-1.amazonaws.com/document/Terms%20of%20Use%20Agreement%20Search%20Buddy%20v.3.pdf"))
                startActivity(intent)
            }
        }

        val clickableSpan2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://searchbuddy-assets.s3.ap-south-1.amazonaws.com/document/Privacy+Policy+Search+Buddy+v.1.pdf"))
                startActivity(intent)
            }
        }
        ss.setSpan(clickableSpan1,47,63, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(clickableSpan2,68,82, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.privacy.setText(ss)
        binding.privacy.setMovementMethod(LinkMovementMethod.getInstance())
        activityLoginViewModel.errorMessage()?.observe(this, {
            LocalSessionManager.saveValue(Constant.OtpMobile,binding.etMail.text.toString(),this)
            var intent = Intent(this, Form_One::class.java)
            startActivity(intent)
            finish()
        })

        binding.continueLogin.setOnClickListener {
            var intent=Intent(this, Login::class.java)
            startActivity(intent)
        }
        binding.browseJobs.setOnClickListener {
            var intent=Intent(this, BrowseJobs::class.java)
            startActivity(intent)
        }

    }
    fun validatenumber():Boolean{
        if (binding.etMail.length()<10){
            binding.tiUsername.error="Enter valid mobile number"
            return false
        }
        else if(binding.etMail.length()==10){
            binding.tiUsername.isErrorEnabled=false
        }
        return true
    }
    private fun isValidate(): Boolean =
        validatenumber()
    private fun requestLogin(
        progress: ProgressBar
    ) {

        var mobileNo=object {
            var mobileNo=binding.etMail.text.toString()
        }

        activityLoginViewModel.requestLogin(this, mobileNo, progress).observe(this, {
            LocalSessionManager.saveValue(Constant.OtpID,it.id,this)
            LocalSessionManager.saveValue(Constant.OtpMobile,binding.etMail.text.toString(),this)
            var intent= Intent(this, EnterOtp::class.java)
            startActivity(intent)
            finish()
        })

    }
    }

