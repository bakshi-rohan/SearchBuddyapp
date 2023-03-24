package com.example.searchbuddy.Forms

import UpdateEducationDetailRequest
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
import com.example.searchbuddy.model.Edu.UpdateEduResponse
import com.example.searchbuddy.model.ErrorModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Signup_Education_ViewModel:ViewModel() {
    var errorResponse = MutableLiveData<String>()
    var updateEduResponse=MutableLiveData<UpdateEduResponse>()
    fun UpdateEducationDetail(
        context: Context,
        Request: UpdateEducationDetailRequest,
        progress: ProgressBar
    ): MutableLiveData<UpdateEduResponse> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestEduUpdate("Bearer " + token.toString(),Request)
                    .enqueue(object : Callback<UpdateEduResponse> {
                        override fun onFailure(call: Call<UpdateEduResponse>, t: Throwable) {
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
                            call: Call<UpdateEduResponse>,
                            response: Response<UpdateEduResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                updateEduResponse.value = response.body()
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

        return updateEduResponse
    }

    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }
}