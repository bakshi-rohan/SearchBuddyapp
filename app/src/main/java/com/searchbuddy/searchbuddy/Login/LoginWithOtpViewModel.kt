package com.searchbuddy.searchbuddy.Login

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.gson.Gson
import com.searchbuddy.searchbuddy.ApiClient.ApiClient
import com.searchbuddy.searchbuddy.model.ErrorModel
import com.searchbuddy.searchbuddy.model.LoginResponse
import com.searchbuddy.searchbuddy.model.LoginWithOTPResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginWithOtpViewModel:ViewModel() {
    var loginResponse = MutableLiveData<LoginWithOTPResponse>()
    var varifyotpResp = MutableLiveData<LoginResponse>()
    var errorResponse = MutableLiveData<String>()
    fun requestLogin(
        context: Context,
        loginRequest: Any,
        progress: ProgressBar
    ): MutableLiveData<LoginWithOTPResponse> {
        progress.visibility = View.VISIBLE

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestLoginwithOtp(loginRequest)
                    .enqueue(object : Callback<LoginWithOTPResponse> {
                        override fun onFailure(call: Call<LoginWithOTPResponse>, t: Throwable) {
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
                            call: Call<LoginWithOTPResponse>,
                            response: Response<LoginWithOTPResponse>
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
    fun verifyOtp(
        context: Context,
        loginRequest: Any,
        progress: ProgressBar
    ): MutableLiveData<LoginResponse> {
        progress.visibility = View.VISIBLE

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.verifyOtp(loginRequest)
                    .enqueue(object : Callback<LoginResponse> {
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
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
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                varifyotpResp.value = response.body()
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

        return varifyotpResp
    }
    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }

}