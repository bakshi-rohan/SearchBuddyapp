package com.searchbuddy.searchbuddy.Forms

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.auth0.android.jwt.JWT
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.ActivityFormOneBinding
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.CityList
import java.io.File
import java.io.IOException
import java.util.*


class Form_One : AppCompatActivity() {
    lateinit var binding: ActivityFormOneBinding
    lateinit var viewModel: Form_oneViewModel
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var profileUri: Uri
    lateinit var profileFile: File
//    lateinit var absolutePath: File
    lateinit var location_string: String
    var a: Int? = null
    private lateinit var pdfUri: Uri
    private lateinit var extension: String
    private var notificationManager: NotificationManager? = null
    private var radioId: Int = 0
    var exampleList: ArrayList<String> = ArrayList()
    lateinit var descriptionData:Array<String>
    lateinit var mm:String
    lateinit var dd:String
    lateinit var mobileno:String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormOneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(Form_oneViewModel::class.java)
        binding.etEmail.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    //Do something when EditText has focus
                    binding.etEmail.setError(null)
                } else {
                    // Do something when Focus is not on the EditText
                    validateEmail()
//                    val imm: InputMethodManager =
//                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                }
            }
        })
        mobileno= LocalSessionManager.getStringValue("OtpMobile","",this).toString()
        Log.i("ccccc",mobileno)
         binding.etMobile.setText(mobileno)
        descriptionData= arrayOf("Basic\nDetails", "Education", "Experience", "CV","Preferences")
        binding.yourStateProgressBarId.setStateDescriptionData(descriptionData)
        binding.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val idRadioButtonChosen: Int = binding.radioGroup.getCheckedRadioButtonId()
            if (idRadioButtonChosen > 0) {
                binding.tiExp.clearFocus()
                binding.tiLocationFormOne.clearFocus()
                binding.tiLocationForm.clearFocus()
                binding.tiDesignationSignup.clearFocus()
                binding.tiUsername.clearFocus()
                binding.tiDateFormOne.clearFocus()
//                val imm: InputMethodManager =
//                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 2)
               var radioButtonChosen = findViewById<View>(idRadioButtonChosen) as RadioButton
                binding.radioMr.setError(null)
            }
        })
        binding.etDate.setOnClickListener {
            val c = Calendar.getInstance()
            val d = Calendar.getInstance().time
            c.set(2005, 11, 31);
            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            var timeStamp=d

//            Log.i("time",timeStamp.toString())
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    var mo=monthOfYear+1
                    var dy=dayOfMonth
                    if (mo<10) {
                        mm ="0" + mo
                    }
                    else{
                        mm= mo.toString()
                    }
                    if (dy<10){
                        dd="0"+dy
                    }
                    else{
                        dd=dy.toString()
                    }
                    val dat = (year.toString() + "-" + mm + "-" + dd)
                    binding.etDate.setText(dat)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day,

                )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()

            datePickerDialog.datePicker.setMaxDate(c.timeInMillis)

        }

        binding.etMobile.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    //Do something when EditText has focus
                    binding.etMobile.setError(null)
                } else {
                    // Do something when Focus is not on the EditText
                    validatenumber()
//                    val imm: InputMethodManager =
//                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                }
            }
        })
        binding.etName.addTextChangedListener(object : TextWatcher {
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
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.tiExp.setError(null)
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
        binding.etCover.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.tiDesignationSignup.setError(null)
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
        binding.etMobile.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.tiExp.setError(null)
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


        binding.btnSbmtFormone.setOnClickListener {
//            Toast.makeText(this,"Hello",Toast.LENGTH_SHORT).show()
//var intent=Intent(this,Signup_Education_Details::class.java)
//            startActivity(intent)
            createProfile()

        }

        notificationManager =
            getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        createNotificationChannel(
            "com.ebookfrenzy.notifydemo.news",
            "Resume Uploaded",
            "Resume Uploaded Successfully"
        )

            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.grey)

        val jsonFileString = getJsonDataFromAsset(applicationContext, "cities.json")
        val gson = Gson()
        val listCityType = object : TypeToken<List<CityList>>() {}.type
        var persons: List<CityList> = gson.fromJson(jsonFileString, listCityType)
        persons.forEachIndexed { idx, city ->
//             someList = person.Districtname
            val CityArray : JsonArray = JsonArray()
            val dataa : String = city.CombinedName

            CityArray.add(dataa)

            for (i in 0 until CityArray.size()) {
//                val he = CityList("","",person.CombinedName,"","")
//                exampleList.add(exampleArray.toString())
                var kuch = CityArray.get(i).toString()
                var result = kuch.substring(1,kuch.length-1)
                exampleList.add(result)

            }
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,exampleList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
binding.tiLocationForm.setAdapter(adapter)

        binding.tiLocationForm.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val item = parent.getItemAtPosition(position)
//                location_string = item.toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        binding.resumeUpload.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_OPEN_DOCUMENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }

        binding.resumeUpload.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_OPEN_DOCUMENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }
//        binding.resumeUpload.setOnClickListener {
//            Toast.makeText(
//                this,
//                "Please Select a File",
//                Toast.LENGTH_LONG
//            ).show()
//        }

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
viewModel.errorMessage()?.observe(this,{
    Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
})
    }


    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            pdfUri = data?.data!!
            val uri: Uri = data?.data!!
            val uriString: String = uri.toString()
            var pdfName: String? = null
            if (uriString.startsWith("content://")) {
                var myCursor: Cursor? = null
                try {
                    myCursor =
                        applicationContext!!.contentResolver.query(uri, null, null, null, null)
                    if (myCursor != null && myCursor.moveToFirst()) {
                        pdfName =
                            myCursor.getString(myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))

                        binding.filePath.text = pdfName
                        extension = pdfName.substring(pdfName.lastIndexOf("."))

                    }
                } finally {
                    myCursor?.close()
                }
                if (extension == ".pdf") {
                    val file: File? = pdfName?.let { createTmpFileFromUri(this, uri, it) }
                    val fileExtention: String = file!!.extension

                    if (file != null) {
//                        absolutePath = file
                    }
                } else if (extension == ".docx") {
                    val file: File? = pdfName?.let { createDocxFileFromUri(this, uri, it) }
                    val fileExtention: String = file!!.extension

                    if (file != null) {
//                        absolutePath = file
                    }

                }
                else if (extension==".doc"){
                    val file: File? = pdfName?.let { createDocFileFromUri(this, uri, it) }
                    val fileExtention: String = file!!.extension

                    if (file != null) {
//                        absolutePath = file
                    }
                }
updateProfile()
                Toast.makeText(this, "Resume submitted successfully", Toast.LENGTH_SHORT)
                binding.filePath.visibility = View.GONE
                binding.linearLayout2.visibility = View.GONE
                binding.warning.visibility = View.GONE
                binding.etName.setEnabled(true)
                binding.etEmail.setEnabled(true)
                binding.etExperience.setEnabled(true)
                binding.etMobile.setEnabled(true)
                binding.etCover.setEnabled(true)
            }





        }

viewModel.errorMessage()?.observe(this,{
    showToast("Error- "+it.toString())
    val builder = AlertDialog.Builder(this)
    builder.setMessage(it.toString())
    builder.setIcon(R.drawable.recruiter_notes)
    builder.setPositiveButton("Ok"){dialog,id ->
    }
    val alertDialog: AlertDialog = builder.create()
    // Set other dialog properties
    alertDialog.setCancelable(false)
    alertDialog.show()
})


    }

    fun createTmpFileFromUri(context: Context, uri: Uri, fileName: String): File? {
        return try {
            val stream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile(fileName, ".pdf", context.cacheDir)
            org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, file)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createDocxFileFromUri(context: Context, uri: Uri, fileName: String): File? {
        return try {
            val stream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile(fileName, ".docx", context.cacheDir)
            org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, file)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createDocFileFromUri(context: Context, uri: Uri, fileName: String): File? {
        return try {
            val stream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile(fileName, ".doc", context.cacheDir)
            org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, file)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification() {

        val channelID = "com.ebookfrenzy.notifydemo.news"
        val notificationID = 101
        val notification = Notification.Builder(
            this,
            channelID
        )
            .setContentTitle("Resume Uploaded")
            .setContentText("Your resume is uploaded successfully.")
            .setSmallIcon(android.R.drawable.ic_notification_clear_all)
            .setChannelId(channelID)

//            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.edit))
            .build()
        notificationManager?.notify(notificationID, notification)
    }
    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun showPermissionRequestExplanation(
        permission: String,
        message: String,
        retry: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(this).apply {
            setTitle("$permission Required")
            setMessage(message)
            setPositiveButton("Ok") { _, _ -> retry?.invoke() }
        }.show()
    }
    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateProfile(

    ) {
//        viewModel.UpdateProfile(
//            this,
//            absolutePath,
//            binding.progress
//        ).observe(this, {
////            Log.i("dattaaaaa", it.data.toString())
//            binding.etName.setText(it.data.name)
//            binding.etEmail.setText(it.data.email)
//            binding.etMobile.setText(it.data.mobile_number)
//            sendNotification()
//
//        })

    }
//    override fun onUserInteraction() {
//        super.onUserInteraction()
//        if (currentFocus != null) {
//            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
//        }
//    }

//            Log.i("ppppp", it.message.toString())
//            val builder = AlertDialog.Builder(this)
//            builder.setMessage(it.message)
//            builder.setIcon(R.drawable.recruiter_notes)
//            builder.setPositiveButton("Ok"){dialog,id ->
//            val intent = Intent(this, Login::class.java)
//            startActivity(intent)
//                finish()
//            }
//            val alertDialog: AlertDialog = builder.create()
//            // Set other dialog properties
//            alertDialog.setCancelable(false)
//            alertDialog.show()

    //            startActivity(intent)
    private fun isValidate():Boolean=
        validatenumber()&&
                validatePrefix()&&validateUserName()&&validateDesignation()&&validateEmail()
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        id: String, name: String,
        description: String
    ) {

        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern =
            longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager?.createNotificationChannel(channel)
    }
    private fun createProfile() {
        location_string=binding.tiLocationForm.text.toString()
        fun genderid(): Int {
            if (binding.radioMr.isChecked) {
                radioId = 83
            } else if (binding.radioMrs.isChecked) {
                radioId = 84
            }
            else if (binding.radioOthers.isChecked){
                radioId = 4279
            }
            return radioId
        }
        var Gender = object {
            val id = genderid()
        }
        var genderstring: String = Gender.toString()

        var userD = object {
            var email = binding.etEmail.text.toString()
            var name = binding.etName.text.toString()
            var mobileNo = binding.etMobile.text.toString()
            var designation = binding.etCover.text.toString()
            var dob=binding.etDate!!.text.toString()+"T18:30:00.000+00:00"

        }
        val info = object {
            var gender = Gender
            var location = location_string
            var userDTO = userD
//            var resume :File = absolutePath
        }
        var gson = Gson()
        var infoJ = gson.toJson(info)

        var requestParams: String = infoJ
        viewModel.createProfile(
            this,
            requestParams,
            binding.progress,
        ).observe(this, {
            var token=it.token
            val jwt = JWT(token)
            val issuer = jwt.issuer //get registered claims
            val claim = jwt.getClaim("USER").asString() //get custom claims
            Log.i("nnnnnnnnn", claim.toString())
            LocalSessionManager.saveValue(Constant.UserID, claim.toString(), this)
            LocalSessionManager.saveValue(Constant.TOKEN, it.token, this)
            val intent = Intent(this, Signup_Education_Details::class.java)
            startActivity(intent)

        })
    }
    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
//            chooseImage(this)
        } else {
            showPermissionRequestExplanation(
                "write_storage",
                "This permission is require to update profile picture "
            ) {
                requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }
    fun validatenumber():Boolean{
        if (binding.etMobile.length()<10){
            binding.tiExp.error="Enter valid mobile number"
            return false
        }
        else if(binding.etMobile.length()==10){
            binding.tiExp.isErrorEnabled=false
        }
        return true
    }
    fun validatePrefix():Boolean{
        if (binding.radioGroup.checkedRadioButtonId==-1){
            binding.radioMr.error="fill"
            return false
        }

        return true
    }
    fun validateUserName(): Boolean {
        if (binding.etName.text.toString().trim().isEmpty()) {
            binding.tiUsername.error = "Enter Full Name!"
            return false
        } else {
            binding.tiUsername.isErrorEnabled = false
        }
        return true
    }
    fun validateEmail(): Boolean {
        val letterPattern = Regex("[a-zA-Z]")

        if (binding.etEmail.text.toString().trim().isEmpty()&&letterPattern.containsMatchIn(binding.etEmail.text.toString())==false) {
            binding.tiLocationFormOne.error = "Enter Email Address!"
            return false
        }
        else if (emailValidator(binding.etEmail)==false){
            return false
        }

        else {
            binding.tiLocationFormOne.isErrorEnabled = false
        }
        return true
    }
    fun validateDesignation(): Boolean {
        if (binding.etCover.text.toString().trim().isEmpty()) {
            binding.tiDesignationSignup.error = "Enter Designation!"
            return false
        } else {
            binding.tiDesignationSignup.isErrorEnabled = false
        }
        return true
    }
//fun validateResume():Boolean
//{
//    if (::absolutePath.isInitialized){
//        return true
//    }
//    Toast.makeText(this,"Please Upload Resume",Toast.LENGTH_SHORT).show()
//
//    return false
//}
    fun emailValidator(etMail: EditText):Boolean {

        // extract the entered data from the EditText
        val emailToText = etMail.text.toString()
        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
        } else {
            binding.tiLocationFormOne.error = "Enter a Valid Email Address!"
            return false
        }
        return true
    }




    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(this, contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        Log.i("yoooooooooo", column_index.toString())
        cursor.moveToFirst()
        val result: String = cursor.getString(column_index)
        cursor.close()
        return result
    }

}