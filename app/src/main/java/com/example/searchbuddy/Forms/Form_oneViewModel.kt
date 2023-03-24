package com.example.searchbuddy.Forms

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
import com.example.searchbuddy.model.SignupResponse
import com.example.searchbuddy.model.UploadResumeResponse
import com.google.gson.Gson
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


class Form_oneViewModel : ViewModel() {
    var uploadResumeResponse = MutableLiveData<UploadResumeResponse>()
    var signupData = MutableLiveData<SignupResponse>()
    var errorResponse = MutableLiveData<String>()


    fun UpdateProfile(
        context: Context,
        resumeFile: File,
        progress: ProgressBar
    ): MutableLiveData<UploadResumeResponse> {
        progress.visibility = View.VISIBLE
        // Parsing any Media type file
        val requestFile: RequestBody =
            resumeFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val ResumeFile =
            MultipartBody.Part.createFormData("file", resumeFile.name, requestFile)
        val pdfFile: RequestBody =
            resumeFile.asRequestBody("application/pdf".toMediaTypeOrNull())
        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                var token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)
                apiService.uploadResume(ResumeFile)
                    .enqueue(object : Callback<UploadResumeResponse> {
                        override fun onFailure(call: Call<UploadResumeResponse>, t: Throwable) {
                            progress.visibility = View.GONE

                            Toast.makeText(
                                context,
                                t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<UploadResumeResponse>,
                            response: Response<UploadResumeResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                uploadResumeResponse.value = response.body()
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

        return uploadResumeResponse
    }
    fun createProfile(
        context: Context,
        information : String,
        progress: ProgressBar,
    ): MutableLiveData<SignupResponse>{
        progress.visibility = View.VISIBLE
//        val requestFile: RequestBody =
//            resumeFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//        val ResumeFiles =
//            MultipartBody.Part.createFormData("resume", resumeFile.name, requestFile)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                var token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)
                apiService.CreateProfile(information)
                    .enqueue(object : Callback<SignupResponse> {
                        override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                            progress.visibility = View.GONE

                            Toast.makeText(
                                context,
                                t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<SignupResponse>,
                            response: Response<SignupResponse>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                signupData.value = response.body()
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

        return signupData
    }

    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }
}
