package com.searchbuddy.searchbuddy.Profile

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.ActivityBasicDetailsBinding
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.searchbuddy.searchbuddy.Dashboard.Dashboard
import com.searchbuddy.searchbuddy.model.CityList
import java.io.File
import java.io.IOException
import java.util.Calendar

class BasicDetails : AppCompatActivity() {
    private lateinit var binding: ActivityBasicDetailsBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var profileUri: Uri
    var exampleCityList: ArrayList<String> = ArrayList()
    lateinit var profileFile: File
    var cal = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasicDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cancel.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.fresher.setOnClickListener {
            binding.fresher.setBackgroundResource(R.drawable.work_status_selected_border)
            binding.experienced.setBackgroundResource(R.drawable.work_status_border)

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
        val jsonFileString = getJsonDataFromAsset(this, "cities.json")
        val gson = Gson()
        val listCityType = object : TypeToken<List<CityList>>() {}.type
        var persons: List<CityList> = gson.fromJson(jsonFileString, listCityType)

        persons.forEachIndexed { idx, city ->
//            Log.i("dataaaaa", "> Item $idx:\n${city.Districtname}")
//             someList = person.Districtname
            val CityArray: JsonArray = JsonArray()
            val dataa: String = city.District
            CityArray.add(dataa)

            Log.i("kkkkkkkkk", city.CombinedName)
            for (i in 0 until CityArray.size()) {
//                val he = CityList("","",person.CombinedName,"","")
//                exampleList.add(exampleArray.toString())
                var kuch = CityArray.get(i).toString()
                var result = kuch.substring(1, kuch.length - 1)
                exampleCityList.add(result)

                Log.i("Arrraaaaay", exampleCityList.toString())

            }
        }

        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, exampleCityList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiLocationForm.setAdapter(adapter)
        binding.tiLocationForm.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val item = parent.getItemAtPosition(position)
               var  location_string = item.toString()
                Log.i("spinner", item.toString())


//                LocationList.add(item.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        binding.resumeUpload.setOnClickListener {
            Toast.makeText(
                this,
                "Please Select a File",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.grey)
        }
        binding.experienced.setOnClickListener {
            binding.experienced.setBackgroundResource(R.drawable.work_status_selected_border)
            binding.fresher.setBackgroundResource(R.drawable.work_status_border)
        }
        binding.male.setOnClickListener {
            binding.male.setBackgroundResource(R.drawable.work_status_selected_border)
            binding.female.setBackgroundResource(R.drawable.work_status_border)
            binding.others.setBackgroundResource(R.drawable.work_status_border)
        }
        binding.female.setOnClickListener {
            binding.female.setBackgroundResource(R.drawable.work_status_selected_border)
            binding.male.setBackgroundResource(R.drawable.work_status_border)
            binding.others.setBackgroundResource(R.drawable.work_status_border)
        }
        binding.others.setOnClickListener {
            binding.others.setBackgroundResource(R.drawable.work_status_selected_border)
            binding.male.setBackgroundResource(R.drawable.work_status_border)
            binding.female.setBackgroundResource(R.drawable.work_status_border)
        }

        binding.etDate.setOnClickListener {
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    binding.etDate.setText(dat)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
        }
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    profileUri = data!!.data!!
                    profileFile = File(getRealPathFromURI(profileUri))
                    val filePath: String = profileFile.path
//                    binding.videoPath.setText(filePath.toString())

//                        binding.secondUserImg.setImageBitmap(bitmap)


                }
            }
        binding.chooseResume.setOnClickListener {

            val intent = Intent()
                .setType("application/pdf")
                .setAction(Intent.ACTION_OPEN_DOCUMENT)



            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            binding.filePath.setText("File: " + selectedFile.toString())

            binding.resumeUpload.setOnClickListener {
                Toast.makeText(
                    this,
                    "Resume Uploaded Successfully",
                    Toast.LENGTH_LONG
                ).show()

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

}
