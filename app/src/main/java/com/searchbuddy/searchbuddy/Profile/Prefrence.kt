package com.searchbuddy.searchbuddy.Profile

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.ActivityPrefrenceBinding
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.CityList
import com.searchbuddy.searchbuddy.model.UpdatePrefRequest
import java.io.IOException
import java.util.Collections


class Prefrence : AppCompatActivity() {
    private lateinit var binding: ActivityPrefrenceBinding
    lateinit var selectedLanguage: BooleanArray
    lateinit var selectedEmployment: BooleanArray
    lateinit var selectedWork: BooleanArray
    lateinit var UserID: String
    var langList: ArrayList<Int> = ArrayList()
    var empoloymentList: ArrayList<Int> = ArrayList()
    var WorkList: ArrayList<Int> = ArrayList()
    var Cityist: ArrayList<Int> = ArrayList()
    var exampleCityList: ArrayList<String> = ArrayList()
    var roleArray = arrayOf(
   "Business Development",
        "Channel Sales",
        "Direct Sales(B2B)",
        "Field Sales",
        "Govt. Sales",
        "Inside Sales",
        "Institutional Sales",
        "Sales(B2C)"
    )
    var employmentArray = arrayOf("Permanent", "Contractual", "Work from office","Hybrid")
    var workArray = arrayOf("Only Remote", "Work from Office", "Hybrid")
    lateinit var viewModel: PrefrencesViewModel
    var yo: Int = 0
    var locString: String = ""
    lateinit var woType: ArrayList<String>
    lateinit var roType: ArrayList<String>
    lateinit var locType: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrefrenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(PrefrencesViewModel::class.java)

        selectedLanguage = BooleanArray(roleArray.size)
        selectedEmployment = BooleanArray(employmentArray.size)
        selectedWork = BooleanArray(workArray.size)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.grey)
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
            val CityArray: JsonArray = JsonArray()
            val dataa: String = city.District
            CityArray.add(dataa)
            for (i in 0 until CityArray.size()) {
                var kuch = CityArray.get(i).toString()
                var result = kuch.substring(1, kuch.length - 1)
                exampleCityList.add(result)

            }
        }
        UserID = LocalSessionManager.getStringValue(Constant.UserID, "", this).toString()
        yo = UserID.toInt()

        viewModel.requestPrefrencesDetail(this, yo, binding.progress).observe(this, {
            if (it.professionalDetails!=null) {
                if (it.professionalDetails.preferences!=null) {
                    var role = it.professionalDetails.preferences.role.toString()
                    var employement = it.professionalDetails.preferences.employementType.toString()
                    var loc = it.professionalDetails.preferences.location.toString()
                    var worktype = it.professionalDetails.preferences.employementType.toString()
                    var roleString = role.substring(1, role.length - 1)
                    var empString = employement.substring(1, employement.length - 1)
                    var LocString = loc.substring(1, loc.length - 1)
                    var workString = worktype.substring(1, worktype.length - 1)
                    binding.textViw.setText(roleString)
                    binding.textViwe2.setText(empString)
                    binding.textView3.setText(workString)
                    binding.tiLocationForm.setText(LocString)
                }
            }
        })
        binding.textViw.setOnClickListener(View.OnClickListener {
            // Initialize alert dialog
            binding.textViw.requestFocus()

            val builder = AlertDialog.Builder(this)
            // set title
            builder.setTitle("Select Role")

            // set dialog non cancelable
            builder.setCancelable(false)
            builder.setMultiChoiceItems(
                roleArray, selectedLanguage
            ) { dialogInterface, i, b ->
                // check condition
                if (b) {
                    // when checkbox selected
                    // Add position  in lang list
                    langList.add(i)
                    // Sort array list
                    Collections.sort(langList)
                } else {
                    // when checkbox unselected
                    // Remove position from langList
                    langList.remove(Integer.valueOf(i))
                }
            }
            builder.setPositiveButton(
                "OK"
            ) { dialogInterface, i -> // Initialize string builder
                val stringBuilder = StringBuilder()
                // use for loop
                for (j in 0 until langList.size) {
                    // concat array value
                    stringBuilder.append(roleArray[langList[j]])
//                    roType= arrayListOf(roleArray[langList[j]])
                    // check condition
                    if (j != langList.size - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ")
                    }
                }
                // set text on textView
                binding.textViw.setText(stringBuilder.toString())
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialogInterface, i -> // dismiss dialog
                dialogInterface.dismiss()
            }
            builder.setNeutralButton(
                "Clear All"
            ) { dialogInterface, i ->
                // use for loop
                for (j in selectedLanguage.indices) {
                    // remove all selection
                    selectedLanguage[j] = false
                    // clear language list
                    langList.clear()
                    // clear text view value
                    binding.textViw.setText("")
                }
            }
            // show dialog
            builder.show()
        })
        roType = arrayListOf(binding.textViw.toString())

        selectedLanguage = BooleanArray(roleArray.size)
        binding.textViwe2.setOnClickListener(View.OnClickListener { // Initialize alert dialog
            val builder = AlertDialog.Builder(this)

            // set title
            builder.setTitle("Select Employment Type")

            // set dialog non cancelable
            builder.setCancelable(false)
            builder.setMultiChoiceItems(
                employmentArray, selectedEmployment
            ) { dialogInterface, i, b ->
                // check condition
                if (b) {
                    // when checkbox selected
                    // Add position  in lang list
                    empoloymentList.add(i)
                    // Sort array list
                    Collections.sort(empoloymentList)
                } else {
                    // when checkbox unselected
                    // Remove position from empoloymentList
                    empoloymentList.remove(Integer.valueOf(i))
                }

            }
            builder.setPositiveButton(
                "OK"
            ) { dialogInterface, i -> // Initialize string builder
                val stringBuilder = StringBuilder()
                // use for loop
                for (j in 0 until empoloymentList.size) {
                    // concat array value
                    stringBuilder.append(employmentArray[empoloymentList[j]])


                    // check condition
                    if (j != empoloymentList.size - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ")
                    }
                }
                // set text on textView
                binding.textViwe2.setText(stringBuilder.toString())
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialogInterface, i -> // dismiss dialog
                dialogInterface.dismiss()
            }
            builder.setNeutralButton(
                "Clear All"
            ) { dialogInterface, i ->
                empoloymentList.remove(Integer.valueOf(i))
                // use for loop
                for (j in selectedEmployment.indices) {
                    // remove all selection
                    selectedEmployment[j] = false
                    // clear language list
                    empoloymentList.clear()
                    // clear text view value
                    binding.textViwe2.setText("")

                }
            }
            // show dialog
            builder.show()
        })

        selectedEmployment = BooleanArray(employmentArray.size)
        binding.textView3.setOnClickListener(View.OnClickListener { // Initialize alert dialog
            val builder = AlertDialog.Builder(this)

            // set title
            builder.setTitle("Select Work Type")

            // set dialog non cancelable
            builder.setCancelable(false)
            builder.setMultiChoiceItems(
                workArray, selectedWork
            ) { dialogInterface, i, b ->
                // check condition
                if (b) {
                    // when checkbox selected
                    // Add position  in lang list
                    WorkList.add(i)
                    // Sort array list
                    Collections.sort(WorkList)
                } else {
                    // when checkbox unselected
                    // Remove position from langList
                    WorkList.remove(Integer.valueOf(i))
                }
            }
            builder.setPositiveButton(
                "OK"
            ) { dialogInterface, i -> // Initialize string builder
                val stringBuilder = StringBuilder()
                // use for loop
                for (j in 0 until WorkList.size) {
                    // concat array value
                    stringBuilder.append(workArray[WorkList[j]])
                    woType= arrayListOf(workArray[WorkList[j]])

                    // check condition
                    if (j != WorkList.size - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ")
                    }
                }
                // set text on textView
                binding.textView3.setText(stringBuilder.toString())
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialogInterface, i -> // dismiss dialog
                dialogInterface.dismiss()
            }
            builder.setNeutralButton(
                "Clear All"
            ) { dialogInterface, i ->
                // use for loop
                for (j in selectedWork.indices) {
                    // remove all selection
                    selectedWork[j] = false
                    // clear language list
                    WorkList.clear()
                    // clear text view value
                    binding.textView3.setText("")
                }
            }
            // show dialog
            builder.show()
        })
        woType = arrayListOf(binding.textView3.toString())
        selectedWork = BooleanArray(workArray.size)
        yo = UserID.toInt()
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, exampleCityList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tiLocationForm.setAdapter(adapter)
        binding.tiLocationForm.threshold = 2
        binding.tiLocationForm.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        binding.tiLocationForm.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            val planet = parent.getItemAtPosition(position).toString()
//            LocalSessionManager.saveValue(Constant.FilterLocation, planet, this)
            locString = planet

        })
        if (locString == "") {
            var some = locString

            locType = ArrayList()
            locType.add(locString)

        }
        binding.save.setOnClickListener { UpdatePrefrence() }
        binding.cancel.setOnClickListener {
            onBackPressed()
        }
    }
//    override fun onUserInteraction() {
//        super.onUserInteraction()
//        if (currentFocus != null) {
//            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
//        }
//    }
    fun UpdatePrefrence() {
        var id = yo
        var professionalDetails = object {
            var preferences = object {
                var employementType = arrayListOf(binding.textViwe2.text.toString())
                var location = arrayListOf(binding.tiLocationForm.text.toString())
                var role = arrayListOf(binding.textViw.text.toString())
                var workType = arrayListOf(binding.textView3.text.toString())
            }
        }
        var requestParams = UpdatePrefRequest(id, professionalDetails)
        viewModel.UpdatePrefDetail(this, requestParams, binding.progress).observe(this, {
            Toast.makeText(this, "Preferences Updated", Toast.LENGTH_SHORT).show()
            onBackPressed()
        })

    }

}