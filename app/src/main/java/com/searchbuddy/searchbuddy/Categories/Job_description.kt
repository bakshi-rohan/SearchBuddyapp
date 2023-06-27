package com.searchbuddy.searchbuddy.Categories

//import android.app.Fragment
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.FragmentJobDescriptionBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.searchbuddy.searchbuddy.Dashboard.Dashboard
import java.net.URL
import java.util.*
import java.util.concurrent.Executors


class Job_description : Fragment() {
    var position_name = ""
    var company_name = ""
    var url: String? = ""
    lateinit var binding: FragmentJobDescriptionBinding
    lateinit var header: RelativeLayout
    lateinit var bottomNavView: BottomNavigationView
    lateinit var position_id: String
    var position_id_int: Int = 0
    lateinit var viewModel: JobdescriptionViewModel
    lateinit var message: String
    lateinit var logo: String

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
        binding.jobDescriptionBtn.setBackgroundResource(R.drawable.job_sdesc_selected_border)
        var bundle = Bundle()
        if (arguments != null) {
            position_id = requireArguments().getInt("position_id").toString()
            position_id_int = requireArguments().getInt("position_id")
            position_name = requireArguments().getString("position").toString()
            company_name = requireArguments().getString("company_name").toString()
            url = requireArguments().getString("url").toString()
            Log.i("url", url.toString())
        }
        binding.backBtnDesc.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.jobDescriptionBtn.setTypeface(null, Typeface.BOLD)



        if (url != null) {
            binding.apply.visibility = View.VISIBLE
            binding.apply.setOnClickListener {
                var bundle: Bundle = Bundle()
                bundle.putString("position_id", position_id!!)
                Navigation.findNavController(it)
                    .navigate(R.id.action_nav_job_desc_to_question_fragment, bundle)
            }
        }

        binding.jobDescriptionBtn.setOnClickListener {
            binding.jobDescLayout.visibility = View.VISIBLE
            binding.jobDescriptionBtn.setTypeface(null, Typeface.BOLD)
            binding.aboutCompanyBtn.setTypeface(null, Typeface.NORMAL)
            binding.aboutCompanyLayout.visibility = View.GONE
            binding.jobDescriptionBtn.setBackgroundResource(R.drawable.job_sdesc_selected_border)
            binding.jobDescriptionBtn.setTextColor(Color.WHITE)
            binding.aboutCompanyBtn.setTextColor(Color.BLACK)
            binding.aboutCompanyBtn.setBackgroundResource(R.drawable.job_desc_border)

//            val text: String = "Job Description"
//            val content: SpannableString = SpannableString(text)
//            content.setSpan(UnderlineSpan(), 0, text.length, 0)
//
//            binding.jobDescriptionBtn.setText(content)
            binding.aboutCompanyBtn.setText("About Company")

        }
        binding.aboutCompanyBtn.setOnClickListener {
            binding.jobDescLayout.visibility = View.GONE
            binding.aboutCompanyBtn.setTypeface(null, Typeface.BOLD)
            binding.jobDescriptionBtn.setTypeface(null, Typeface.NORMAL)
            binding.aboutCompanyLayout.visibility = View.VISIBLE
            binding.aboutCompanyBtn.setBackgroundResource(R.drawable.job_sdesc_selected_border)
            binding.jobDescriptionBtn.setBackgroundResource(R.drawable.job_desc_border)
            binding.aboutCompanyBtn.setTextColor(Color.WHITE)
            binding.jobDescriptionBtn.setTextColor(Color.BLACK)
//            val text: String = "About Company"
//            val content: SpannableString = SpannableString(text)
//            content.setSpan(UnderlineSpan(), 0, text.length, 0)
//            binding.aboutCompanyBtn.setText(content)
            binding.jobDescriptionBtn.setText("Job Description")


        }
        binding.saveJob.setOnClickListener {
            saveJobs()
        }
        if (binding.saveJob.text=="Saved"){
            binding.saveJob.setText("Save")
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
                Toast.makeText(
                    requireContext(),
                    "Please install whatsapp first.",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            // Starting Whatsapp
            startActivity(intent)
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
                    if (it != null) {
                        if (it.name != null) {
                            binding.jobNameDescription.setText(it.name)
                        }
                        if (it.client != null) {
                            binding.companyNameDescription.setText(it.client)
                        }
                        if (it.expTo != null && it.expFrom != null) {
                            binding.experienceDesc.setText(it.expFrom + "-" + it.expTo + "years")
                        }
                        if (it.roleDesc != null) {
                            binding.positionOverviewDetail.setText(getHtml(it.roleDesc))
                        }
                        if (it.aboutOrg != null) {
                            binding.aboutComp.setText(getHtml(it.aboutOrg))
                        }
                        if (it.kra != null) {
                            binding.tiKra.setText(getHtml(it.kra))
                        }
                        if (it.totalViewed!=null){
                            binding.totalViews.setText(it.totalViewed.toString())
                        }
                        if (it.totalApplied!=null){
                            binding.totalApplied.setText(it.totalApplied.toString())
                        }
                        if (it.logo != null) {
                            logo = it.logo
                            val executor = Executors.newSingleThreadExecutor()
                            var image: Bitmap? = null
                            val handler = Handler(Looper.getMainLooper())
                            executor.execute {
                                val imageUrl =
                                    "https://www.searchbuddy.in/api/get-picture/organisation/" + logo
                                try {
                                    val `in` = java.net.URL(imageUrl).openStream()
                                    image = BitmapFactory.decodeStream(`in`)
                                    if (image != null) {

                                        handler.post {
                                            binding.build.setImageBitmap(image)
                                            binding.build.setImageBitmap(image)
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        if (it.positionSaved != null) {
                            if (it.positionSaved == true) {
                                binding.saveJob.setText("Saved")
                                binding.saveJob.setOnClickListener {
                                    deleteJobs()
                                }
                            } else {
                                binding.saveJob.setText("Save")
                                binding.saveJob.setOnClickListener {
                                    saveJobs()
                                }
                            }
                        }
                        if (it.location != null) {
                            val some = it.location.toString()
                            var somstr = some.substring(1, some.length - 1)
                            binding.locationDesc.setText(somstr)
                        }
                        if (it.level != null) {
                            binding.level.setText(it.level)
                        }
                        if (it.url != null) {
                            var name=it.name
                            var jd="https://www.searchbuddy.in/#/app/jd?id="+it.id
                            Log.i("mmm",jd)
                            val myURL = URL(jd)
                            message =
                                "Hey checkout this job I found on SearchBuddy" + "  " + myURL
                        }
                        if (it.applied != null) {
                            if (it.applied == true) {
                                binding.apply.setText("Applied")
                                binding.apply.isClickable = false
                                binding.buttonLayout.visibility=View.GONE

                            } else {
                                binding.apply.setText("Apply")
                                binding.apply.isClickable = true
                            }

                        } else {
                            binding.apply.setText("Apply")
                            binding.apply.isClickable = true
                        }
                    }
                }
            })
//        viewModel.errorMessage()?.observe(viewLifecycleOwner, {
//            showToast("No data found")
//        })
        binding.paperPlane.setOnClickListener {
            sendMessage(message)
        }

        return binding.root
    }

    private fun saveJobs() {

        var jobList = arrayListOf<Any>()
        var kk = object {
            var id = position_id_int
        }
        jobList.add(kk)
        viewModel.saveJobs(requireContext(), jobList, binding.progress)
            .observe(viewLifecycleOwner, {
//                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                viewModel.requestJobs(requireContext(), position_id.toInt(), binding.progress)
                    .observe(viewLifecycleOwner, {
//                Log.i("qqq", it.expTo)
                        if (it == null) {
                            binding.positionOverviewDetail.setText("No Data Found")
                        } else {
                            if (it != null) {
                                if (it.name != null) {
                                    binding.jobNameDescription.setText(it.name)
                                }
                                if (it.client != null) {
                                    binding.companyNameDescription.setText(it.client)
                                }
                                if (it.expTo != null && it.expFrom != null) {
                                    binding.experienceDesc.setText(it.expFrom + "-" + it.expTo + "years")
                                }
                                if (it.roleDesc != null) {
                                    binding.positionOverviewDetail.setText(getHtml(it.roleDesc))
                                }
                                if (it.aboutOrg != null) {
                                    binding.aboutComp.setText(getHtml(it.aboutOrg))
                                }
                                if (it.kra != null) {
                                    binding.tiKra.setText(getHtml(it.kra))
                                }
                                if (it.logo != null) {
                                    logo = it.logo
                                    val executor = Executors.newSingleThreadExecutor()
                                    var image: Bitmap? = null
                                    val handler = Handler(Looper.getMainLooper())
                                    executor.execute {
                                        val imageUrl =
                                            "https://www.searchbuddy.in/api/get-picture/organisation/" + logo
                                        try {
                                            val `in` = java.net.URL(imageUrl).openStream()
                                            image = BitmapFactory.decodeStream(`in`)
                                            if (image != null) {

                                                handler.post {
                                                    binding.build.setImageBitmap(image)
                                                    binding.build.setImageBitmap(image)
                                                }
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                }
                                if (it.positionSaved != null) {
                                    if (it.positionSaved == true) {
                                        binding.saveJob.setText("Saved")
                                        binding.saveJob.setOnClickListener {
                                            deleteJobs()
                                        }
                                    } else {
                                        binding.saveJob.setText("Save")
                                        binding.saveJob.setOnClickListener {
                                            saveJobs()
                                        }
                                    }
                                }
                                if (it.location != null) {
                                    val some = it.location.toString()
                                    var somstr = some.substring(1, some.length - 1)
                                    binding.locationDesc.setText(somstr)
                                }
                                if (it.level != null) {
                                    binding.level.setText(it.level)
                                }
                                if (it.url != null) {
                                    message =
                                        "Hey checkout this job I found on SearchBuddy" + "  " + url.toString()
                                }
                                if (it.applied != null) {
                                    if (it.applied == true) {
                                        binding.apply.setText("Applied")
                                        binding.apply.isClickable = false
                                    } else {
                                        binding.apply.setText("Apply")
                                        binding.apply.isClickable = true
                                    }

                                } else {
                                    binding.apply.setText("Apply")
                                    binding.apply.isClickable = true
                                }
                            }
                        }
                    })
                binding.saveJob.setText("Saved")
            })
    }

    private fun deleteJobs() {


        var id = position_id_int

//        jobList.add(kk)
        viewModel.deleteJobs(requireContext(), id, binding.progress).observe(viewLifecycleOwner, {
//            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            binding.saveJob.setText("Save")

            viewModel.requestJobs(requireContext(), position_id.toInt(), binding.progress)
                .observe(viewLifecycleOwner, {
//                Log.i("qqq", it.expTo)
                    if (it == null) {
                        binding.positionOverviewDetail.setText("No Data Found")
                    } else {
                        if (it != null) {
                            if (it.name != null) {
                                binding.jobNameDescription.setText(it.name)
                            }
                            if (it.client != null) {
                                binding.companyNameDescription.setText(it.client)
                            }
                            if (it.expTo != null && it.expFrom != null) {
                                binding.experienceDesc.setText(it.expFrom + "-" + it.expTo + "years")
                            }
                            if (it.roleDesc != null) {
                                binding.positionOverviewDetail.setText(getHtml(it.roleDesc))
                            }
                            if (it.aboutOrg != null) {
                                binding.aboutComp.setText(getHtml(it.aboutOrg))
                            }
                            if (it.kra != null) {
                                binding.tiKra.setText(getHtml(it.kra))
                            }
                            if (it.logo != null) {
                                logo = it.logo
                                val executor = Executors.newSingleThreadExecutor()
                                var image: Bitmap? = null
                                val handler = Handler(Looper.getMainLooper())
                                executor.execute {
                                    val imageUrl =
                                        "https://www.searchbuddy.in/api/get-picture/organisation/" + logo
                                    try {
                                        val `in` = java.net.URL(imageUrl).openStream()
                                        image = BitmapFactory.decodeStream(`in`)
                                        if (image != null) {

                                            handler.post {
                                                binding.build.setImageBitmap(image)
                                                binding.build.setImageBitmap(image)
                                            }
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                            if (it.positionSaved != null) {
                                if (it.positionSaved == true) {
                                    binding.saveJob.setText("Saved")
                                    binding.saveJob.setOnClickListener {
                                        deleteJobs()
                                    }
                                } else {
                                    binding.saveJob.setText("Save")
                                    binding.saveJob.setOnClickListener {
                                        saveJobs()
                                    }
                                }
                            }
                            if (it.location != null) {
                                val some = it.location.toString()
                                var somstr = some.substring(1, some.length - 1)
                                binding.locationDesc.setText(somstr)
                            }
                            if (it.level != null) {
                                binding.level.setText(it.level)
                            }
                            if (it.url != null) {
                                message =
                                    "Hey checkout this job I found on SearchBuddy" + "  " + url.toString()
                            }
                            if (it.applied != null) {
                                if (it.applied == true) {
                                    binding.apply.setText("Applied")
                                    binding.apply.isClickable = false
                                } else {
                                    binding.apply.setText("Apply")
                                    binding.apply.isClickable = true
                                }

                            } else {
                                binding.apply.setText("Apply")
                                binding.apply.isClickable = true
                            }
                        }
                    }
                })
        })
    }

    fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
    fun getHtml(htmlBody: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(htmlBody, Html.FROM_HTML_MODE_LEGACY).toString()
        else
            Html.fromHtml(htmlBody).toString()
    }

}