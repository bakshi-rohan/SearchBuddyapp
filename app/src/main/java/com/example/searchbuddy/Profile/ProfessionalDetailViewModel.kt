package com.example.searchbuddy.Profile

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
import com.example.searchbuddy.model.ErrorModel
import com.example.searchbuddy.model.GetAllWorkHistoryModelItem
import com.example.searchbuddy.model.ProfessionalDetailModel
import com.example.searchbuddy.model.UpdatePrefResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfessionalDetailViewModel :ViewModel() {
    var loginResponse = MutableLiveData<ProfessionalDetailModel>()
    var errorResponse = MutableLiveData<String>()
    var UpdateResponse = MutableLiveData<UpdatePrefResponse>()
    var WorkHistoryResponse = MutableLiveData<List<GetAllWorkHistoryModelItem>>()


    fun requestProfessionalDetail(
        context: Context,
        UserId: Int,
        progress: ProgressBar
    ): MutableLiveData<ProfessionalDetailModel> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestProfessionalDetail("Bearer " + token.toString(),UserId)
                    .enqueue(object : Callback<ProfessionalDetailModel> {
                        override fun onFailure(call: Call<ProfessionalDetailModel>, t: Throwable) {
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
                            call: Call<ProfessionalDetailModel>,
                            response: Response<ProfessionalDetailModel>
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

    fun UpdateProfessionalDetail(
        context: Context,
        info: String,
        resume: String,
        progress: ProgressBar
    ): MutableLiveData<UpdatePrefResponse> {
        progress.visibility = View.VISIBLE
//        val requestFile: RequestBody =
//            resume.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//        val ResumeFiles =
//            MultipartBody.Part.createFormData("resume", resume.name, requestFile)
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.UpdateProfessionalDetail("Bearer " + token.toString(), resume!!,info)
                    .enqueue(object : Callback<UpdatePrefResponse> {
                        override fun onFailure(call: Call<UpdatePrefResponse>, t: Throwable) {
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
                            call: Call<UpdatePrefResponse>,
                            response: Response<UpdatePrefResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                UpdateResponse.value = response.body()
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

        return UpdateResponse
    }
    fun requestWorkHistory(
        context: Context,
//        loginRequest: LogOutRequest,
        progress: ProgressBar
    ): MutableLiveData<List<GetAllWorkHistoryModelItem>> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestallhistory("Bearer " + token.toString())
                    .enqueue(object : Callback<List<GetAllWorkHistoryModelItem>> {
                        override fun onFailure(call: Call<List<GetAllWorkHistoryModelItem>>, t: Throwable) {
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
                            call: Call<List<GetAllWorkHistoryModelItem>>,
                            response: Response<List<GetAllWorkHistoryModelItem>>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                WorkHistoryResponse.value = response.body()
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

        return WorkHistoryResponse
    }

    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }

}