package com.searchbuddy.searchbuddy.Categories

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.searchbuddy.searchbuddy.ApiClient.ApiClient
import com.searchbuddy.searchbuddy.Utils.Constant
import com.searchbuddy.searchbuddy.Utils.LocalSessionManager
import com.searchbuddy.searchbuddy.model.ErrorModel
import com.searchbuddy.searchbuddy.model.GetSavedJobsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedJobsViewModel:ViewModel() {
    var logoutResponse = MutableLiveData<GetSavedJobsResponse>()
    var errorResponse = MutableLiveData<String>()

    fun requestAppliedJobs(
        context: Context,
        pagesize:Int,
        index:Int,
        progress: ProgressBar,
        image:ImageView,
        text:TextView

    ): MutableLiveData<GetSavedJobsResponse>{
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestSavedJobs("Bearer " + token.toString(),pagesize,index)
                    .enqueue(object : Callback<GetSavedJobsResponse> {
                        override fun onFailure(call: Call<GetSavedJobsResponse> ,t: Throwable) {
//                            nodataimage.visibility=View.VISIBLE
                            progress.visibility = View.GONE
                            image.visibility=View.VISIBLE
                            text.visibility=View.VISIBLE
                            Log.i("errrrrr", call.toString())
                            Log.i("errrr", t.toString())


                        }

                        override fun onResponse(
                            call: Call<GetSavedJobsResponse>,
                            response: Response<GetSavedJobsResponse>
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

    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }
}