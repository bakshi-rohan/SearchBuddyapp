package com.example.searchbuddy.Profile

import android.R
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.searchbuddy.Dashboard.Dashboard
import com.example.searchbuddy.Utils.Constant
import com.example.searchbuddy.Utils.LocalSessionManager
import com.example.searchbuddy.databinding.ActivityPersonalDetailsBinding
import com.example.searchbuddy.model.CityList
import com.example.searchbuddy.model.Edu.BasicDetailUpdateRequest
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
import java.util.*

class PersonalDetails : AppCompatActivity() {
    private lateinit var binding: ActivityPersonalDetailsBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var profileUri: Uri
    var exampleCityList: ArrayList<String> = ArrayList()
    lateinit var profileFile: File
    var cal = Calendar.getInstance()
    lateinit var viewModel: PersonalDetailViewModel
    lateinit var UserID: String
    lateinit var genderValue: String
    var genderid:Int=0
    var yo:Int=0
    lateinit var mm:String
    lateinit var dd:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PersonalDetailViewModel::class.java)

        binding = ActivityPersonalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cancel.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }
        binding.etEmail.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    //Do something when EditText has focus
                    binding.etEmail.setError(null)
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
                    validateMobile()
//                    val imm: InputMethodManager =
//                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                }
            }
        })
        binding.etDesg.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    //Do something when EditText has focus
                    binding.etDesg.setError(null)
                } else {
                    // Do something when Focus is not on the EditText
                    validateDesg()
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
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.tiEmail.setError(null)
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
        binding.etDesg.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.tiDesg.setError(null)
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

                binding.tiName.setError(null)
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
        binding.etDate.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.tiDate.setError(null)
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

                binding.tiNumber.setError(null)
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
            val dataa: String = city.CombinedName
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
            ArrayAdapter(this, R.layout.simple_list_item_1, exampleCityList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiLocationForm.setAdapter(adapter)
//        binding.tiLocationForm.setOnItemSelectedListener(object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View,
//                position: Int,
//                id: Long
//            ) {
//                val item = parent.getItemAtPosition(position)
//                var location_string = item.toString()
////                Log.i("spinner", item.toString())
//
//
////                LocationList.add(item.toString())
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        })
        binding.save.setOnClickListener{
            if (isValidate()){
                UpdateDetail()
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor =
                this.resources.getColor(com.example.searchbuddy.R.color.orange)
        }
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

         yo = UserID.toInt()
        Log.i("idddd",yo.toString())
        viewModel.requestPersonalDetail(this, yo, binding.progress).observe(this, {
//            Log.i("bbbb", it.toString())
            if (it!=null) {
                if (it.userDTO!=null) {
                    if (it.userDTO.name!=null) {
                        binding.etName.setText(it.userDTO.name)
                    }
                    if (it.userDTO.mobileNo!=null) {
                        binding.etNumber.setText(it.userDTO.mobileNo)
                    }
                    if (it.dob!=null) {
                        var date = it.dob
                        var outputDate = date.substring(0, 10);
                        binding.etDate.setText(outputDate)
                    }
                   if (it.userDTO.email!=null) {
                       binding.etEmail.setText(it.userDTO.email)
                   }
                    if (it.userDTO.designation!=null) {
                        binding.etDesg.setText(it.userDTO.designation)
                    }
                }
                if (it.gender!=null) {
                    if (it.gender.id == 83) {
                        binding.radioMr.isChecked = true
                    } else if (it.gender.id == 84) {
                        binding.radioMs.isChecked = true
                    }
                }
                if (it.differentlyAbled!=null) {
                    if (it.differentlyAbled == false) {
                        binding.radioNo.isChecked = true
                    } else if (it.differentlyAbled == true) {
                        binding.radioYes.isChecked = true
                    }
                }
                if (it.location!=null){
                var locationValue = it.location
                if (locationValue != null) {
//                    var spinnerPosition: Int = adapter.getPosition(locationValue)
                    binding.tiLocationForm.setText(locationValue)
                }
                }
                LocalSessionManager.saveValue(Constant.NAME, binding.etName.text.toString(), this)
                LocalSessionManager.saveValue(Constant.Mobile_no, binding.etNumber.text.toString(), this)
                LocalSessionManager.saveValue(Constant.Date, binding.etDate.text.toString(), this)
                LocalSessionManager.saveValue(Constant.EMAIL, binding.etEmail.text.toString(), this)
                LocalSessionManager.saveValue(Constant.Designation, binding.etDesg.text.toString(), this)
                LocalSessionManager.saveValue(Constant.Male, binding.radioMr.isChecked, this)
                LocalSessionManager.saveValue(Constant.Female, binding.radioMs.isChecked, this)
            }
        })


    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    fun validateUserName(): Boolean {
        if (binding.etName.text.toString().trim().isEmpty()) {
            binding.tiName.error = "Enter User Name!"
            return false
        } else {
            binding.tiName.isErrorEnabled = false
        }
        return true
    }
    fun validateMobile(): Boolean {
        if (binding.etNumber.length()<10){
            binding.tiNumber.error="Enter valid mobile number"
            return false
        }
        else if(binding.etNumber.length()==10){
            binding.tiNumber.isErrorEnabled=false
        }
        return true
    }
    fun validateDesg(): Boolean {
        if (binding.etDesg.text.toString().trim().isEmpty()) {
            binding.tiDesg.error = "Enter Designation"
            return false
        } else {
            binding.tiDesg.isErrorEnabled = false
        }
        return true
    }
    fun validatedob(): Boolean {
        if (binding.etDate.text.toString().trim().isEmpty()) {
            binding.tiDate.error = "Enter D.O.B!"
            return false
        } else {
            binding.tiDate.isErrorEnabled = false
        }
        return true
    }
    fun validateemail(): Boolean {
        if (binding.etEmail.text.toString().trim().isEmpty()) {
            binding.tiEmail.error = "Enter Email"
            return false
        }
        else if (emailValidator(binding.etEmail)==false){
            return false
        }
        else {
            binding.tiEmail.isErrorEnabled = false
        }
        return true
    }
    fun emailValidator(etMail: EditText):Boolean {

        // extract the entered data from the EditText
        val emailToText = etMail.text.toString()

        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
        } else {
            binding.tiEmail.error = "Enter a Valid Email!"
            return false
        }
        return true
    }

    private fun isValidate(): Boolean =
        validateUserName() && validateDesg()&&validateemail()&&validatedob()&&validateMobile()
    private fun UpdateDetail(){
        var diffentlabled:Boolean=true
        if (binding.radioYes.isChecked==true){
            diffentlabled=true
        }
        else if(binding.radioNo.isChecked==true){
            diffentlabled=false
        }
        var dob=binding.etDate!!.text.toString()+"T18:30:00.000+00:00"
        genderValue=""
        if (binding.radioMr.isChecked==true){
            genderid=83
            genderValue="Male"
        }
        else if (binding.radioMs.isChecked==true){
            genderid=84
            genderValue="Female"
        }
        else if (binding.radioOthers.isChecked==true){
            genderid=4279
            genderValue="Others"
        }

        var Gender = object {
            var id=genderid
            var value=genderValue
            var codeValueType = object {
                var id=yo
                var name="gender"
            }
        }
         yo = UserID.toInt()
        var id=yo
        var location=binding.tiLocationForm.text.toString()
        var UserDto = object {
            var designation=binding.etDesg!!.text.toString()
            var email=binding.etEmail!!.text.toString()
            var mobileNo=binding.etNumber!!.text.toString()
            var name=binding.etName!!.text.toString()
        }

        var requestParams=BasicDetailUpdateRequest(diffentlabled, dob, Gender,id,location,UserDto)
        viewModel.UpdatePersonalDetail(this,requestParams,binding.progress).observe(this,{
            LocalSessionManager.saveValue(Constant.NAME, binding.etName.text.toString(), this)
            LocalSessionManager.saveValue(Constant.Mobile_no, binding.etNumber.text.toString(), this)
            LocalSessionManager.saveValue(Constant.Date, binding.etDate.text.toString(), this)
            LocalSessionManager.saveValue(Constant.EMAIL, binding.etEmail.text.toString(), this)
            LocalSessionManager.saveValue(Constant.Designation, binding.etDesg.text.toString(), this)
            LocalSessionManager.saveValue(Constant.Male, binding.radioMr.isChecked, this)
            LocalSessionManager.saveValue(Constant.Female, binding.radioMs.isChecked, this)

            Toast.makeText(this,"Details Updated Sucessfully",Toast.LENGTH_SHORT).show()
            onBackPressed()

        })
    }

}