package com.searchbuddy.searchbuddy.Forms

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.searchbuddy.databinding.ActivityFormThreeBinding
import com.searchbuddy.searchbuddy.Adapter.EducationFieldAdapter
import com.searchbuddy.searchbuddy.Adapter.ExperienceFieldAdapter
import com.searchbuddy.searchbuddy.Login.Login
import com.searchbuddy.searchbuddy.model.EducationFieldModel
import com.searchbuddy.searchbuddy.model.ExperienceFieldModel


class Form_three : AppCompatActivity() {
    private var parentLinearLayout: LinearLayout? = null
    private var EduparentLinearLayout: LinearLayout? = null
    lateinit var binding: ActivityFormThreeBinding
    lateinit var education_recyler: RecyclerView
    lateinit var work_recyler: RecyclerView
    lateinit var education_recyler_adapter: EducationFieldAdapter
    lateinit var work_recyler_adapter: ExperienceFieldAdapter
    lateinit var list: ArrayList<EducationFieldModel>
    lateinit var work_list: ArrayList<ExperienceFieldModel>
    lateinit var university: Array<String>
    lateinit var course: Array<String>
    lateinit var board: Array<String>
    lateinit var company_name: Array<String>
    lateinit var designation: Array<String>
    lateinit var location: Array<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormThreeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parentLinearLayout = findViewById(com.bumptech.searchbuddy.R.id.parent_layout)

        university = arrayOf(
            "",

        )
        course = arrayOf(
            "",

        )
        board = arrayOf(
            "",

        )
        company_name = arrayOf(
            "",

        )
        designation = arrayOf(
            "",

        )
        location = arrayOf(
            "",

        )

        binding.btnSbmt.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnSkip.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        education_recyler = binding.recyclerEducation
        work_recyler = binding.recyclerExp
        education_recyler.layoutManager =LinearLayoutManager(this)
        work_recyler.layoutManager =LinearLayoutManager(this)
        list = arrayListOf<EducationFieldModel>()
        work_list = arrayListOf<ExperienceFieldModel>()
        getworkdata()
        getdata()
//        education_recyler_adapter = EducationFieldAdapter(list,this)


        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor =
                this.resources.getColor(com.bumptech.searchbuddy.R.color.grey)
        }

    }


    private fun getdata() {
        for (i in 0..university.size - 1) {
            val listdata = EducationFieldModel(university[i], course[i],board[i])
            list.add(listdata)

        }
        education_recyler_adapter = EducationFieldAdapter(list, this)
        education_recyler.adapter = education_recyler_adapter
        binding.addEdu.setOnClickListener{
            for (i in 0..university.size - 1) {
                val listdata = EducationFieldModel(university[i], course[i],board[i])
                list.add(listdata)
                if (list.size >1){
                    binding.deleteEdu.visibility = View.VISIBLE
                }


            }
            education_recyler_adapter.notifyDataSetChanged()
        }

        binding.deleteEdu.setOnClickListener{
            for (i in 0..university.size - 1) {
                val listdata = EducationFieldModel(university[i], course[i],board[i])
                list.remove(listdata)
                if (list.size<=1){
                    binding.deleteEdu.visibility = View.GONE
                }

            }
            education_recyler_adapter.notifyDataSetChanged()

        }
    }

    private fun getworkdata() {
        for (i in 0..company_name.size - 1) {
            val workdata = ExperienceFieldModel(company_name[i], designation[i],location[i])
            work_list.add(workdata)

        }
        work_recyler_adapter = ExperienceFieldAdapter(work_list, this)
        work_recyler.adapter = work_recyler_adapter
        binding.addExp.setOnClickListener{
            for (i in 0..university.size - 1) {
                val workdata = ExperienceFieldModel(company_name[i], designation[i],location[i])
                work_list.add(workdata)
                if (work_list.size >1){
                    binding.deleteExp.visibility = View.VISIBLE
                }

            }
            work_recyler_adapter.notifyDataSetChanged()
        }
        binding.deleteExp.setOnClickListener{
            for (i in 0..university.size - 1) {
                val workdata = ExperienceFieldModel(company_name[i], designation[i],location[i])
                work_list.remove(workdata)
                if (work_list.size<=1){
                    binding.deleteExp.visibility = View.GONE
                }

            }
            work_recyler_adapter.notifyDataSetChanged()

        }
    }
}