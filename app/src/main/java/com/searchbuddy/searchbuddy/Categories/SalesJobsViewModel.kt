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
import com.searchbuddy.searchbuddy.model.JobRequestModel
import com.searchbuddy.searchbuddy.model.JobRequestResponse
import com.searchbuddy.searchbuddy.model.SaveJobsResponse
import com.searchbuddy.searchbuddy.model.aa.PreJobsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SalesJobsViewModel : ViewModel() {
    var loginResponse = MutableLiveData<JobRequestResponse>()
    var PreResponse = MutableLiveData<PreJobsResponse>()
    var errorResponse = MutableLiveData<String>()
    var savedResponse = MutableLiveData<SaveJobsResponse>()

    fun requestPremiumJobs(
        context: Context,
        Request: JobRequestModel,
//        exp_from:Int,
//        exp_to:Int,
        progress: ProgressBar
    ): MutableLiveData<PreJobsResponse> {
        progress.visibility = View.VISIBLE

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestPremiumJobs(Request)
                    .enqueue(object : Callback<PreJobsResponse> {
                        override fun onFailure(call: Call<PreJobsResponse>, t: Throwable) {
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
                            call: Call<PreJobsResponse>,
                            response: Response<PreJobsResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                PreResponse.value = response.body()
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

        return PreResponse
    }
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
    fun requestJobsLogged(
        context: Context,
        Request: JobRequestModel,
//        exp_from:Int,
//        exp_to:Int,
        progress: ProgressBar
    ): MutableLiveData<JobRequestResponse> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestJobsLogged("Bearer " + token.toString(),Request)
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

                            Toast.makeText(
                                context,
                                t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<SaveJobsResponse>,
                            response: Response<SaveJobsResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                savedResponse.value = response.body()
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

        return savedResponse
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

//                            Toast.makeText(
//                                context,
//                                t.message,
//                                Toast.LENGTH_LONG
//                            ).show()
                        }

                        override fun onResponse(
                            call: Call<SaveJobsResponse>,
                            response: Response<SaveJobsResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                savedResponse.value = response.body()
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

        return savedResponse
    }
    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }

}