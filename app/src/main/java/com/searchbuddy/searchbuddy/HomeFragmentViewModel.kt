package com.searchbuddy.searchbuddy

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
import com.searchbuddy.searchbuddy.model.PersonalDetailModel
import com.searchbuddy.searchbuddy.model.RecommendedjobData
import com.searchbuddy.searchbuddy.model.SaveJobsResponse
import com.searchbuddy.searchbuddy.model.TopcomapniesResponse
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
    var savedResponse = MutableLiveData<SaveJobsResponse>()
    var loginResponse = MutableLiveData<PersonalDetailModel>()

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
    fun requestPersonalDetail(
        context: Context,
        UserId: Int,
        progress: ProgressBar
    ): MutableLiveData<PersonalDetailModel> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)
        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestPersonalDetail("Bearer " + token.toString(),UserId)
                    .enqueue(object : Callback<PersonalDetailModel> {
                        override fun onFailure(call: Call<PersonalDetailModel>, t: Throwable) {
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
                            call: Call<PersonalDetailModel>,
                            response: Response<PersonalDetailModel>
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