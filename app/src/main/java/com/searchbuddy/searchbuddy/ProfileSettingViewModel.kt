package com.searchbuddy.searchbuddy

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
import com.searchbuddy.searchbuddy.model.GetAllWorkHistoryModelItem
import com.searchbuddy.searchbuddy.model.RequestProfileResponse
import com.searchbuddy.searchbuddy.model.UpdatePasswordResponse
import com.searchbuddy.searchbuddy.model.UpdateProfileResponse
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

class ProfileSettingViewModel : ViewModel() {
    var loginResponse = MutableLiveData<RequestProfileResponse>()
    var errorResponse = MutableLiveData<String>()
    var signupData = MutableLiveData<UpdateProfileResponse>()
    var ResetResponse = MutableLiveData<UpdatePasswordResponse>()
    var ImageResponse = MutableLiveData<ByteArray>()
    var WorkHistoryResponse = MutableLiveData<GetAllWorkHistoryModelItem>()


    fun requestProfileDetail(
        context: Context,
        UserId: Int,
        progress: ProgressBar
    ): MutableLiveData<RequestProfileResponse> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)
        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.requestprofile("Bearer " + token.toString(),UserId)
                    .enqueue(object : Callback<RequestProfileResponse> {
                        override fun onFailure(call: Call<RequestProfileResponse>, t: Throwable) {
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
                            call: Call<RequestProfileResponse>,
                            response: Response<RequestProfileResponse>
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
    fun createProfile(
        context: Context,
        image: File,
        user : Any,
        progress: ProgressBar,
    ): MutableLiveData<UpdateProfileResponse>{
        progress.visibility = View.VISIBLE
        val requestFile: RequestBody =
            image.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val ResumeFiles =
            MultipartBody.Part.createFormData("image", image.name, requestFile)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                var token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)
                apiService.UpdateProfile("Bearer " + token.toString(),ResumeFiles,user)
                    .enqueue(object : Callback<UpdateProfileResponse> {
                        override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                            progress.visibility = View.GONE

                            Toast.makeText(
                                context,
                                t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<UpdateProfileResponse>,
                            response: Response<UpdateProfileResponse>
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
    fun createProfilewithoutImage(
        context: Context,
        user : Any,
        progress: ProgressBar,
    ): MutableLiveData<UpdateProfileResponse>{
        progress.visibility = View.VISIBLE


        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                var token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)
                apiService.UpdateProfilewithoutImage("Bearer " + token.toString(),user)
                    .enqueue(object : Callback<UpdateProfileResponse> {
                        override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                            progress.visibility = View.GONE

                            Toast.makeText(
                                context,
                                t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<UpdateProfileResponse>,
                            response: Response<UpdateProfileResponse>
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
    fun requestPassChange(
        context: Context,
        ResetRequest: ArrayList<String>,
        progress: ProgressBar
    ): MutableLiveData<UpdatePasswordResponse> {
        progress.visibility = View.VISIBLE
        val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

        try {
            progress.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
                apiService.changePassword("Bearer " + token.toString(),ResetRequest)
                    .enqueue(object : Callback<UpdatePasswordResponse> {
                        override fun onFailure(call: Call<UpdatePasswordResponse>, t: Throwable) {
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
                            call: Call<UpdatePasswordResponse>,
                            response: Response<UpdatePasswordResponse>
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
fun getImage(
    context: Context,
    imageId:String,
    progress: ProgressBar
):MutableLiveData<ByteArray>{
    progress.visibility = View.VISIBLE
    val token = LocalSessionManager.getStringValue(Constant.TOKEN, "", context)

    try {
        progress.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val apiService = ApiClient.apiClient().create(ApiClient.ApiInterface::class.java)
            apiService.requestImage(imageId)
                .enqueue(object : Callback<ByteArray> {
                    override fun onFailure(call: Call<ByteArray>, t: Throwable) {
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
                        call: Call<ByteArray>,
                        response: Response<ByteArray>
                    ) {
                        progress.visibility = View.GONE

                        if (response.code() == 200) {

                            ImageResponse.value = response.body()
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

    return ImageResponse
}

    fun errorMessage(): MutableLiveData<String>? {
        return errorResponse

    }

}