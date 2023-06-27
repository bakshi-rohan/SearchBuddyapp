package com.searchbuddy.searchbuddy.Login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.ActivityLoginSignupBinding
import com.searchbuddy.searchbuddy.Forms.Form_One
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager


class LoginSignup : AppCompatActivity() {
private lateinit var binding : ActivityLoginSignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        binding.btnLoginSignup.setOnClickListener{
            val intent = Intent (this, Login::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnsignup.setOnClickListener{
            val intent = Intent (this, Form_One::class.java)
            startActivity(intent)
        }
                    var isLogedIn = LocalSessionManager.getBoolValue(Constant.IS_LOGED_IN, false, this)
//if (isLogedIn!!){
//    binding.findOut.visibility=View.GONE
//}

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.grey)
        }
//        binding.findOut.setOnClickListener{
//            val intent=Intent(this,Dashboard::class.java)
//            startActivity(intent)
//        }

       }

    }
