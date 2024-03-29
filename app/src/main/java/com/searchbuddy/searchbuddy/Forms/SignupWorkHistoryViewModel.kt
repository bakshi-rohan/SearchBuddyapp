package com.searchbuddy.searchbuddy.Forms

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
import com.searchbuddy.searchbuddy.model.RequestCompanyResponse
import com.searchbuddy.searchbuddy.model.SaveCompanyRequest
import com.searchbuddy.searchbuddy.model.SaveWorkHistoryResponse
import com.searchbuddy.searchbuddy.model.UpdatePrefResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupWorkHistoryViewModel:ViewModel() {
    var loginResponse = MutableLiveData<RequestCompanyResponse>()
    var errorResponse = MutableLiveData<String>()
    var savecompanyRes = MutableLiveData<SaveWorkHistoryResponse>()
    var UpdateResponse = MutableLiveData<UpdatePrefResponse>()

    fun saveCompany(
        context: Context,
        loginRequest: SaveCompanyRequest,
        progress: ProgressBar
    ): MutableLiveData<SaveWorkHistoryResponse> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.saveCompany("Bearer " +token.toString(),loginRequest)
                    .enqueue(object : Callback<SaveWorkHistoryResponse> {
                        override fun onFailure(call: Call<SaveWorkHistoryResponse>, t: Throwable) {
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
                            call: Call<SaveWorkHistoryResponse>,
                            response: Response<SaveWorkHistoryResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                savecompanyRes.value = response.body()
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

        return savecompanyRes
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
    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }
}