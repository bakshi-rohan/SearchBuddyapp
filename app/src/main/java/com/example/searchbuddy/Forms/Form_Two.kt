package com.example.searchbuddy.Forms

import android.app.AlertDialog
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.searchbuddy.Login.Login
import com.example.searchbuddy.R
import com.example.searchbuddy.databinding.ActivityFormTwoBinding
import com.example.searchbuddy.model.CityList
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException

class Form_Two : AppCompatActivity() {
    lateinit var binding: ActivityFormTwoBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var profileUri: Uri
    lateinit var profileFile: File
    private val GALLERY = 1
    private val CAMERA = 2
    private var someList :ArrayList<String> =ArrayList()
    val exampleList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSubmit.setOnClickListener {
            val intent = Intent(this, Form_three::class.java)
            startActivity(intent)
        }

        val jsonFileString = getJsonDataFromAsset(applicationContext, "cities.json")
        if (jsonFileString != null) {
        }

        val gson = Gson()
        val listCityType = object : TypeToken<List<CityList>>() {}.type
        var persons: List<CityList> = gson.fromJson(jsonFileString, listCityType)
        persons.forEachIndexed { idx, city -> Log.i("dataaaaa", "> Item $idx:\n${city.Districtname}")
//             someList = person.Districtname
            val CityArray : JsonArray = JsonArray()
            val dataa : String = city.CombinedName

            CityArray.add(dataa)

            Log.i("kkkkkkkkk",city.CombinedName)
            for (i in 0 until CityArray.size()) {
//                val he = CityList("","",person.CombinedName,"","")
//                exampleList.add(exampleArray.toString())
                var kuch = CityArray.get(i).toString()
                var result = kuch.substring(1,kuch.length-1)
                exampleList.add(result)
                Log.i("Arrraaaaay",exampleList.toString())

            }
        }


val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,exampleList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiLocationForm.setAdapter(adapter)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.orange)
        }

        binding.buttonAdd.setOnClickListener(View.OnClickListener {
            addNewChip()
        })
        binding.btnSkip.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }


        binding.selectVideoResume.setOnClickListener {
//            requestStoragePermission()
            val builder = AlertDialog.Builder(this)

            builder.setMessage("Choose Video from")

            builder.setPositiveButton(
                "Camera "
            ) { dialog, id ->
                requestStoragePermission()
            }

            //set positive button
            builder.setNegativeButton(
                "Gallery"
            ) { dialog, id ->
                chooseImage(this)
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


    }

    fun takeVideoFromCamera(context: Context) {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, CAMERA)

    }

    private fun startActivityForResult(intent: Intent, camera: String) {

    }

    fun chooseImage(context: Context) {


        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setType("video/*")
        resultLauncher.launch(intent)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            binding.videoPath.setText("Video: " + selectedFile.toString())

        } else if (resultCode == CAMERA) {
            val selectedFile = data?.data //The uri with the location of the file
            binding.videoPath.setText("Video: " + selectedFile.toString())

        }
//
//
    }

    private fun requestStoragePermission() {
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

    private fun addNewChip(): Boolean {
        val keyword: String = binding.editTextKeyword.text.toString()
        if (binding.editTextKeyword.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter Your Skills!", Toast.LENGTH_SHORT).show();
            binding.editTextKeyword.requestFocus()
            return false
        } else {
            binding.editTextKeyword.isActivated = false

        }
        try {
            val chipGroup: ChipGroup = binding.chipGroup
            val layoutInflater: LayoutInflater = LayoutInflater.from(this)
            val newChip: Chip =
                LayoutInflater.from(this).inflate(R.layout.layout_chip_entry, null) as Chip
            newChip.setText(keyword)
            chipGroup.addView(newChip)
            newChip.setOnCloseIconClickListener {
                chipGroup.removeView(newChip)
            }
            binding.editTextKeyword.setText("")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true

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

}


//