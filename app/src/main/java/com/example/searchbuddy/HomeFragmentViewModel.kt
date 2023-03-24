package com.example.searchbuddy

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.searchbuddy.ApiClient.ApiClient
import com.example.searchbuddy.Utils.Constant
import com.example.searchbuddy.Utils.LocalSessionManager
import com.example.searchbuddy.model.AppliedJobsModel
import com.example.searchbuddy.model.ErrorModel
import com.example.searchbuddy.model.RecommendedjobData
import com.example.searchbuddy.model.TopcomapniesResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragmentViewModel : ViewModel() {
    var logoutResponse = MutableLiveData<RecommendedjobData>()
    var errorResponse = MutableLiveData<String>()
    var topcompaniesResponse=MutableLiveData<TopcomapniesResponse>()
    var appliedJobsresponse=MutableLiveData<AppliedJobsModel>()

    fun requestRecommendedJobs(
        context: Context,
//        loginRequest: LogOutRequest,
        progress: ProgressBar
    ): MutableLiveData<RecommendedjobData> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.getRecommendedJobs("Bearer " + token.toString())
                    .enqueue(object : Callback<RecommendedjobData> {
                        override fun onFailure(call: Call<RecommendedjobData>, t: Throwable) {
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
                            call: Call<RecommendedjobData>,
                            response: Response<RecommendedjobData>
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
    fun requestTopcompanies(
        context: Context,
//        loginRequest: LogOutRequest,
        progress: ProgressBar
    ): MutableLiveData<TopcomapniesResponse> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestTopcompanies("Bearer " + token.toString())
                    .enqueue(object : Callback<TopcomapniesResponse> {
                        override fun onFailure(call: Call<TopcomapniesResponse>, t: Throwable) {
                            progress.visibility = View.GONE
                            Log.i("errrrrr", call.toString())
                            Log.i("errrr", t.toString())

                            Toast.makeText(
                                context,
                                t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<TopcomapniesResponse>,
                            response: Response<TopcomapniesResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                topcompaniesResponse.value = response.body()
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

        return topcompaniesResponse
    }
    fun requestAppliedJobs(
        context: Context,
        userId:Int,
        pagesize:Int,
        index:Int,
        progress: ProgressBar
    ): MutableLiveData<AppliedJobsModel> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
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

                                appliedJobsresponse.value = response.body()
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

        return appliedJobsresponse
    }

    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }
}