package com.example.searchbuddy

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.searchbuddy.databinding.ActivityFaqBinding

class Faq : AppCompatActivity() {
    private lateinit var binding: ActivityFaqBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.orange)
        }
        binding.llCandidate.visibility=View.GONE
        binding.searchbuddy.setBackgroundResource(R.drawable.work_status_selected_border)
        binding.searchbuddy.setOnClickListener {
            binding.searchbuddy.setBackgroundResource(R.drawable.work_status_selected_border)
            binding.candidate.setBackgroundResource(R.drawable.work_status_border)
            binding.llCandidate.visibility=View.GONE
            binding.llSearchbuddy.visibility=View.VISIBLE
        }
        binding.candidate.setOnClickListener {
            binding.candidate.setBackgroundResource(R.drawable.work_status_selected_border)
            binding.searchbuddy.setBackgroundResource(R.drawable.work_status_border)
            binding.llSearchbuddy.visibility=View.GONE
            binding.llCandidate.visibility=View.VISIBLE
        }
    }
}