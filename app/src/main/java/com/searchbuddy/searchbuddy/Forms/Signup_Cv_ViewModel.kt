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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class Signup_Cv_ViewModel:ViewModel() {
    var UpdateResponse = MutableLiveData<Unit>()
    var errorResponse = MutableLiveData<String>()

    fun UploadResume(
        context: Context,
        resume: File,
        info:String,
        progress: ProgressBar
    ): MutableLiveData<Unit> {
        progress.visibility = View.VISIBLE
        // Parsing any Media type file
        val requestFile: RequestBody =
            resume.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val ResumeFile =
            MultipartBody.Part.createFormData("resume", resume.name, requestFile)
        val pdfFile: RequestBody =
            resume.asRequestBody("application/pdf".toMediaTypeOrNull())
        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                var token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)
                apiService.UploaResume("Bearer " + token.toString(),ResumeFile,info)
                    .enqueue(object : Callback<Unit> {
                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            progress.visibility = View.GONE

                            Toast.makeText(
                                context,
                                t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<Unit>,
                            response: Response<Unit>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                UpdateResponse.value = response.body()
                            } else {
                                val error = Gson().fromJson(
                                    response.errorBody()!!.charStream(),
                                    ErrorModel::class.java
                                )

                                errorResponse.value = error.message
                                Log.i("yoooo", error.message)

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
    fun UploadVideoResume(
        context: Context,
        video: File,
        candidateId:Int,
        progress: ProgressBar
    ): MutableLiveData<Unit> {
        progress.visibility = View.VISIBLE
        // Parsing any Media type file
        val requestFile: RequestBody =
            video.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val ResumeFile =
            MultipartBody.Part.createFormData("video", video.name, requestFile)
        val pdfFile: RequestBody =
            video.asRequestBody("application/pdf".toMediaTypeOrNull())
        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                var token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)
                apiService.UploadVideoResume("Bearer " + token.toString(),ResumeFile,candidateId)
                    .enqueue(object : Callback<Unit> {
                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            progress.visibility = View.GONE

                            Toast.makeText(
                                context,
                                t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<Unit>,
                            response: Response<Unit>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                UpdateResponse.value = response.body()
                            } else {
                                val error = Gson().fromJson(
                                    response.errorBody()!!.charStream(),
                                    ErrorModel::class.java
                                )

                                errorResponse.value = error.message
                                Log.i("yoooo", error.message)

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