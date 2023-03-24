package com.example.searchbuddy.Forms

import android.R
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.searchbuddy.Utils.Constant
import com.example.searchbuddy.Utils.Constant.Companion.UserID
import com.example.searchbuddy.Utils.LocalSessionManager
import com.example.searchbuddy.databinding.ActivitySignupWorkHistoryBinding
import com.example.searchbuddy.model.FunctionList
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.util.*


class Signup_Work_History : AppCompatActivity() {
    lateinit var binding: ActivitySignupWorkHistoryBinding
    var exampleCityList: ArrayList<String> = ArrayList()
    lateinit var noticePeriodList: ArrayList<String>
    lateinit var descriptionData: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupWorkHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tiEnddateWork.setVisibility(View.GONE)
        binding.btnSbmt.setOnClickListener {

            var intent = Intent(this, Signup_CV::class.java)
            startActivity(intent)
        }
        binding.btnSkip.setOnClickListener {
            var intent = Intent(this, Signup_prefrences::class.java)
            startActivity(intent)
        }
        descriptionData =
            arrayOf("Basic\nDetails", "Educational\nDetails", "Experience", "CV", "Preferences")
        binding.yourStateProgressBarId.setStateDescriptionData(descriptionData)
        var radioYes = binding.radioYesWork
        var radioNo = binding.radioNoWork
        var radioGroup = binding.radioLayout
        radioYes.isChecked = true
        radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                // TODO Auto-generated method stub
                if (radioNo.isChecked() === true) {
                    binding.tiEnddateWork.setVisibility(View.VISIBLE)
                } else {
                    binding.tiEnddateWork.setVisibility(View.GONE)
                }
            }
        })
        noticePeriodList = arrayListOf(
            "Select Notice Period",
            "Immediate",
            "15 days",
            "30 days",
            "45 days",
            "60 days",
            "90 days"
        )
        binding.etDateWork.setOnClickListener {
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
                    val dat =
                        (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
                    binding.etDateWork.setText(dat)
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
            datePickerDialog.datePicker.setMaxDate(System.currentTimeMillis())
        }
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(com.example.searchbuddy.R.color.orange)
        }
        binding.etEnddateWork.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val date =
                        (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
                    binding.etEnddateWork.setText(date)
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
            datePickerDialog.datePicker.setMaxDate(System.currentTimeMillis())
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

        val jsonFileString = getJsonDataFromAsset(this, "functions.json")
        val gson = Gson()
        val listCityType = object : TypeToken<List<FunctionList>>() {}.type
        var persons: List<FunctionList> = gson.fromJson(jsonFileString, listCityType)
        persons.forEachIndexed { idx, func ->
//            Log.i("dataaaaa", "> Item $idx:\n${func.Value}")
//             someList = person.Districtname
            val CityArray: JsonArray = JsonArray()
            val dataa: String = func.Value
            CityArray.add(dataa)

//            Log.i("kkkkkkkkk", city.CombinedName)
            for (i in 0 until CityArray.size()) {
//                val he = CityList("","",person.CombinedName,"","")
//                exampleList.add(exampleArray.toString())
                var kuch = CityArray.get(i).toString()
                var result = kuch.substring(1, kuch.length - 1)
                exampleCityList.add(result)

//                Log.i("Arrraaaaay", exampleCityList.toString())

            }
        }
        UserID = LocalSessionManager.getStringValue(Constant.UserID, "", this).toString()

        val adapter =
            ArrayAdapter(this, R.layout.simple_spinner_item, exampleCityList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiFunctionForm.setAdapter(adapter)
        binding.tiFunctionForm.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    (view as TextView).setTextColor(Color.parseColor("#808080"))
                }
                val item = parent.getItemAtPosition(position)
                var location_string = item.toString()
//                Log.i("spinner", item.toString())


//                LocationList.add(item.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        val noticeAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, noticePeriodList)
        noticeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiNotice.setAdapter(noticeAdapter)
        binding.tiNotice.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    (view as TextView).setTextColor(Color.parseColor("#808080"))
                }
                val item = parent.getItemAtPosition(position)
                var location_string = item.toString()
//                Log.i("spinner", item.toString())


//                LocationList.add(item.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

    }

    fun validatefunction(): Boolean {
        if (binding.tiFunctionForm.selectedItem.toString() == "Select Function") {
            Toast.makeText(this, "Please select function", Toast.LENGTH_SHORT)
            return false
        } else {
        }
        return true
    }

    private fun isValidate(): Boolean =
        validatefunction()
}