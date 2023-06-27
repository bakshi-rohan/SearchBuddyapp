package com.searchbuddy.searchbuddy

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.ActivityProfileSettingBinding
import com.google.android.material.imageview.ShapeableImageView
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.Executors


class ProfileSetting : AppCompatActivity() {
    lateinit var binding: ActivityProfileSettingBinding


    lateinit var imageView: ShapeableImageView
    lateinit var camera_button: ShapeableImageView
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var type: String
    lateinit var profileUri: Uri
    lateinit var profileFile: File
    val REQUEST_CODE = 200
    lateinit var viewModel: ProfileSettingViewModel
    lateinit var UserID: String
    var yo: Int = 0
     var filename: String=""
//    lateinit var Picname:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileSettingViewModel::class.java)
        binding = ActivityProfileSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.grey)
        }
        binding.etEmailSetting.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    //Do something when EditText has focus
                    binding.etEmailSetting.setError(null)
                } else {
                    // Do something when Focus is not on the EditText
                    validateemail()
//                    val imm: InputMethodManager =
//                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                }
            }
        })
        binding.etNumber.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    //Do something when EditText has focus
                    binding.etNumber.setError(null)
                } else {
                    // Do something when Focus is not on the EditText
                    validatenumber()
//                    val imm: InputMethodManager =
//                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                }
            }
        })
        binding.etName.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    //Do something when EditText has focus
                    binding.etName.setError(null)
                } else {
                    // Do something when Focus is not on the EditText
                    validateUserName()
//                    val imm: InputMethodManager =
//                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                }
            }
        })
        binding.etDesgSetting.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    //Do something when EditText has focus
                    binding.etDesgSetting.setError(null)
                } else {
                    // Do something when Focus is not on the EditText
                    validateDesignation()
//                    val imm: InputMethodManager =
//                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                }
            }
        })
        binding.etEmailSetting.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.email.setError(null)
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
        binding.etNumber.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.etNumber.setError(null)
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
        binding.etDesgSetting.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.etDesgSetting.setError(null)
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
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.etName.setError(null)
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
        UserID = LocalSessionManager.getStringValue(Constant.UserID, "", this).toString()
        yo = UserID.toInt()
        viewModel.requestProfileDetail(this, yo, binding.progress).observe(this, {
            binding.etDesgSetting.setText(it.designation)
            binding.etNumber.setText(it.mobileNo)
            binding.etName.setText(it.name)
            binding.etEmailSetting.setText(it.email)
            if (it.profilePicName!=null) {
                filename = it.profilePicName
                Log.i("profile",filename)
            }
            val executor = Executors.newSingleThreadExecutor()
            var image: Bitmap? = null
            val handler = Handler(Looper.getMainLooper())
            executor.execute {
                val imageUrl =
                    "https://www.searchbuddy.in/api/get-picture/profilepicture/" + filename
                try {
                    val `in` = java.net.URL(imageUrl).openStream()
                    image = BitmapFactory.decodeStream(`in`)
                    if (image != null) {

                        handler.post {
                            binding.imProfilePic.setImageBitmap(image)
                        }
                    }

                }

                catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })
//        getImage()

        binding.llPswrdManage.visibility = View.GONE
        binding.searchbuddy.setBackgroundResource(R.drawable.work_status_selected_border)
        binding.searchbuddy.setOnClickListener {
            binding.searchbuddy.setBackgroundResource(R.drawable.work_status_selected_border)
            binding.candidate.setBackgroundResource(R.drawable.work_status_border)
            binding.llPswrdManage.visibility = View.GONE
            binding.rl.visibility = View.VISIBLE
            binding.llProfileSettingDetail.visibility = View.VISIBLE
        }
        binding.candidate.setOnClickListener {
            binding.candidate.setBackgroundResource(R.drawable.work_status_selected_border)
            binding.searchbuddy.setBackgroundResource(R.drawable.work_status_border)
            binding.llProfileSettingDetail.visibility = View.GONE
            binding.rl.visibility = View.GONE
            binding.llPswrdManage.visibility = View.VISIBLE
        }

//        binding.psSv.setOnTouchListener(object : OnSwipeTouchListener(this) {
//
//            override fun onSwipeRight() {
//                super.onSwipeRight()
//                binding.candidate.setBackgroundResource(R.drawable.work_status_selected_border)
//                binding.searchbuddy.setBackgroundResource(R.drawable.work_status_border)
//                binding.llProfileSettingDetail.visibility = View.GONE
//                binding.rl.visibility = View.GONE
//                binding.llPswrdManage.visibility = View.VISIBLE
//
//            }
//
//            override fun onSwipeLeft() {
//                super.onSwipeLeft()
//                binding.searchbuddy.setBackgroundResource(R.drawable.work_status_selected_border)
//                binding.candidate.setBackgroundResource(R.drawable.work_status_border)
//                binding.llPswrdManage.visibility = View.GONE
//                binding.rl.visibility = View.VISIBLE
//                binding.llProfileSettingDetail.visibility = View.VISIBLE
//            }
//
//
//        })
        binding.updateDetail.setOnClickListener {
            if (isValidate()) {
                UploadDetails()
            }
        }
        binding.updatePswrd.setOnClickListener {
            changePassword()
        }
        imageView = binding.imProfilePic
        camera_button = binding.cameraImg
        camera_button.setOnClickListener {
            val builder = AlertDialog.Builder(this)

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
//                    var ki=saveBitmapToFile(profileFile)
//                    Log.i("llllll", ki?.length().toString())
                    val filePath: String = profileFile.path
                    val bitmap = BitmapFactory.decodeFile(filePath)
//                    filename=profileFile.name
//                    Log.i("kkkkkk",filename)
                    Glide.with(this).load(profileFile).apply(
                        RequestOptions()
                            .placeholder(R.drawable.profile_placeholder)
                    ).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imProfilePic)

                }

            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            var photo :Bitmap= data.getExtras()?.get("data") as Bitmap
            imageView.setImageBitmap(photo)
//            var some= data.extras?.get("data") as Bitmap
//            profileFile=some
            var tempUri:Uri= getImageUri(getApplicationContext(), photo)
            profileFile=File(getRealPathFromURI(tempUri)!!)
        }
    }
    fun emailValidator(etMail: EditText):Boolean {

        // extract the entered data from the EditText
        val emailToText = etMail.text.toString()

        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
        } else {
            binding.email.error = "Enter a Valid Email Address!"
            return false
        }
        return true
    }
    private fun isValidate(): Boolean = validateemail()&&validatenumber()&&validateUserName()&&validateDesignation()
    fun validateemail(): Boolean {
        if (binding.etEmailSetting.text.toString().trim().isEmpty()) {
            binding.email.error = "Enter Email"
            return false
        }
        else if (emailValidator(binding.etEmailSetting)==false){
            return false
        }
        else {
            binding.email.isErrorEnabled = false
        }
        return true
    }
    fun validatenumber():Boolean{
        if (binding.etNumber.length()<10){
            binding.tiNumber.error="Enter valid mobile number"
            return false
        }
        else if(binding.etNumber.length()==10){
            binding.tiNumber.isErrorEnabled=false
        }
        return true
    }
    fun validateUserName(): Boolean {
        if (binding.etName.text.toString().trim().isEmpty()) {
            binding.tiName.error = "Enter Full Name!"
            return false
        } else {
            binding.tiName.isErrorEnabled = false
        }
        return true
    }
    fun validateDesignation(): Boolean {
        if (binding.etDesgSetting.text.toString().trim().isEmpty()) {
            binding.desg.error = "Enter Designation!"
            return false
        } else {
            binding.desg.isErrorEnabled = false
        }
        return true
    }
        private fun getImageUri( inContext:Context,  inImage:Bitmap): Uri {
        var bytes: ByteArrayOutputStream = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        var path: String = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        );
        return Uri.parse(path);
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            chooseImage(this)
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
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            uploadImage(this)
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
        val loader = CursorLoader(this, contentUri, proj, null, null, null)
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
        AlertDialog.Builder(this).apply {
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

    private fun UploadDetails() {
        var requestParams = object {
            var id = yo
            var name = binding.etName.text.toString()
            var email = binding.etEmailSetting.text.toString()
            var mobileNo = binding.etNumber.text.toString()
            var isActive = true
            var designation = binding.etDesgSetting.text.toString()
            var updatedBy = yo
        }
               if (::profileFile.isInitialized)
                {
            viewModel.createProfile(this, profileFile, requestParams, binding.progress)
                .observe(this, {
                    Toast.makeText(this,"Profile Updated Succesfully",Toast.LENGTH_SHORT).show()
                    Log.i("hhhhhhh", it.toString())

                })
        }
        else{
            viewModel.createProfilewithoutImage(this,requestParams,binding.progress).observe(this,{
                Toast.makeText(this,"Profile Updated Succesfully",Toast.LENGTH_SHORT).show()

            })
        }

    }
//    override fun onUserInteraction() {
//        super.onUserInteraction()
//        if (currentFocus != null) {
//            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
//        }
//    }
    private fun changePassword() {
        var list: ArrayList<String>
        list = ArrayList()
        var oldpass = binding.etEmailSetting!!.text.toString()
        var currentpass = binding.etOldPassword!!.text.toString()
        var cnfrmpass = binding.etNewPassword!!.text.toString()
        list.add(oldpass)
        list.add(currentpass)
        list.add(cnfrmpass)
        var resetparams: ArrayList<String> = list
        viewModel.requestPassChange(this, resetparams, binding.progress).observe(this, {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })
    }

    fun getImage() {
//        Picname="PR_094122044124"


//        viewModel.getImage(this,Picname,binding.progress).observe(this,{
//            Log.i("llll",it.toString())
//
//            Toast.makeText(this,"hulle hulare",Toast.LENGTH_LONG).show()
//        })
    }
    fun saveBitmapToFile(file: File): File? {
        return try {

            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image
            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val REQUIRED_SIZE = 75

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE
            ) {
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)
            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()

            // here i override the original image file
            file.createNewFile()
            val outputStream = FileOutputStream(file)
            selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            file
        } catch (e: java.lang.Exception) {
            null
        }
    }
}