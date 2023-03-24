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
import com.example.searchbuddy.model.Edu.PrefrencesModel
import com.example.searchbuddy.model.ErrorModel
import com.example.searchbuddy.model.UpdatePrefRequest
import com.example.searchbuddy.model.UpdatePrefResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrefrencesViewModel:ViewModel() {
    var loginResponse = MutableLiveData<PrefrencesModel>()
    var errorResponse = MutableLiveData<String>()
    var updatePrefResponse=MutableLiveData<UpdatePrefResponse>()


    fun requestPrefrencesDetail(
        context: Context,
        UserId: Int,
        progress: ProgressBar
    ): MutableLiveData<PrefrencesModel> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestPrefrences("Bearer " + token.toString(),UserId)
                    .enqueue(object : Callback<PrefrencesModel> {
                        override fun onFailure(call: Call<PrefrencesModel>, t: Throwable) {
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
                            call: Call<PrefrencesModel>,
                            response: Response<PrefrencesModel>
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
    fun UpdatePrefDetail(
        context: Context,
        Request: UpdatePrefRequest,
        progress: ProgressBar
    ): MutableLiveData<UpdatePrefResponse> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestPrefUpdate("Bearer " + token.toString(),Request)
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

                                updatePrefResponse.value = response.body()
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

        return updatePrefResponse
    }
    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }
}