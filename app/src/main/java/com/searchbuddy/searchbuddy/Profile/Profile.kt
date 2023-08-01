package com.searchbuddy.searchbuddy.Profile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.FragmentSettingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import com.searchbuddy.searchbuddy.Dashboard.Dashboard
import com.searchbuddy.searchbuddy.Faq
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.Executors


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class Profile : Fragment() {
    lateinit var binding: FragmentSettingBinding

    //    lateinit var profileIcon: ShapeableImageView
    lateinit var notificationIcon: ShapeableImageView
    lateinit var bar: androidx.appcompat.widget.SearchView
    lateinit var profileIcon: ShapeableImageView
    lateinit var header: RelativeLayout
    lateinit var imageView: ShapeableImageView
    lateinit var camera_button: ShapeableImageView
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var pdfUri: Uri
    lateinit var absolutePath: File
    lateinit var files: File
    lateinit var Videofiles: File
    private lateinit var extension: String
    lateinit var type: String
    lateinit var profileUri: Uri
    lateinit var profileFile: File
    val REQUEST_CODE = 200
    lateinit var viewModel: ProfileViewModel
    lateinit var UserID: String
    var yo: Int = 0
    lateinit var cvName: String
    var fileName: String = ""
    var videoName: String = ""
    lateinit var name: String
    private val CAMERA = 2
    lateinit var bottomNavView: BottomNavigationView
    lateinit var userId:String
     var userIdInt:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
//        profileIcon = (activity as Dashboard)!!.findViewById(R.id._icon_profile)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
//
        bottomNavView = (activity as Dashboard?)!!.findViewById(R.id.nav_view)
        val nav: Menu = bottomNavView.menu
        var home = nav.findItem(R.id.navigation_home)
        home.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {

                activity?.onBackPressed()

                return true
            }
        })


        var navBar: BottomNavigationView =
            requireActivity().findViewById(com.bumptech.searchbuddy.R.id.nav_view)
        navBar.visibility = View.VISIBLE
        notificationIcon =
            (activity as Dashboard)!!.findViewById(com.bumptech.searchbuddy.R.id.notification_icon)
//        bar = (activity as Dashboard)!!.findViewById(com.example.searchbuddy.R.id.search_bar_home)
//        bar.visibility=View.GONE
        profileIcon =
            (activity as Dashboard)!!.findViewById(com.bumptech.searchbuddy.R.id.drawer_icon)
        profileIcon.visibility = View.GONE
        header = (activity as Dashboard)!!.findViewById(com.bumptech.searchbuddy.R.id.header)
        header.setBackgroundColor(Color.parseColor("#d9d9d9"))
        notificationIcon.visibility = View.GONE
        userId = LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
userIdInt=userId.toInt()
        binding.edtBasic.setOnClickListener {
            val intent = Intent(context, PersonalDetails::class.java)
            startActivity(intent)
        }
        fun goFaq() {
            var intent = Intent(requireContext(), Faq::class.java)
            startActivity(intent)
        }


        binding.edtEmp.setOnClickListener {
            val intent = Intent(context, AddProfessionalDetail::class.java)
            startActivity(intent)
        }

        binding.edtPref.setOnClickListener {
            val intent = Intent(context, Prefrence::class.java)
            startActivity(intent)
        }


        binding.edtEduc.setOnClickListener {
            val intent = Intent(context, Education_details::class.java)
            startActivity(intent)
        }

        binding.resumeUpload.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_OPEN_DOCUMENT)
                .setType("PDF")

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }

        binding.resumeUpload.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_OPEN_DOCUMENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }
        binding.selectVideoResume.setOnClickListener {
//            requestStoragePermission()
            val builder = AlertDialog.Builder(requireContext())

            builder.setMessage("Choose Video from")

            builder.setPositiveButton(
                "Camera "
            ) { dialog, id ->
                requestStoragePermissionCamera()
            }

            //set positive button
            builder.setNegativeButton(
                "Gallery"
            ) { dialog, id ->
                chooseVideo(requireContext())
            }

            //set neutral button
            builder.setNeutralButton("Cancel") { dialog, id ->
            }
            builder.show()

        }
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    "Storage Permission Granted"

                    //Here you can go with the action that requires permission
                    //For now I am just showing a normal String in TextView
                } else {
                    "Storage Permission NOT Granted"

                    //Here you need to skip the functionality that requires permission
                    //Because user has denied the permission
                    //you can ask permission again when user will request the feature
                    //that requires this permission
                }
            }
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            }

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    "Storage Permission Granted"

                } else {
                    "Storage Permission NOT Granted"

                }
            }
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    profileUri = data!!.data!!
                    profileFile = File(getRealPathFromURI(profileUri))
                    val filePath: String = profileFile.path


                }
            }
        fun checkIfFragmentAttached(operation: Context.() -> Unit) {
            if (isAdded && context != null) {
                operation(requireContext())
            }
        }
        cvName = ""
       var UserId =
            LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
        Log.i("User",userId.toString())

        yo  = UserId.toInt()

        viewModel.requestPersonalDetail(requireContext(), yo, binding.progress)
            .observe(requireActivity(), {
                if (it != null) {
                    if (it.userDTO != null) {
                        if (it.userDTO.name != null) {
                            binding.usernameProfile.setText(it.userDTO.name)
                        }
                        if (it.userDTO.designation != null) {
                            binding.currentDesignation.setText(it.userDTO.designation)
                        }
                        if (it.userDTO.name != null) {
                            binding.name.setText(it.userDTO.name)
                        }
                        if (it.userDTO.designation != null) {
                            binding.desg.setText(it.userDTO.designation)
                        }
                        if (it.dob != null) {
                            var date = it.dob
                            var outputDate = date.substring(0, 10);
                            binding.dob.setText(outputDate)
                        }
                        if (it.userDTO.email != null) {
                            binding.email.setText(it.userDTO.email)
                        }
                        if (it.userDTO.mobileNo != null) {
                            binding.mob.setText(it.userDTO.mobileNo)
                        }
                        if (it.gender != null && it.gender.id == 83) {
                            binding.prefix.setText("Male")
                        } else if (it.gender.id == 84) {
                            binding.prefix.setText("Female")
                        } else if (it.gender != null && it.gender.id == 4279) {
                            binding.prefix.setText("Others")
                        }
                        if (it.videoProfile != null) {
                            var video_name = it.videoProfile
                            var video_sub = video_name.substring(0, 18)
//                            binding.videoPath.setText(video_sub + ".mp4")
                            videoName = it.videoProfile
                        }
                        if (it.cv != null) {
                            var cv_name = it.cv
                            var cv_sub = cv_name.substring(0, 12)
                            binding.filePath.setText(cv_sub)
                            fileName = it.cv
                            Log.i("aaaaa", fileName)
                            checkIfFragmentAttached {
                                LocalSessionManager.saveValue("pdf_name", cv_sub, requireContext())
                            }
                        }
                    }
                }
            })
        viewModel.requestWorkHistory(requireContext(), binding.progress).observe(requireActivity(), {
//            Log.i("comapany", it.companyName.toString())
            if (it.companyName != null) {
                binding.company.setText(it.companyName.toString())
            }
            if (it.designation != null) {
                binding.designation.setText(it.designation.toString())
            }
            if (it.function != null) {
                binding.function.setText(it.function.value.toString())
            }
            if (it.location!=null){
                binding.locationProfile.setText(it.location.toString())
            }
            if (it.expMonths!=null){
                binding.experience.setText(it.expMonths.toString()+ "months")
            }

        })
//        var webView:WebView=binding.webview
//        val webView = binding.webview as WebView


//        viewModel.requestProfessionalDetail(requireContext(), yo, binding.progress)
//            .observe(requireActivity(), {
//                if (it.professionalDetails != null) {
//                    if (it.professionalDetails.level != null) {
//                        if (it.professionalDetails.level.value != null) {
//                            binding.level.setText(it.professionalDetails.level.value)
//                        }
//                    }
//                    if (it.function != null) {
//                        if (it.function.value != null) {
//                            binding.function.setText(it.function.value)
//                        }
//                    }
//                    if (it.professionalDetails.annualSalary != null) {
//                        binding.annualSalary.setText(it.professionalDetails.annualSalary.toString() + "Rs")
//                    }
//                    if (it.experienceMonths != null) {
//                        binding.experience.setText(it.experienceMonths.toString() + " Months")
//                    }
//                    if (it.professionalDetails.industry != null) {
//                        binding.industry.setText(it.professionalDetails.industry.toString())
//                    }
//                }
//            })
        viewModel.requestEducationDetail(requireContext(), yo, binding.progress)
            .observe(requireActivity(), {
                if (it.professionalDetails != null) {
                    if (it.professionalDetails.educationDetails != null) {
//                    if (it.professionalDetails.educationDetails.metric.percentage!= null&&it.professionalDetails.educationDetails.metric.percentage!="") {
//                        binding.tenthPercent.setText(it.professionalDetails.educationDetails.metric.percentage.toString()+"%" )
//                    }
                        if (it.professionalDetails.educationDetails.intermediate.percentage != null && it.professionalDetails.educationDetails.intermediate.percentage != "") {
                            binding.twelvePercent.setText(it.professionalDetails.educationDetails.intermediate.percentage.toString() + "%")
                        }
                        if (it.professionalDetails.educationDetails.graduation.percentage != null && it.professionalDetails.educationDetails.graduation.percentage != "") {
                            binding.gradCgpa.setText(it.professionalDetails.educationDetails.graduation.percentage.toString())
                        }
                        if (it.professionalDetails.educationDetails.postGraduation.percentage != null && it.professionalDetails.educationDetails.postGraduation.percentage != "") {
                            binding.postCgpa.setText(it.professionalDetails.educationDetails.postGraduation.percentage.toString())
                        }
                    }
                }
            })
        viewModel.requestPrefrencesDetail(requireContext(), yo, binding.progress)
            .observe(requireActivity(), {
                if (it.professionalDetails != null) {
                    if (it.professionalDetails.preferences != null) {
                        if (it.professionalDetails.preferences.location != null) {
                            var loc = it.professionalDetails.preferences.location.toString()
                            var LocString = loc.substring(1, loc.length - 1)
                            binding.location.setText(LocString)
                        }
                        if (it.professionalDetails.preferences.role != null) {
                            var role = it.professionalDetails.preferences.role.toString()
                            var roleString = role.substring(1, role.length - 1)
                            binding.role.setText(roleString)
                        }
                        if (it.professionalDetails.preferences.employementType != null) {
                            var employement =
                                it.professionalDetails.preferences.employementType.toString()
                            var empString = employement.substring(1, employement.length - 1)
                            binding.empType.setText(empString)
                        }
                        if (it.professionalDetails.preferences.workType != null) {
                            var worktype = it.professionalDetails.preferences.workType.toString()
                            var workString = worktype.substring(1, worktype.length - 1)
                            binding.workType.setText(workString)
                        }
                    }
                }
            })
        imageView = binding.imProfilePic
        camera_button = binding.cameraImg
        camera_button.setOnClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setMessage("Choose Image from")

            builder.setPositiveButton(
                "Camera "
            ) { dialog, id ->
                // User clicked Update Now button


                requestCameraPermission()


            }

            //set positive button
            builder.setNegativeButton(
                "Gallery"
            ) { dialog, id ->
                // User cancelled the dialog
                requestStoragePermission()

            }

            //set neutral button
            builder.setNeutralButton("Cancel") { dialog, id ->
                // User Click on reminder me latter
            }
            builder.show()
        }
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    "Storage Permission Granted"

                    //Here you can go with the action that requires permission
                    //For now I am just showing a normal String in TextView
                } else {
                    "Storage Permission NOT Granted"

                    //Here you need to skip the functionality that requires permission
                    //Because user has denied the permission
                    //you can ask permission again when user will request the feature
                    //that requires this permission
                }
            }
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    profileUri = data!!.data!!
                    profileFile = File(getRealPathFromURI(profileUri))
                    val filePath: String = profileFile.path
                    val bitmap = BitmapFactory.decodeFile(filePath)
                    Glide.with(this).load(profileFile).apply(
                        RequestOptions()
                            .placeholder(com.bumptech.searchbuddy.R.drawable.profile_placeholder)
                    ).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imProfilePic)

                }

            }

        name = LocalSessionManager.getStringValue("pdf_name", "", requireContext()).toString()
        Log.i("bbb", name)
//        if (fileName!=null){
//            binding.filePath.setText(fileName)
//        }
        binding.imResume.setOnClickListener {
            if (cvName != null) {

//                var download= context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                Log.i("zzzzz", fileName.toString())
                if (fileName != null) {
                    val openURL = Intent(android.content.Intent.ACTION_VIEW)
                    openURL.data =
                        Uri.parse("https://www.searchbuddy.in/api/download/mobile/cv/" + fileName)
                    startActivity(openURL)
//
                }
            }
        }
        binding.filePath.setOnClickListener {
            if (cvName != null) {

//                var download= context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                Log.i("zzzzz", fileName.toString())
                if (fileName != null) {
                    val openURL = Intent(android.content.Intent.ACTION_VIEW)
                    openURL.data =
                        Uri.parse("https://www.searchbuddy.in/api/download/mobile/cv/" + fileName)
                    startActivity(openURL)
//                    var url =
//                        "https://testingsales.searchbuddy.in/api/download/mobile/cv/" + fileName
//                    var uri = Uri.parse(url)
//                    var getPdf = DownloadManager.Request(uri)
//                    getPdf.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                    download.enqueue(getPdf)
//                    Toast.makeText(context, "Download Started", Toast.LENGTH_LONG).show()
                }
            }
        }
        binding.imVResume.setOnClickListener {
            if (cvName != null) {

                var download =
                    context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                Log.i("zzzzz", videoName.toString())
                if (fileName != null) {
                    var url =
                        "https://www.searchbuddy.in/api/download/videos/" + videoName
                    var uri = Uri.parse(url)
                    var getPdf = DownloadManager.Request(uri)
                    getPdf.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    download.enqueue(getPdf)
                    Toast.makeText(context, "Download Started", Toast.LENGTH_LONG).show()
//                    val openURL = Intent(android.content.Intent.ACTION_VIEW)
//                    openURL.data = Uri.parse("https://testingsales.searchbuddy.in/api/download/videos/" + videoName)
//                    startActivity(openURL)
//
                }
            }
        }
        binding.videoPath.setOnClickListener {
            if (cvName != null) {

                var download =
                    context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                Log.i("zzzzz", videoName.toString())
                if (fileName != null) {
                    var url =
                        "https://www.searchbuddy.in/api/download/videos/" + videoName
                    var uri = Uri.parse(url)
                    var getPdf = DownloadManager.Request(uri)
                    getPdf.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    download.enqueue(getPdf)
                    Toast.makeText(context, "Download Started", Toast.LENGTH_LONG).show()
                }
            }
        }
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        fun checkIfFragmentAttached(operation: Context.() -> Unit) {
            if (isAdded && context != null) {
                operation(requireContext())
            }
        }
        cvName = ""
        UserID =
            LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()

        yo = userIdInt
        viewModel.requestPersonalDetail(requireContext(), yo, binding.progress)
            .observe(requireActivity(), {
                if (it != null) {
                    if (it.userDTO != null) {
                        if (it.userDTO.name != null) {
                            binding.usernameProfile.setText(it.userDTO.name)
                        }
                        if (it.userDTO.designation != null) {
                            binding.currentDesignation.setText(it.userDTO.designation)
                        }
                        if (it.userDTO.name != null) {
                            binding.name.setText(it.userDTO.name)
                        }
                        if (it.userDTO.designation != null) {
                            binding.desg.setText(it.userDTO.designation)
                        }
                        if (it.dob != null) {
                            var date = it.dob
                            var outputDate = date.substring(0, 10);
                            binding.dob.setText(outputDate)
                        }
                        if (it.userDTO.email != null) {
                            binding.email.setText(it.userDTO.email)
                        }
                        if (it.userDTO.mobileNo != null) {
                            binding.mob.setText(it.userDTO.mobileNo)
                        }
                        if (it.gender != null && it.gender.id == 83) {
                            binding.prefix.setText("Male")
                        } else if (it.gender.id == 84) {
                            binding.prefix.setText("Female")
                        } else if (it.gender != null && it.gender.id == 4279) {
                            binding.prefix.setText("Others")
                        }
                        if (it.cv != null) {
                            var cv_name = it.cv
                            var cv_sub = cv_name.substring(0, 12)
                            binding.filePath.setText(cv_sub)
                            fileName = it.cv
                            Log.i("aaaaa", fileName)
                            checkIfFragmentAttached {
                                LocalSessionManager.saveValue("pdf_name", cv_sub, requireContext())
                            }
                        }
                        checkIfFragmentAttached {
                            if (it.userDTO.profilePicName != null) {
                                val executor = Executors.newSingleThreadExecutor()
                                var image: Bitmap? = null
                                val handler = Handler(Looper.getMainLooper())
                                var uri =
                                    Uri.parse("https://www.searchbuddy.in/api/get-picture/profilepicture/" + it.userDTO.profilePicName)
                                Glide.with(requireContext()).load(uri).into(binding.imProfilePic)


                            }
                        }
                    }
                }
            })
//        var webView:WebView=binding.webview
//        val webView = binding.webview as WebView



        viewModel.requestEducationDetail(requireContext(), yo, binding.progress)
            .observe(requireActivity(), {
                if (it.professionalDetails != null) {
                    if (it.professionalDetails.educationDetails != null) {
//                    if (it.professionalDetails.educationDetails.metric.percentage != null&&it.professionalDetails.educationDetails.metric.percentage!="") {
//                        binding.tenthPercent.setText(it.professionalDetails.educationDetails.metric.percentage.toString()+"%" )
//                    }
                        if (it.professionalDetails.educationDetails.intermediate.percentage != null && it.professionalDetails.educationDetails.intermediate.percentage != "") {
                            binding.twelvePercent.setText(it.professionalDetails.educationDetails.intermediate.percentage.toString() + "%")
                        }
                        if (it.professionalDetails.educationDetails.graduation.percentage != null && it.professionalDetails.educationDetails.graduation.percentage != "") {
                            binding.gradCgpa.setText(it.professionalDetails.educationDetails.graduation.percentage.toString() + "%")
                        }
                        if (it.professionalDetails.educationDetails.postGraduation.percentage != null && it.professionalDetails.educationDetails.postGraduation.percentage != "") {
                            binding.postCgpa.setText(it.professionalDetails.educationDetails.postGraduation.percentage.toString() + "%")
                        }
                    }
                }
            })
        viewModel.requestPrefrencesDetail(requireContext(), yo, binding.progress)
            .observe(requireActivity(), {
                if (it.professionalDetails != null) {
                    if (it.professionalDetails.preferences != null) {
                        if (it.professionalDetails.preferences.location != null) {
                            var loc = it.professionalDetails.preferences.location.toString()
                            var LocString = loc.substring(1, loc.length - 1)
                            binding.location.setText(LocString)
                        }
                        if (it.professionalDetails.preferences.role != null) {
                            var role = it.professionalDetails.preferences.role.toString()
                            var roleString = role.substring(1, role.length - 1)
                            binding.role.setText(roleString)
                        }
                        if (it.professionalDetails.preferences.employementType != null) {
                            var employement =
                                it.professionalDetails.preferences.employementType.toString()
                            var empString = employement.substring(1, employement.length - 1)
                            binding.empType.setText(empString)
                        }
                        if (it.professionalDetails.preferences.workType != null) {
                            var worktype = it.professionalDetails.preferences.workType.toString()
                            var workString = worktype.substring(1, worktype.length - 1)
                            binding.workType.setText(workString)
                        }
                    }
                }
            })
        viewModel.requestWorkHistory(requireContext(), binding.progress).observe(requireActivity(), {
//            Log.i("comapany", it.companyName.toString())
            if (it.companyName != null) {
                binding.company.setText(it.companyName.toString())
            }
            if (it.designation != null) {
                binding.designation.setText(it.designation.toString())
            }
            if (it.function != null) {
                binding.function.setText(it.function.value.toString())
            }
            if (it.location!=null){
                binding.locationProfile.setText(it.location.toString())
            }
            if (it.expMonths!=null){
                binding.experience.setText(it.expMonths.toString()+ "months")
            }

        })
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            pdfUri = data?.data!!
            val uri: Uri = data?.data!!
            files = getFile(requireContext(), uri)!!
//            val inputStream: InputStream? = requireContext()!!.contentResolver.openInputStream(uri)
            val uriString: String = uri.toString()
            var pdfName: String? = null
            if (uriString.startsWith("content://")) {
                var myCursor: Cursor? = null
                try {
                    myCursor =
                        requireContext()!!.contentResolver.query(uri, null, null, null, null)
                    if (myCursor != null && myCursor.moveToFirst()) {
                        pdfName =
                            myCursor.getString(myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        name =
                            myCursor.getString(myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        LocalSessionManager.saveValue(Constant.PdfName, name, requireContext())


                        binding.filePath.setText(pdfName)
                        Log.i("abbb", fileName)
                        extension = pdfName.substring(pdfName.lastIndexOf("."))
                        Log.i("extensionnn", extension)

                    }
                } finally {
                    myCursor?.close()
                }
                if (extension == ".pdf") {
                    val file: File? =
                        pdfName?.let { createTmpFileFromUri(requireContext(), uri, it) }
                    val fileExtention: String = file!!.extension

                    if (file != null) {
                        absolutePath = file
                        Log.i("abbbbbbbb", absolutePath.toString())
                    }
                } else if (extension == ".docx" || extension == ".doc") {
                    val file: File? =
                        pdfName?.let { createDocxFileFromUri(requireContext(), uri, it) }
                    val fileExtention: String = file!!.extension

                    if (file != null) {
                        absolutePath = file
                        Log.i("abbbbbbbb", absolutePath.toString())
                    }

                }


            }

            UploadResume()
//            binding.resumeUpload.setOnClickListener {
//UploadResume()
//            }


        }

//        if (resultCode == RESULT_OK) {
//            val selectedFile = data?.data //The uri with the location of the file
//            Videofiles = getFile(requireContext(), selectedFile!!)!!
//            binding.videoPath.setText("Video: " + selectedFile.toString())
//            UploadVideoResume()
//            Toast.makeText(requireContext(),"Video Resume Uploaded",Toast.LENGTH_SHORT).show()
//        } else if (resultCode == CAMERA) {
//            val selectedFile = data?.data //The uri with the location of the file
//            Videofiles = getFile(requireContext(), selectedFile!!)!!
//            Log.i("bbbbb", Videofiles.name)
//            UploadVideoResume()
//            Toast.makeText(requireContext(),"Video Resume Uploaded",Toast.LENGTH_SHORT).show()
//
//            binding.videoPath.setText("Video: " + selectedFile.toString())
//
//        }

    }

    @Throws(IOException::class)
    fun getFile(context: Context, uri: Uri): File? {
        val destinationFilename =
            File(context.filesDir.path + File.separatorChar + queryName(context, uri))
        try {
            context.contentResolver.openInputStream(uri).use { ins ->
                if (ins != null) {
                    createFileFromStream(
                        ins,
                        destinationFilename
                    )
                }
            }
        } catch (ex: java.lang.Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
        return destinationFilename
    }

    fun takeVideoFromCamera(context: Context) {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, CAMERA)

    }

    fun chooseVideo(context: Context) {


        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setType("video/*")
        startActivityForResult(intent, CAMERA)

    }

    fun createFileFromStream(ins: InputStream, destination: File?) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: java.lang.Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
    }

    private fun queryName(context: Context, uri: Uri): String {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)!!
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }

    private fun requestStoragePermissionCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
//            chooseImage(this)
            takeVideoFromCamera(requireContext())
        } else {
            showPermissionRequestExplanation(
                "Camera",
                "This permission is require to update video resume "
            ) {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    fun createDocxFileFromUri(context: Context, uri: Uri, fileName: String): File? {
        return try {
            val stream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile(fileName, "")
            org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, file)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

//    fun createDocFileFromUri(context: Context, uri: Uri, fileName: String): File? {
//        return try {
//            val stream = context.contentResolver.openInputStream(uri)
//            val file = File.createTempFile(fileName, ".doc", context.cacheDir)
//            org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, file)
//            file
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }

    fun createTmpFileFromUri(context: Context, uri: Uri, fileName: String): File? {
        return try {
            val stream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile(fileName, "")
            Log.i("xxxx", file.name)
            org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, file)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            chooseImage(requireContext())
        } else {
            showPermissionRequestExplanation(
                "write_storage",
                "This permission is require to update profile picture "
            ) {
                requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            uploadImage(requireContext())
        } else {
            showPermissionRequestExplanation(
                "Camera Permission",
                "This permission is require to update profile picture "
            ) {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(requireContext(), contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result: String = cursor.getString(column_index)
        cursor.close()
        return result
    }

    fun showPermissionRequestExplanation(
        permission: String,
        message: String,
        retry: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("$permission Required")
            setMessage(message)
            setPositiveButton("Ok") { _, _ -> retry?.invoke() }
        }.show()
    }

    fun chooseImage(context: Context) {


        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)

    }

    fun uploadImage(context: Context) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }



    private fun UploadResume() {
        UserID =
            LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()

        yo =userIdInt
        var info = object {
            var id = yo
        }
        var gson = Gson()
        var infoJ = gson.toJson(info)

        viewModel.UploadResume(requireContext(), files, infoJ, binding.progress)
            .observe(requireActivity(), {
                Toast.makeText(requireContext(), "Resume Uploaded", Toast.LENGTH_SHORT).show()
            })
    }

    private fun UploadVideoResume() {
        UserID =
            LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()

        yo = userIdInt
        var info = object {
            var id = yo
        }
        var gson = Gson()
        var infoJ = gson.toJson(info)

        viewModel.UploadVideoResume(requireContext(), Videofiles, yo, binding.progress)
            .observe(requireActivity(), {
//                Toast.makeText(requireContext(), "Video resume Uploaded", Toast.LENGTH_SHORT).show()
            })
    }

}

