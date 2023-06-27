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
import com.searchbuddy.searchbuddy.model.JobdescriptionResponse
import com.searchbuddy.searchbuddy.model.SaveJobsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobdescriptionViewModel : ViewModel() {
    var loginResponse = MutableLiveData<JobdescriptionResponse>()
    var SaveResponse = MutableLiveData<SaveJobsResponse>()
    var errorResponse = MutableLiveData<String>()

    fun requestJobs(
        context: Context,
        position_id: Int,
        progress: ProgressBar
    ): MutableLiveData<JobdescriptionResponse> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.getLoggedJobdescription("Bearer " + token.toString(),position_id)
                    .enqueue(object : Callback<JobdescriptionResponse> {
                        override fun onFailure(call: Call<JobdescriptionResponse>, t: Throwable) {
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
                            call: Call<JobdescriptionResponse>,
                            response: Response<JobdescriptionResponse>
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

    fun saveJobs(
        context: Context,
        id: Any,
        progress: ProgressBar
    ): MutableLiveData<SaveJobsResponse> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.saveJob("Bearer " + token.toString(),id)
                    .enqueue(object : Callback<SaveJobsResponse> {
                        override fun onFailure(call: Call<SaveJobsResponse>, t: Throwable) {
                            progress.visibility = View.GONE
                            Log.i("hellooo", call.toString())
                            Log.i("hellooo", t.toString())


                        }

                        override fun onResponse(
                            call: Call<SaveJobsResponse>,
                            response: Response<SaveJobsResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                SaveResponse.value = response.body()
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

        return SaveResponse
    }
    fun deleteJobs(
        context: Context,
        id: Any,
        progress: ProgressBar
    ): MutableLiveData<SaveJobsResponse> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.deleteJob("Bearer " + token.toString(),id)
                    .enqueue(object : Callback<SaveJobsResponse> {
                        override fun onFailure(call: Call<SaveJobsResponse>, t: Throwable) {
                            progress.visibility = View.GONE
                            Log.i("hellooo", call.toString())
                            Log.i("hellooo", t.toString())


                        }

                        override fun onResponse(
                            call: Call<SaveJobsResponse>,
                            response: Response<SaveJobsResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                SaveResponse.value = response.body()
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

        return SaveResponse
    }

    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }
}