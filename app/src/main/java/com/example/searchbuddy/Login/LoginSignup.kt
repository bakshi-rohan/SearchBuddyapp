package com.example.searchbuddy.Login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.searchbuddy.Forms.Form_One
import com.example.searchbuddy.R
import com.example.searchbuddy.Utils.Constant
import com.example.searchbuddy.Utils.LocalSessionManager
import com.example.searchbuddy.databinding.ActivityLoginSignupBinding

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
            window.statusBarColor = this.resources.getColor(R.color.orange)
        }
//        binding.findOut.setOnClickListener{
//            val intent=Intent(this,Dashboard::class.java)
//            startActivity(intent)
//        }

       }

    }
