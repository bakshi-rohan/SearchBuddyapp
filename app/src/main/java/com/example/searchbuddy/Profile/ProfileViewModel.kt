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
import com.example.searchbuddy.model.Edu.EducationDetailModel
import com.example.searchbuddy.model.Edu.PrefrencesModel
import com.example.searchbuddy.model.ErrorModel
import com.example.searchbuddy.model.PersonalDetailModel
import com.example.searchbuddy.model.ProfessionalDetailModel
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

class ProfileViewModel:ViewModel() {
    var loginResponse = MutableLiveData<PersonalDetailModel>()
    var UpdateResponse = MutableLiveData<Unit>()
    var errorResponse = MutableLiveData<String>()
    var prodetailresponse=MutableLiveData<ProfessionalDetailModel>()
    var edudetailresponse=MutableLiveData<EducationDetailModel>()
    var prefResponse = MutableLiveData<PrefrencesModel>()
    fun requestPersonalDetail(
        context: Context,
        UserId: Int,
        progress: ProgressBar
    ): MutableLiveData<PersonalDetailModel> {

        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)
        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestPersonalDetail("Bearer " + token.toString(),UserId)
                    .enqueue(object : Callback<PersonalDetailModel> {
                        override fun onFailure(call: Call<PersonalDetailModel>, t: Throwable) {
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
                            call: Call<PersonalDetailModel>,
                            response: Response<PersonalDetailModel>
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

                                prodetailresponse.value = response.body()
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

        return prodetailresponse
    }
    fun requestEducationDetail(
        context: Context,
        UserId: Int,
        progress: ProgressBar
    ): MutableLiveData<EducationDetailModel> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestEducation("Bearer " + token.toString(),UserId)
                    .enqueue(object : Callback<EducationDetailModel> {
                        override fun onFailure(call: Call<EducationDetailModel>, t: Throwable) {
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
                            call: Call<EducationDetailModel>,
                            response: Response<EducationDetailModel>
                        ) {
                            progress.visibility = View.GONE

                            if (response.code() == 200) {

                                edudetailresponse.value = response.body()
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

        return edudetailresponse
    }
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

                                prefResponse.value = response.body()
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

        return prefResponse
    }
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

    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }

}