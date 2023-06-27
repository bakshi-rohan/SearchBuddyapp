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
import com.searchbuddy.searchbuddy.model.ResetPasswordResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordViewModel : ViewModel() {
    var ResetResponse = MutableLiveData<ResetPasswordResponse>()
    var errorResponse = MutableLiveData<String>()

    fun requestReset(
        context: Context,
        ResetRequest: ArrayList<String>,
        progress: ProgressBar
    ): MutableLiveData<ResetPasswordResponse> {
        progress.visibility = View.VISIBLE

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestResetPassword(ResetRequest)
                    .enqueue(object : Callback<ResetPasswordResponse> {
                        override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
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
                            call: Call<ResetPasswordResponse>,
                            response: Response<ResetPasswordResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                ResetResponse.value = response.body()
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

        return ResetResponse
    }

    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }
}