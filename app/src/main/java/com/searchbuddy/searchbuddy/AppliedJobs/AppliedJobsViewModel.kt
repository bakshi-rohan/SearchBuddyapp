package com.searchbuddy.searchbuddy.AppliedJobs

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
import com.searchbuddy.searchbuddy.model.AppliedJobsModel
import com.searchbuddy.searchbuddy.model.ErrorModel
import com.searchbuddy.searchbuddy.model.aa.PreAppliedResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppliedJobsViewModel : ViewModel() {

    var logoutResponse = MutableLiveData<AppliedJobsModel>()
    var PreAppliedResponse = MutableLiveData<PreAppliedResponse>()
    var errorResponse = MutableLiveData<String>()

    fun requestAppliedJobs(
        context: Context,
        userId:Int,
        pagesize:Int,
        index:Int,
        progress: ProgressBar
    ): MutableLiveData<AppliedJobsModel> {
        progress.visibility = View.GONE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.GONE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestAppliedJobs("Bearer " + token.toString(),userId,pagesize,index)
                    .enqueue(object : Callback<AppliedJobsModel> {
                        override fun onFailure(call: Call<AppliedJobsModel>, t: Throwable) {
                            progress.visibility = View.GONE
                            Log.i("errrrrr", call.toString())
                            Log.i("errrr", t.toString())

                            Toast.makeText(
                                context,
                                t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<AppliedJobsModel>,
                            response: Response<AppliedJobsModel>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                logoutResponse.value = response.body()
                            } else {
                                Log.i("errorrrrr", response.errorBody().toString())
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

        return logoutResponse
    }

    fun requestPreAppliedJobs(
        context: Context,
        pagesize:Int,
        index:Int,
        email:String,
        progress: ProgressBar
    ): MutableLiveData<PreAppliedResponse> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestPreAppliedJobs(pagesize,index,email)
                    .enqueue(object : Callback<PreAppliedResponse> {
                        override fun onFailure(call: Call<PreAppliedResponse>, t: Throwable) {
                            progress.visibility = View.GONE
                            Log.i("errrrrr", call.toString())
                            Log.i("errrr", t.toString())

                            Toast.makeText(
                                context,
                                t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<PreAppliedResponse>,
                            response: Response<PreAppliedResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                PreAppliedResponse.value = response.body()
                            } else {
                                Log.i("errorrrrr", response.errorBody().toString())
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

        return PreAppliedResponse
    }


    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }
}