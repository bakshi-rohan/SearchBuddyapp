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
import com.example.searchbuddy.model.RequestCompanyResponse
import com.example.searchbuddy.model.SaveCompanyRequest
import com.example.searchbuddy.model.SaveWorkHistoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProfesionalDetailViewModel:ViewModel() {
    var loginResponse = MutableLiveData<RequestCompanyResponse>()
    var errorResponse = MutableLiveData<String>()
    var savecompanyRes = MutableLiveData<SaveWorkHistoryResponse>()

    fun requestCompany(
        context: Context,
        companyName: String,
        progress: ProgressBar
    ): MutableLiveData<RequestCompanyResponse> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.RequestCompany("Bearer " + token.toString(),companyName)
                    .enqueue(object : Callback<RequestCompanyResponse> {
                        override fun onFailure(call: Call<RequestCompanyResponse>, t: Throwable) {
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
                            call: Call<RequestCompanyResponse>,
                            response: Response<RequestCompanyResponse>
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

    fun UpdateCompany(
        context: Context,
        companyName: String,
        loginRequest: SaveCompanyRequest,
        progress: ProgressBar
    ): MutableLiveData<SaveWorkHistoryResponse> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.UpodateCompany("Bearer " +token.toString(),companyName,loginRequest)
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

    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }
}