package com.example.searchbuddy.Categories

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.searchbuddy.ApiClient.ApiClient
import com.example.searchbuddy.model.ErrorModel
import com.example.searchbuddy.model.JobRequestModel
import com.example.searchbuddy.model.JobRequestResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SalesJobsViewModel : ViewModel() {
    var loginResponse = MutableLiveData<JobRequestResponse>()
    var errorResponse = MutableLiveData<String>()

    fun requestJobs(
        context: Context,
        Request: JobRequestModel,
//        exp_from:Int,
//        exp_to:Int,
        progress: ProgressBar
    ): MutableLiveData<JobRequestResponse> {
        progress.visibility = View.VISIBLE

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestJobs(Request)
                    .enqueue(object : Callback<JobRequestResponse> {
                        override fun onFailure(call: Call<JobRequestResponse>, t: Throwable) {
                            progress.visibility = View.GONE
                            Log.i("hellooo", call.toString())
                            Log.i("hellooo", t.toString())

                            Toast.makeText(
                                context,
                                t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<JobRequestResponse>,
                            response: Response<JobRequestResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                loginResponse.value = response.body()
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

        return loginResponse
    }

    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }
}