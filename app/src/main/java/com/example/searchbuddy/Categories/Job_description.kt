package com.example.searchbuddy.Categories

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.searchbuddy.Dashboard.Dashboard
import com.example.searchbuddy.R
import com.example.searchbuddy.databinding.FragmentJobDescriptionBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class Job_description : Fragment() {
    var position_name = ""
    var company_name = ""
    var url:String? = ""
    lateinit var binding: FragmentJobDescriptionBinding
    lateinit var header: RelativeLayout
    lateinit var bottomNavView: BottomNavigationView
    lateinit var position_id: String
    lateinit var viewModel: JobdescriptionViewModel
    lateinit var message:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(JobdescriptionViewModel::class.java)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobDescriptionBinding.inflate(inflater, container, false)
        header = (activity as Dashboard).findViewById(R.id.header)
        header.visibility = View.GONE
        bottomNavView = (activity as Dashboard)!!.findViewById(R.id.nav_view)
        bottomNavView.visibility = View.GONE
        binding.aboutCompanyLayout.visibility = View.GONE
        val text: String = "Job Description"
        var bundle = Bundle()
        if (arguments != null) {
            position_id = requireArguments().getInt("position_id").toString()
            position_name = requireArguments().getString("position").toString()
            company_name = requireArguments().getString("company_name").toString()
            url = requireArguments().getString("url").toString()
            Log.i("url",url.toString())
        }
        val content: SpannableString = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, text.length, 0)
        binding.jobNameDescription.text = position_name
        binding.companyNameDescription.text = company_name
        binding.jobDescriptionBtn.setText(content)


        binding.backBtnDesc.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.jobDescriptionBtn.setTypeface(null, Typeface.BOLD)
        message="Hey this is Searchbuddy"
//        binding.similar.setOnClickListener {
//            Navigation.findNavController(it).navigate(R.id.action_nav_job_desc_to_nav_sales)
//        }


            if (url!=null) {
                binding.apply.visibility=View.VISIBLE
                binding.apply.setOnClickListener {
                    var bundle:Bundle=Bundle()
                    bundle.putString("position_id", position_id!!)
                    Navigation.findNavController(it).navigate(R.id.action_nav_job_desc_to_question_fragment,bundle)
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
            if (intent.resolveActivity(requireContext().packageManager) == null) {
                Toast.makeText(requireContext(),
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
        viewModel.requestJobs(requireContext(), position_id.toInt(), binding.progress)
            .observe(viewLifecycleOwner, {
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
                        if (it.industry!=null) {
                            binding.industryTypes.setText(it.industry)
                        }
                        if (it.name!=null) {
                            binding.function.setText(it.function)
                        }
                        if (it.location!=null) {
                            val some = it.location.toString()
                            var somstr = some.substring(1, some.length - 1)
                            binding.locationDesc.setText(somstr)
                        }
                        if (it.skills!=null) {
                            var getSkills = it.skills.toString()
                            var skillSubString = getSkills.substring(1, getSkills.length - 1)
                            binding.skills.setText(skillSubString)
                        }
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
//        viewModel.errorMessage()?.observe(viewLifecycleOwner, {
//            showToast("No data found")
//        })
        return binding.root
    }

    fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}