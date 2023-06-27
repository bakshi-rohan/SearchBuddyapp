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
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.searchbuddy.databinding.ActivitySignupCvBinding
import com.google.gson.Gson
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.Constant.Companion.UserID
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class Signup_CV : AppCompatActivity() {
    lateinit var binding: ActivitySignupCvBinding
    lateinit var descriptionData:Array<String>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var pdfUri: Uri
    lateinit var absolutePath: File
    lateinit var files: File
    private lateinit var extension: String
    lateinit var type: String
    lateinit var profileUri: Uri
    lateinit var profileFile: File
    val REQUEST_CODE = 200
    private val CAMERA = 2
//    lateinit var UserId:String
    lateinit var name:String
    var fileName:String=""
    lateinit var Videofiles: File
    private var notificationManager: NotificationManager? = null

    lateinit var viewModel: Signup_Cv_ViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupCvBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(Signup_Cv_ViewModel::class.java)
        descriptionData= arrayOf("Basic\nDetails", "Education", "Experience", "CV","Preferences")
        binding.yourStateProgressBarId.setStateDescriptionData(descriptionData)
        binding.resumeUpload.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_OPEN_DOCUMENT)
                .setType("PDF")

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }
        binding.btnSbmt.setOnClickListener {
           Toast.makeText(this,"Please select  resume",Toast.LENGTH_SHORT).show()
        }

//        UserId = LocalSessionManager.getStringValue(Constant.UserID, "", this,).toString()
//Log.i("kk", UserID)
        binding.resumeUpload.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_OPEN_DOCUMENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }
        binding.btnSkip.setOnClickListener {
            var intent = Intent(this, Signup_prefrences::class.java)
//            intent.putExtra("userid",UserId)
            startActivity(intent)
        }

        binding.selectVideoResume.setOnClickListener {
//            requestStoragePermission()
            val builder = AlertDialog.Builder(this)

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
                chooseVideo(this)
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

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(com.bumptech.searchbuddy.R.color.grey)
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
        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            pdfUri = data?.data!!
            val uri: Uri = data?.data!!
            files = getFile(this,uri)!!
            binding.btnSbmt.setOnClickListener {
                var intent=Intent(this,Signup_prefrences::class.java)
                startActivity(intent)
            }
//            val inputStream: InputStream? = requireContext()!!.contentResolver.openInputStream(uri)
            val uriString: String = uri.toString()
            var pdfName: String? = null
            if (uriString.startsWith("content://")) {
                var myCursor: Cursor? = null
                try {
                    myCursor =
                        this.contentResolver.query(uri, null, null, null, null)
                    if (myCursor != null && myCursor.moveToFirst()) {
                        pdfName =
                            myCursor.getString(myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        name=
                            myCursor.getString(myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        LocalSessionManager.saveValue(Constant.PdfName,name,this)


                        binding.filePath.setText(pdfName)
                        Log.i("abbb",fileName)
                        extension = pdfName.substring(pdfName.lastIndexOf("."))
                        Log.i("extensionnn", extension)

                    }
                } finally {
                    myCursor?.close()
                }
                if (extension == ".pdf") {
                    val file: File? = pdfName?.let { createTmpFileFromUri(this, uri, it) }
                    val fileExtention: String = file!!.extension

                    if (file != null) {
                        absolutePath = file
                        Log.i("abbbbbbbb", absolutePath.toString())
                    }
                } else if (extension == ".docx"||extension==".doc") {
                    val file: File? = pdfName?.let { createDocxFileFromUri(this, uri, it) }
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

            if (resultCode == RESULT_OK) {
                val selectedFile = data?.data //The uri with the location of the file
                Videofiles = getFile(this, selectedFile!!)!!
//            binding.videoPath.setText("Video: " + selectedFile.toString())
                UploadVideoResume()
            } else if (resultCode == CAMERA) {
                val selectedFile = data?.data //The uri with the location of the file
                Videofiles = getFile(this, selectedFile!!)!!
                Log.i("bbbbb", Videofiles.name)
                UploadVideoResume()

//            binding.videoPath.setText("Video: " + selectedFile.toString())

            }
        }

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
    private fun queryName(context: Context, uri: Uri): String {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)!!
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
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
    fun createTmpFileFromUri(context: Context, uri: Uri, fileName: String): File? {
        return try {
            val stream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile(fileName,"")
            Log.i("xxxx",file.name)
            org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, file)
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
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
    fun chooseVideo(context: Context) {


        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setType("video/*")
        resultLauncher.launch(intent)

    }

    fun uploadImage(context: Context) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }
    private fun requestStoragePermissionCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
//            chooseImage(this)
            takeVideoFromCamera(this)
        } else {
            showPermissionRequestExplanation(
                "Camera",
                "This permission is require to update video resume "
            ) {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun UploadResume(){
//        UserId = LocalSessionManager.getStringValue(Constant.UserID, "", this).toString()

        var yo = UserID.toInt()
        Log.i("cccccccc",yo.toString())

        var info=object {
            var id=yo
        }
        var gson = Gson()
        var infoJ = gson.toJson(info)

        viewModel.UploadResume(this,files,infoJ,binding.progress).observe(this,{
            sendNotification()
        })
    }
    private fun UploadVideoResume() {
//        UserId =
//            LocalSessionManager.getStringValue(Constant.UserID, "", this).toString()

        var yo = UserID.toInt()
        var info = object {
            var id = yo
        }
        var gson = Gson()
        var infoJ = gson.toJson(info)

        viewModel.UploadVideoResume(this, Videofiles, yo, binding.progress)
            .observe(this, {
//                Toast.makeText(this, "Video resume Uploaded", Toast.LENGTH_SHORT).show()
            })
    }
}