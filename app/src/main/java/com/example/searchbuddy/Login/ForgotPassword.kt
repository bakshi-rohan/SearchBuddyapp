package com.example.searchbuddy.Login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.searchbuddy.R
import com.example.searchbuddy.databinding.ActivityForgotPasswordBinding

class ForgotPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    lateinit var activityResetViewModel: ResetPasswordViewModel
    lateinit var list: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activityResetViewModel = ViewModelProvider(this).get(ResetPasswordViewModel::class.java)

        binding.btnResetPass.setOnClickListener {
            if (isValidate()) {
                var userName = binding.etMail!!.text.toString()
                list = ArrayList()
                list.add(userName)
                requestPassword(userName, this, binding.progress)


            }
        }
        binding.etMail.addTextChangedListener(object : TextWatcher {
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
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.orange)
        }
        activityResetViewModel.errorMessage()?.observe(this, {
            showToast(it.toString())
        })

    }
    private fun isValidate(): Boolean =
        validateUserName()
    fun validateUserName(): Boolean {
        if (binding.etMail.text.toString().trim().isEmpty()) {
            binding.tiUsername.error = "Enter Email Address!"
            binding.etMail.requestFocus()
            return false
        }
        else if (emailValidator(binding.etMail)==false){
            return false
        }
        else {
            binding.tiUsername.isErrorEnabled = false
        }
        return true
    }
    fun emailValidator(etMail: EditText):Boolean {

        // extract the entered data from the EditText
        val emailToText = etMail.text.toString()

        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
        } else {
            binding.tiUsername.error = "Enter a Valid Email Address!"
            binding.etMail.requestFocus()
            return false
        }
        return true
    }
    override fun onUserInteraction() {
        super.onUserInteraction()
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
    private fun requestPassword(
        userName: String,
        ResetActivity: ForgotPassword,
        progress: ProgressBar
    ) {

        var resetParams: ArrayList<String> = list
        activityResetViewModel.requestReset(this, resetParams, progress).observe(this, {

            Log.i("Mesaaageeee", it.message)
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        })

    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}