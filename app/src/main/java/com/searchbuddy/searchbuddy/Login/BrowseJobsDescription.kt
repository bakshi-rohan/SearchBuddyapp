package com.searchbuddy.searchbuddy.Login

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.searchbuddy.databinding.ActivityBrowseJobsDescriptionBinding
import com.searchbuddy.searchbuddy.Categories.JobdescriptionViewModel

class BrowseJobsDescription : AppCompatActivity() {
    var position_name = ""
    var company_name = ""
    var url:String? = ""
    lateinit var binding: ActivityBrowseJobsDescriptionBinding
    lateinit var position_id: String
    lateinit var viewModel: JobdescriptionViewModel
    lateinit var message:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowseJobsDescriptionBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(JobdescriptionViewModel::class.java)

        binding.aboutCompanyLayout.visibility = View.GONE
        val text: String = "Job Description"
        var bundle = Bundle()
        if (intent.extras != null) {
            position_id = intent.extras!!.getInt("position_id").toString()
            position_name = intent.extras!!.getString("position").toString()
            company_name = intent.extras!!.getString("company_name").toString()
            url = intent.extras!!.getString("url").toString()
            Log.i("url",url.toString())
        }
        val content: SpannableString = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, text.length, 0)
        binding.jobNameDescription.text = position_name
        binding.companyNameDescription.text = company_name
        binding.jobDescriptionBtn.setText(content)


        binding.backBtnDesc.setOnClickListener {
          onBackPressed()
        }
        binding.jobDescriptionBtn.setTypeface(null, Typeface.BOLD)
        message="Hey this is Searchbuddy"
//        binding.similar.setOnClickListener {
//            Navigation.findNavController(it).navigate(R.id.action_nav_job_desc_to_nav_sales)
//        }


        if (url!=null) {
            binding.apply.visibility= View.VISIBLE
            binding.apply.setOnClickListener {
                val builder = AlertDialog.Builder(this,)
                builder.setTitle("Alert")
                builder.setMessage("Please log in to apply")
                builder.setPositiveButton("OK") { dialog, which ->
                    // handle OK button click
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)

                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    // handle Cancel button click
                }
                val dialog = builder.create()
                dialog.show()
            }
        }

        binding.jobDescriptionBtn.setOnClickListener {
            binding.jobDescLayout.visibility = View.VISIBLE
            binding.jobDescriptionBtn.setTypeface(null, Typeface.BOLD)
            binding.aboutCompanyBtn.setTypeface(null, Typeface.NORMAL)
            binding.aboutCompanyLayout.visibility = View.GONE
            val text: String = "Job Description"
            val content: SpannableString = SpannableString(text)
            content.setSpan(UnderlineSpan(), 0, text.length, 0)

            binding.jobDescriptionBtn.setText(content)
            binding.aboutCompanyBtn.setText("About Company")

        }
        binding.aboutCompanyBtn.setOnClickListener {
            binding.jobDescLayout.visibility = View.GONE
            binding.aboutCompanyBtn.setTypeface(null, Typeface.BOLD)
            binding.jobDescriptionBtn.setTypeface(null, Typeface.NORMAL)
            binding.aboutCompanyLayout.visibility = View.VISIBLE

            val text: String = "About Company"
            val content: SpannableString = SpannableString(text)
            content.setSpan(UnderlineSpan(), 0, text.length, 0)
            binding.aboutCompanyBtn.setText(content)
            binding.jobDescriptionBtn.setText("Job Description")


        }
        fun sendMessage(message: String) {

            // Creating intent with action send
            val intent = Intent(Intent.ACTION_SEND)

            // Setting Intent type
            intent.type = "text/plain"

            // Setting whatsapp package name
            intent.setPackage("com.whatsapp")

            // Give your message here
            intent.putExtra(Intent.EXTRA_TEXT, message)

            // Checking whether whatsapp is installed or not
            if (intent.resolveActivity(this.packageManager) == null) {
                Toast.makeText(this,
                    "Please install whatsapp first.",
                    Toast.LENGTH_SHORT).show()
                return
            }

            // Starting Whatsapp
            startActivity(intent)
        }
        binding.paperPlane.setOnClickListener {
            sendMessage(message)
        }

        fun getHtml(htmlBody: String): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                Html.fromHtml(htmlBody, Html.FROM_HTML_MODE_LEGACY).toString()
            else
                Html.fromHtml(htmlBody).toString()
        }
        viewModel.requestJobs(this, position_id.toInt(), binding.progress)
            .observe(this, {
//                Log.i("qqq", it.expTo)
                if (it == null) {
                    binding.positionOverviewDetail.setText("No Data Found")
                } else {
                    if (it!=null) {
                        if (it.name!=null) {
                            binding.jobNameDescription.setText(it.name)
                        }
                        if (it.client!=null) {
                            binding.companyNameDescription.setText(it.client)
                        }
                        if (it.expTo!=null&&it.expFrom!=null) {
                            binding.experienceDesc.setText(it.expFrom + "-" + it.expTo + "years")
                        }
                        if (it.roleDesc!=null) {
                            binding.positionOverviewDetail.setText(getHtml(it.roleDesc))
                        }
                        if (it.aboutOrg!=null) {
                            binding.aboutComp.setText(getHtml(it.aboutOrg))
                        }
                        if (it.kra!=null) {
                            binding.tiKra.setText(getHtml(it.kra))
                        }
//                        if (it.industry!=null) {
//                            binding.industryTypes.setText(it.industry)
//                        }
//                        if (it.name!=null) {
//                            binding.function.setText(it.function)
//                        }
                        if (it.location!=null) {
                            val some = it.location.toString()
                            var somstr = some.substring(1, some.length - 1)
                            binding.locationDesc.setText(somstr)
                        }
//                        if (it.skills!=null) {
//                            var getSkills = it.skills.toString()
//                            var skillSubString = getSkills.substring(1, getSkills.length - 1)
//                            binding.skills.setText(skillSubString)
//                        }
                        if (it.level!=null) {
                            binding.level.setText(it.level)
                        }
                        if (it.applied!=null){
                            if (it.applied==true){
                                binding.apply.setText("Applied")
                                binding.apply.isClickable=false
                            }
                            else{
                                binding.apply.setText("Apply")
                                binding.apply.isClickable=true
                            }

                        }
                        else{
                            binding.apply.setText("Apply")
                            binding.apply.isClickable=true
                        }
                    }
                }
            })
        setContentView(binding.root)

    }
}