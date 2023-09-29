package com.searchbuddy.searchbuddy.Profile

import android.R
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.searchbuddy.databinding.ActivityProfessionalDetailsBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.searchbuddy.searchbuddy.Adapter.WorkHistoryAdapter
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.FunctionList
import com.searchbuddy.searchbuddy.model.GetAllWorkHistoryModelItem
import com.searchbuddy.searchbuddy.model.IndustryListItem
import java.io.File
import java.io.IOException


class ProfessionalDetails : AppCompatActivity() {
    private lateinit var binding: ActivityProfessionalDetailsBinding
    var exampleCityList: ArrayList<String> = ArrayList()
    var exampleIndustryList: ArrayList<String> = ArrayList()
    var exampleLevelList: ArrayList<String> = ArrayList()
    lateinit var viewModel: ProfessionalDetailViewModel
    lateinit var UserId: String
    lateinit var position_list: ArrayList<GetAllWorkHistoryModelItem>
    lateinit var myadapter: WorkHistoryAdapter
    lateinit var WorkHistoryRecyler: RecyclerView
    var yo: Int = 0
    var chipentry:String=""
    lateinit var chiplist:ArrayList<String>
    lateinit var noticePeriodList:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfessionalDetailViewModel::class.java)
        binding = ActivityProfessionalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
position_list=ArrayList()
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor =
                this.resources.getColor(com.bumptech.searchbuddy.R.color.grey)
        }
        binding.etSalary.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    //Do something when EditText has focus
                    binding.etSalary.setError(null)
                } else {
                    // Do something when Focus is not on the EditText
                    validatesalary()
//                    val imm: InputMethodManager =
//                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                }
            }
        })
        binding.etExp.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    //Do something when EditText has focus
                    binding.etExp.setError(null)
                } else {
                    // Do something when Focus is not on the EditText
                    validateexp()
//                    val imm: InputMethodManager =
//                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                }
            }
        })
//        binding.etSummary.setOnFocusChangeListener(object : View.OnFocusChangeListener {
//            override fun onFocusChange(v: View?, hasFocus: Boolean) {
//                if (hasFocus) {
//                    //Do something when EditText has focus
//                    binding.etSummary.setError(null)
//                } else {
//                    // Do something when Focus is not on the EditText
//                    validateSummary()
////                    val imm: InputMethodManager =
////                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
////                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
//                }
//            }
//        })
        binding.etExpecSal.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    //Do something when EditText has focus
                    binding.etExpecSal.setError(null)
                } else {
                    // Do something when Focus is not on the EditText
                    validateExpecsal()
//                    val imm: InputMethodManager =
//                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                }
            }
        })
        binding.etSalary.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.tiSalary.setError(null)
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
        binding.etExp.addTextChangedListener(object : TextWatcher {
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
        binding.etSummary.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.tiSummary.setError(null)
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
        binding.etExpecSal.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                binding.tiExpecSal.setError(null)
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
        WorkHistoryRecyler = binding.workHistoryRecycler
        WorkHistoryRecyler.layoutManager = LinearLayoutManager(this)
        binding.cancel.setOnClickListener {
            onBackPressed()
//            val intent = Intent(this, Dashboard::class.java)
//            startActivity(intent)
//            finish()
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
        noticePeriodList= arrayListOf(
            "Select Notice Period",
            "Immediate",
            "15 days",
            "30 days",
            "45 days",
            "60 days",
            "90 days"
        )
val noticeAdapter=ArrayAdapter(this,R.layout.simple_spinner_item,noticePeriodList)
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
        UserId = LocalSessionManager.getStringValue(Constant.UserID, "", this).toString()

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

        val jsonFilString = getJsonDataFromAsset(this, "industries.json")
        val gso = Gson()
        val listCityTyp = object : TypeToken<List<IndustryListItem>>() {}.type
        var person: List<IndustryListItem> = gso.fromJson(jsonFilString, listCityTyp)
        person.forEachIndexed { idx, indus ->
//            Log.i("dataaaaa", "> Item $idx:\n${indus.subIndustries}")
//             someList = person.Districtname
            val CityArray: JsonArray = JsonArray()
            val dataa: String = indus.subIndustries.toString()
            CityArray.add(dataa)

//            Log.i("kkkkkkkkk", city.CombinedName)
            for (i in 0 until CityArray.size()) {
//                val he = CityList("","",person.CombinedName,"","")
//                exampleList.add(exampleArray.toString())
                var kuch = CityArray.get(i).toString()
                var result = kuch.substring(2, kuch.length - 2)
                exampleIndustryList.add(result)

//                Log.i("Arrraaaaay", exampleIndustryList.toString())

            }
        }
        val adapte =
            ArrayAdapter(this, R.layout.simple_spinner_item, exampleIndustryList)

        adapte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiIndustryForm.setAdapter(adapte)
        binding.tiIndustryForm.setOnItemSelectedListener(object :
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

        exampleLevelList = ArrayList()
        exampleLevelList.add("Level")
        exampleLevelList.add("Fresher(from Campus)")
        exampleLevelList.add("Lateral(1-3 years)")
        exampleLevelList.add("Team leader")
        exampleLevelList.add("Managerial")
        exampleLevelList.add("Function head")
        exampleLevelList.add("CXO")
        exampleLevelList.add("CEO/MD/Chairman")
        var level_adapter = ArrayAdapter(this, R.layout.simple_spinner_item, exampleLevelList)
        level_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiLevelForm.setAdapter(level_adapter)
        binding.tiLevelForm.setOnItemSelectedListener(object :
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
//                Log.i("Level", item.toString())


//                LocationList.add(item.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        binding.button.setOnClickListener {
            var intent = Intent(this, AddProfessionalDetail::class.java)
            startActivity(intent)
        }
binding.save.setOnClickListener {
    if (binding.tiFunctionForm.selectedItem=="Function"){
        binding.tiFunctionForm.requestFocus()
        Toast.makeText(this,"Please Choose Function",Toast.LENGTH_SHORT).show()
    }
    else if (isValidate()) {
        UpdateProfDetails()
    }
}


        //
        yo = UserId.toInt()
        viewModel.requestProfessionalDetail(this, yo, binding.progress).observe(this, {
           if (it.professionalDetails!=null) {
               binding.etExp.setText(it.experienceMonths.toString())
               if (it.function!=null) {
                   if (it.function.value != null) {
                       var functionValue = it.function.value
                       if (functionValue != null) {
                           var spinnerFunction: Int = adapter.getPosition(functionValue)
                           binding.tiFunctionForm.setSelection(spinnerFunction, true)
                       }
                   }
               }
               if (it.professionalDetails.level!=null) {
                   var levelValue = it.professionalDetails.level.value
                   var spinnerLevel: Int = level_adapter.getPosition(levelValue)
                   binding.tiLevelForm.setSelection(spinnerLevel,true)
               }

if (it.professionalDetails.industry!=null) {
    var IndustryValue = it.professionalDetails.industry

    if (IndustryValue != null) {
        var spinnerIndustry: Int = adapte.getPosition(IndustryValue)
        binding.tiIndustryForm.setSelection(spinnerIndustry, true)
    }
}
               if (it.professionalDetails.noticePeriod!=null) {
                   var NoticeValue = it.professionalDetails.noticePeriod.value

                   if (it.professionalDetails.noticePeriod.value != null && it.professionalDetails.noticePeriod.value != "") {
                       var spinnerIndustry: Int = noticeAdapter.getPosition(NoticeValue)
                       binding.tiNotice.setSelection(spinnerIndustry,true)

                   }
               }
               if (it.professionalDetails.summary != null && it.professionalDetails.summary != "") {
                   binding.etSummary.setText(it.professionalDetails.summary)
               }
               if (it.professionalDetails.annualSalary != null) {
                   binding.etSalary.setText(it.professionalDetails.annualSalary.toString())
               }
               if (it.professionalDetails.expectedSalary != null) {
                   binding.etExpecSal.setText(it.professionalDetails.expectedSalary.toString())
               }
               if (it.professionalDetails.primarySkills != null) {
                   var some:ListIterator<String> = it.professionalDetails.primarySkills.listIterator()
                   while (some.hasNext()){
                       var skill=some.next()
                       binding.editTextKeyword.setText(skill)

                           addNewChip()

                   }

               }

           }
        })
//        viewModel.requestWorkHistory(this,binding.progress).observe(this,{
//            position_list= ArrayList()
//            if (it != null) {
//                position_list = it as ArrayList<GetAllWorkHistoryModelItem>
//                if (position_list != null) {
//                    myadapter = WorkHistoryAdapter(position_list){index->deleteItem(index)}
//                    WorkHistoryRecyler.adapter = myadapter
//                    myadapter.notifyDataSetChanged()
//
//                }
//            }
//            else {
//                position_list= arrayListOf()
//            }
//        })
        binding.buttonAdd.setOnClickListener(View.OnClickListener {

            addNewChip()
        })

    }

    fun deleteItem(index:Int){
        position_list.removeAt(index)
        myadapter.setItems(position_list)
    }
//    override fun onUserInteraction() {
//        super.onUserInteraction()
//        if (currentFocus != null) {
//            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
//        }
//    }
    override fun onResume() {
        super.onResume()
        var level_adapter = ArrayAdapter(this, R.layout.simple_spinner_item, exampleLevelList)
        level_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiLevelForm.setAdapter(level_adapter)
        val adapte =
            ArrayAdapter(this, R.layout.simple_spinner_item, exampleIndustryList)

        adapte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiIndustryForm.setAdapter(adapte)
        yo = UserId.toInt()

//        viewModel.requestWorkHistory(this,binding.progress).observe(this,{
//            position_list= ArrayList()
//            if (it != null) {
//                position_list = it as ArrayList<GetAllWorkHistoryModelItem>
//                if (position_list != null) {
//                    myadapter = WorkHistoryAdapter(position_list){index->deleteItem(index)}
//                    WorkHistoryRecyler.adapter = myadapter
//                    myadapter.notifyDataSetChanged()
//
//                }
//            }
//            else {
//                position_list= arrayListOf()
//            }
//        })
    }
    fun validatesalary(): Boolean {
        if (binding.etSalary.text.toString().trim().isEmpty()) {
            binding.tiSalary.error = "Enter Annual Salary"
            return false
        } else {
            binding.tiSalary.isErrorEnabled = false
        }
        return true
    }
    fun validateexp(): Boolean {
        if (binding.etExp.text.toString().trim().isEmpty()) {
            binding.tiExp.error = "Enter Experience"
            return false
        } else {
            binding.tiExp.isErrorEnabled = false
        }
        return true
    }
//    fun validatenotice(): Boolean {
//        if (binding.tiNotice.selectedItem.toString().trim().isEmpty()) {
//            binding.tiNotice.error = "Enter Designation"
//            binding.etNotice.requestFocus()
//            return false
//        } else {
//            binding.tiNotice.isErrorEnabled = false
//        }
//        return true
//    }
    fun validateSummary(): Boolean {
        if (binding.etSummary.text.toString().trim().isEmpty()) {
            binding.tiSummary.error = "Enter Designation"
            return false
        } else {
            binding.tiSummary.isErrorEnabled = false
        }
        return true
    }
    fun validateExpecsal(): Boolean {
        if (binding.etExpecSal.text.toString().trim().isEmpty()) {
            binding.tiExpecSal.error = "Enter Expected Salary!"
            return false
        } else {
            binding.tiExpecSal.isErrorEnabled = false
        }
        return true
    }
  private  fun isValidate(): Boolean =
        validatesalary() &&validateexp()&&validateExpecsal()
    private fun addNewChip(): Boolean {
        val keyword: String = binding.editTextKeyword.text.toString()
        if (binding.editTextKeyword.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter Your Skills!", Toast.LENGTH_SHORT)
                .show();
            binding.editTextKeyword.requestFocus()
            return false
        } else {
            binding.editTextKeyword.isActivated = false

        }

        try {
            val chipGroup: ChipGroup = binding.chipGroup
            val layoutInflater: LayoutInflater = LayoutInflater.from(this)
            val newChip: Chip = LayoutInflater.from(this)
                .inflate(com.bumptech.searchbuddy.R.layout.layout_chip_entry, null) as Chip
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

    private fun UpdateProfDetails(){
        for (i in 0 until binding.chipGroup.getChildCount()) {
             chipentry = (binding.chipGroup.getChildAt(i) as Chip).text.toString()
        }
//        binding?.chipGroup?.children
//            ?.toList()
//            ?.filter { (it as Chip).isChecked }
//            ?.joinToString(", ") { (it as Chip).text }
        chiplist=ArrayList()
        for (i in 0 until binding.chipGroup.getChildCount()) {
            val email = (binding.chipGroup.getChildAt(i) as Chip).text.toString()
            chiplist.add(email)
        }
        Log.i("Chips",chiplist.toString())
         var id=10
        if (binding.tiFunctionForm.selectedItem.toString()=="Select Category"){
id=10
        }else if (binding.tiFunctionForm.selectedItem.toString()=="Channel Sales and P&L"){
            id=13
        }
        else if(binding.tiFunctionForm.selectedItem.toString()=="Product/ Service Sale"){
            id=12
        }
        else if (binding.tiFunctionForm.selectedItem.toString()=="Sales and Marketing Operations"){
            id=14
        }
        else if (binding.tiFunctionForm.selectedItem.toString()=="Sales Process and Enablemen"){
            id=15
        }
        else if (binding.tiFunctionForm.selectedItem.toString()=="Sales Strategy"){
            id=11
        }
        else if (binding.tiFunctionForm.selectedItem.toString()=="Other"){
            id=4276
        }
        var path: File? =null
        var Salary = binding.etSalary!!.text.toString()
        var expecSalary=binding.etExpecSal!!.text.toString()
        var exp=binding.etExp!!.text.toString()

        var info=object {

            var id = yo
            var experienceMonths =exp.toInt()
            var function = object {
                var id = id
                var codeValueType = object {
                    var id = 3
                    var name = "function"
                }
                var value = binding.tiFunctionForm!!.selectedItem.toString()
            }
            var professionalDetails = object {
                var annualSalary:Int=Salary.toInt()
                var level = object {
                    var id = 951
                    var value = binding.tiLevelForm!!.selectedItem.toString()
                }
                var summary = binding.etSummary!!.text.toString()
                var industry = binding.tiIndustryForm!!.selectedItem.toString()
                var expectedSalary = expecSalary.toInt()
                var noticePeriod = object {
                    var id = 110
                    var value = binding.tiNotice!!.selectedItem.toString()
                }
                var primarySkills = chiplist
                var workHistory = position_list
            }
        }
        var gson = Gson()
        var information = gson.toJson(info)
        var requestParams: String = information
        var infostr=information.replace("\\u003d",":")

        viewModel.UpdateProfessionalDetail(this,infostr, "",binding.progress).observe(this,{
            onBackPressed()
//            Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
        })
    }


}