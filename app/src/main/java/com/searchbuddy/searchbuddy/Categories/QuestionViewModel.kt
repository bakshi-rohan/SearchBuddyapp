package com.searchbuddy.searchbuddy.Categories

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.gson.Gson
import com.searchbuddy.searchbuddy.ApiClient.ApiClient
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.ErrorModel
import com.searchbuddy.searchbuddy.model.GetQuestionModel
import com.searchbuddy.searchbuddy.model.SubmitResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionViewModel : ViewModel() {
    var WorkHistoryResponse = MutableLiveData<GetQuestionModel>()
    var SubmitResponse = MutableLiveData<SubmitResponse>()
    var errorResponse = MutableLiveData<String>()

    fun requestProfessionalDetail(
        context: Context,
         Position:Int,
        progress: ProgressBar
    ): MutableLiveData<GetQuestionModel> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestQuestion(Position)
                    .enqueue(object : Callback<GetQuestionModel> {
                        override fun onFailure(call: Call<GetQuestionModel>, t: Throwable) {
                            progress.visibility = View.GONE
                            Log.i("hellooo", call.toString())
                            Log.i("hellooo", t.toString())

                            Toast.makeText(
                                context,
                                t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<GetQuestionModel>,
                            response: Response<GetQuestionModel>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                WorkHistoryResponse.value = response.body()
                            } else {
                                Log.i("hellooo", response.errorBody().toString())
                                val error = Gson().fromJson(
                                    response.errorBody()!!.charStream(),
                                    ErrorModel::class.java
                                )
                                errorResponse.value = error.message

                                errorMessage()
                            }

                        }


                    })


            }
        } catch (e: Exception) {
            progress.visibility = View.GONE

        }

        return WorkHistoryResponse
    }
    fun UpdateProfessionalDetail(
        context: Context,
        info: String,
        progress: ProgressBar
    ): MutableLiveData<SubmitResponse> {
        progress.visibility = View.VISIBLE

        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.SubmitResponse("Bearer " + token.toString(),info)
                    .enqueue(object : Callback<SubmitResponse> {
                        override fun onFailure(call: Call<SubmitResponse>, t: Throwable) {
                            progress.visibility = View.GONE
                            Log.i("hellooo", call.toString())
                            Log.i("hellooo", t.toString())

                            Toast.makeText(
                                context,
                                t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<SubmitResponse>,
                            response: Response<SubmitResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                SubmitResponse.value = response.body()
                            } else {
                                Log.i("hellooo", response.errorBody().toString())
                                val error = Gson().fromJson(
                                    response.errorBody()!!.charStream(),
                                    ErrorModel::class.java
                                )
                                errorResponse.value = error.message

                                errorMessage()
                            }

                        }
                    })


            }
        } catch (e: Exception) {
            progress.visibility = View.GONE

        }

        return SubmitResponse
    }
    fun errorMessage(): MutableLiveData<String> {
        return errorResponse

    }
}



