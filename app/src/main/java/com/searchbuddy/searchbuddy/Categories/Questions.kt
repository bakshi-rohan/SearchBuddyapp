package com.searchbuddy.searchbuddy.Categories

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.searchbuddy.R
import com.bumptech.searchbuddy.databinding.FragmentQuestionsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.searchbuddy.searchbuddy.Adapter.QuestionAdapter
import com.searchbuddy.searchbuddy.Dashboard.Dashboard
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.Question


class Questions : Fragment() {

    var checkList: ArrayList<String> = ArrayList()
    var radioList: ArrayList<String> = ArrayList()
    lateinit var newArrayList: ArrayList<Question>
    lateinit var binding: FragmentQuestionsBinding
    lateinit var myadapter: QuestionAdapter
    lateinit var position_id: String
    lateinit var viewModel: QuestionViewModel
    var position_id_int = 0
    lateinit var responses_list: ArrayList<Any>
    lateinit var response_object: ArrayList<String>
    lateinit var question_id: String
    lateinit var question_list:List<Question>
    lateinit var UserID: String
    var yo:Int=0
    lateinit var bottomNavView: BottomNavigationView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(QuestionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQuestionsBinding.inflate(inflater, container, false)
//        questionList = arrayListOf("Choose your skills")
        bottomNavView=  (activity as Dashboard?)!!.findViewById(R.id.nav_view)
        bottomNavView.visibility=View.GONE
        checkList = arrayListOf("java", "kotlin", "Sql")
        radioList = arrayListOf("Kotlin", "Male")
        newArrayList = arrayListOf<Question>()
        if (arguments != null) {
            position_id = requireArguments().getString("position_id").toString()
            Log.i("id", position_id)

        }
        position_id_int = position_id.toInt()
        UserID = LocalSessionManager.getStringValue(Constant.UserID, "", requireContext()).toString()
        yo = UserID.toInt()



        fun getQuestion() {
            viewModel.requestProfessionalDetail(requireContext(), position_id_int, binding.progress)
                .observe(requireActivity(),
                    {
                        checkIfFragmentAttached {
                            val activity = activity as Context
                            var k = it.questions
                            Log.i("kkk", k.toString())
                            newArrayList = it.questions as ArrayList<Question>
                            var recyler = binding.questionRecyler
                            recyler.layoutManager = LinearLayoutManager(requireContext())
                            myadapter = QuestionAdapter(newArrayList)
                            recyler.adapter = myadapter
                            question_list=newArrayList
                            if (newArrayList.size!=0) {
                                for (i in 0..newArrayList.size) {
                                    var k = newArrayList[0]
                                    question_id = k.id.toString()
                                    Log.i("kkkkkk", question_id.toString())
                                }
                            }
                            else if (newArrayList.size==0){
                                binding.txtNoData.visibility=View.VISIBLE
                            }
                        }

                    })
        }
        getQuestion()

        binding.btnLogin.setOnClickListener {
            for (i in 0..newArrayList.size - 1) {
                var rohan = object {
                    var id = question_id
                    var ans = question_list
                }
                Log.i("findme", rohan.toString())

            }
            submitResponse()
            if (isAdded()){
                viewModel.errorMessage()?.observe(requireActivity(), {
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                })
            }

        }


        return binding.root


    }

    private fun submitResponse() {
        yo = UserID.toInt()

        responses_list = ArrayList()
        response_object = arrayListOf("Yes")
        var answer_one = object {
            var id = 11
            var response = response_object

        }
        var answer_two = object {
            var id = 12
            var response = response_object
        }
        var answer_three = object {
            var id = 12
            var response = response_object
        }
        var answer_four = object {
            var id = 12
            var response = response_object
        }

        responses_list.add(
            answer_one
        )
        responses_list.add(
            answer_two
        )
        responses_list.add(
            answer_three
        )
        responses_list.add(
            answer_four
        )
        var info = object {
            var position_id = position_id_int.toString()
            var responses = responses_list
            var type = "GENERAL"
            var candidate = object {
                var id = yo
            }
        }
        var gson = Gson()
        var information = gson.toJson(info)
        viewModel.UpdateProfessionalDetail(requireContext(), information, binding.progress)
            .observe(requireActivity(), {
                Log.i("resp", it.message)
                checkIfFragmentAttached {
                    Navigation.findNavController(binding.btnLogin).navigate(R.id.action_question_fragment_to_successfully_applied)
                }
            })
    }
    fun checkIfFragmentAttached(operation: Context.() -> Unit) {
        if (isAdded && context != null) {
            operation(requireContext())
        }
    }
    fun isAttachedToActivity(): Boolean {
        return isVisible && activity != null
    }


}