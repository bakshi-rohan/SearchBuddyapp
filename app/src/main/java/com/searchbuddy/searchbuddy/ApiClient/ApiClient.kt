package com.searchbuddy.searchbuddy.ApiClient

import UpdateEducationDetailRequest
import com.google.gson.GsonBuilder
import com.searchbuddy.searchbuddy.model.AppliedJobsModel
import com.searchbuddy.searchbuddy.model.Edu.BasicDetailUpdateRequest
import com.searchbuddy.searchbuddy.model.Edu.EducationDetailModel
import com.searchbuddy.searchbuddy.model.Edu.PrefrencesModel
import com.searchbuddy.searchbuddy.model.Edu.UpdateEduResponse
import com.searchbuddy.searchbuddy.model.GetAllWorkHistoryModelItem
import com.searchbuddy.searchbuddy.model.GetQuestionModel
import com.searchbuddy.searchbuddy.model.GetSavedJobsResponse
import com.searchbuddy.searchbuddy.model.JobRequestModel
import com.searchbuddy.searchbuddy.model.JobRequestResponse
import com.searchbuddy.searchbuddy.model.JobdescriptionResponse
import com.searchbuddy.searchbuddy.model.LoginRequest
import com.searchbuddy.searchbuddy.model.LoginResponse
import com.searchbuddy.searchbuddy.model.LoginWithOTPResponse
import com.searchbuddy.searchbuddy.model.PersonalDetailModel
import com.searchbuddy.searchbuddy.model.ProfessionalDetailModel
import com.searchbuddy.searchbuddy.model.RecommendedjobData
import com.searchbuddy.searchbuddy.model.RequestCompanyResponse
import com.searchbuddy.searchbuddy.model.RequestProfileResponse
import com.searchbuddy.searchbuddy.model.ResetPasswordResponse
import com.searchbuddy.searchbuddy.model.SaveCompanyRequest
import com.searchbuddy.searchbuddy.model.SaveJobsResponse
import com.searchbuddy.searchbuddy.model.SaveWorkHistoryResponse
import com.searchbuddy.searchbuddy.model.SignupResponse
import com.searchbuddy.searchbuddy.model.SubmitResponse
import com.searchbuddy.searchbuddy.model.TopcomapniesResponse
import com.searchbuddy.searchbuddy.model.UpdatePasswordResponse
import com.searchbuddy.searchbuddy.model.UpdatePrefRequest
import com.searchbuddy.searchbuddy.model.UpdatePrefResponse
import com.searchbuddy.searchbuddy.model.UpdateProfileResponse
import com.searchbuddy.searchbuddy.model.UploadResumeResponse
import com.searchbuddy.searchbuddy.model.aa.PreAppliedResponse
import com.searchbuddy.searchbuddy.model.aa.PreJobsResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit


class ApiClient {
    companion object {
        private const val BASE_URL: String = "https://www.searchbuddy.in/api/"


        fun apiClient(): Retrofit {
            val gson = GsonBuilder().setLenient().create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS).addInterceptor(interceptor).build()
//            val rxAdapter = RxJava3CallAdapterFactory.create()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
    }

    class NullOnEmptyConverterFactory : Converter.Factory() {
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
        ): Converter<ResponseBody, *> {
            val delegate: Converter<ResponseBody, *> =
                retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
            return Converter { body ->
                if (body.contentLength() == 0L) null else delegate.convert(
                    body
                )
            }
        }
    }

    interface ApiInterface {
        @Headers("Content-Type:application/json")
        @POST("login/candidate/mobile")
        fun requestLogin(@Body login: LoginRequest): Call<LoginResponse>
        @Headers("Content-Type:application/json")
        @POST("login/mobile")
        fun requestLoginwithOtp(@Body mobileNo: Any): Call<LoginWithOTPResponse>

        @Headers("Content-Type:application/json")
        @POST("login/otp")
        fun verifyOtp(@Body mobileNo: Any): Call<LoginResponse>

        @Headers("Content-Type:application/json")
        @POST("save-positions")
        fun saveJob(@Header("authorization") token: String,@Body id: Any): Call<SaveJobsResponse>

        @Headers("Content-Type:application/json")
        @DELETE("save-positions/{id}")
        fun deleteJob(@Header("authorization") token: String,@Path("id") id: Any): Call<SaveJobsResponse>
        @Headers("Content-Type:application/json")
        @GET("logout")
        fun requestLogout(@Header("authorization") token: String): Call<Void>

        @Headers("Content-Type:application/json")
        @GET("organisation/top")
        fun requestTopcompanies(@Header("authorization") token: String): Call<TopcomapniesResponse>

        @Headers("Content-Type:application/json")
        @GET("screen/mobile/questions/{Position}")
        fun requestQuestion(
            @Path("Position") Position: Int,
            ): Call<GetQuestionModel>

        @Headers("Content-Type:application/json")
        @GET("candidate/work-history/all")
        fun requestallhistory(@Header("authorization") token: String): Call<GetAllWorkHistoryModelItem>
//            @Headers("Content-Type:application/json")
//            @POST("reset-password")
//            fun requestResetPassword(@Body reset: ResetPasswordRequest): Call<ResetPasswordResponse>

        @Headers("Content-Type:application/json")
        @POST("reset-password")
        fun requestResetPassword(@Body reset: ArrayList<String>): Call<ResetPasswordResponse>

        @Headers("Content-Type:application/json")
        @POST("search/positions")
        fun requestJobs(@Body reset: JobRequestModel): Call<JobRequestResponse>

        @Headers("Content-Type:application/json")
        @POST("https://testingsales.searchbuddy.in/api/external/premium-jobs")
        fun requestPremiumJobs(@Body reset: JobRequestModel): Call<PreJobsResponse>
        @Headers("Content-Type:application/json")
        @POST("search/logged/positions")
        fun requestJobsLogged(
            @Header("authorization") token: String,
            @Body reset: JobRequestModel): Call<JobRequestResponse>

        @Headers("Content-Type:application/json")
        @POST("update-password")
        fun changePassword(
            @Header("authorization") token: String,
            @Body reset: ArrayList<String>
        ): Call<UpdatePasswordResponse>

        @Headers("Content-Type: application/json")
        @PUT("candidate/basic-detail")
        fun requestBasicUpdate(
            @Header("authorization") token: String,
            @Body reset: BasicDetailUpdateRequest
        ): Call<Unit>

        @Headers("Content-Type: application/json")
        @PUT("candidate/educational-detail")
        fun requestEduUpdate(
            @Header("authorization") token: String,
            @Body reset: UpdateEducationDetailRequest
        ): Call<UpdateEduResponse>

        @Headers("Content-Type: application/json")
        @PUT("candidate/preferences-detail")
        fun requestPrefUpdate(
            @Header("authorization") token: String,
            @Body reset: UpdatePrefRequest
        ): Call<UpdatePrefResponse>


        @Headers("Content-Type:application/json", "Accept:application/json")
        @GET("candidate/progress/{UserId}")
        fun requestAppliedJobs(
            @Header("authorization") token: String,
            @Path("UserId") UserId: Int,
            @Query("pagesize") pagesize: Int,
            @Query("index") index: Int
        ): Call<AppliedJobsModel>

        @Headers("Content-Type:application/json", "Accept:application/json")
        @GET("external/candidate-progress")
        fun requestPreAppliedJobs(
            @Query("pagesize") pagesize: Int,
            @Query("index") index: Int,
            @Query("email",encoded = true) email: String
        ): Call<PreAppliedResponse>
        @Headers("Content-Type:application/json", "Accept:application/json")
        @GET("save-positions/mobile")
        fun requestSavedJobs(
            @Header("authorization") token: String,
            @Query("pagesize") pagesize: Int,
            @Query("index") index: Int
        ): Call<GetSavedJobsResponse>
        @Headers("Content-Type:application/json", "Accept:application/json")
        @GET("candidate/basic-detail/{UserId}")
        fun requestPersonalDetail(
            @Header("authorization") token: String,
            @Path("UserId") UserId: Int
        ): Call<PersonalDetailModel>

        @Headers("Content-Type:application/json", "Accept:application/json")
        @GET("user/{UserId}")
        fun requestprofile(
            @Header("authorization") token: String,
            @Path("UserId") UserId: Int
        ): Call<RequestProfileResponse>


        @Headers("Content-Type:application/json", "Accept:application/json")
        @GET("candidate/professional-detail/{UserId}")
        fun requestProfessionalDetail(
            @Header("authorization") token: String,
            @Path("UserId") UserId: Int
        ): Call<ProfessionalDetailModel>

        @Headers("Content-Type:application/json", "Accept:application/json")
        @GET("candidate/educational-detail/{UserId}")
        fun requestEducation(
            @Header("authorization") token: String,
            @Path("UserId") UserId: Int
        ): Call<EducationDetailModel>

        @GET("get-picture/profilepicture/{ImageId}")
        fun requestImage(
            @Path("ImageId") Imageid: String
        ): Call<ByteArray>

        @Headers("Content-Type:application/json", "Accept:application/json")
        @GET("candidate/work-history")
        fun RequestCompany(
            @Header("authorization") token: String,
            @Query("companyName") companyName:String
            ):Call<RequestCompanyResponse>

        @Headers("Content-Type:application/json", "Accept:application/json")
        @PUT("candidate/work-history")
        fun UpodateCompany(
            @Header("authorization") token: String,
            @Query("oldCompanyName") companyName:String,
            @Body login: SaveCompanyRequest
        ):Call<SaveWorkHistoryResponse>

        @Headers("Content-Type:application/json")
        @POST("candidate/work-history")
        fun saveCompany(@Header("authorization") token: String,@Body login: SaveCompanyRequest): Call<SaveWorkHistoryResponse>


        @Headers("Content-Type:application/json", "Accept:application/json")
        @GET("candidate/preferences-detail/{UserId}")
        fun requestPrefrences(
            @Header("authorization") token: String,
            @Path("UserId") UserId: Int
        ): Call<PrefrencesModel>

        @Multipart
        @POST("https://www.searchbuddy.co.in/parse")
        fun uploadResume(
            @Part resumeFile: MultipartBody.Part,
        ): Call<UploadResumeResponse>

        @Multipart
        @PUT("candidate/upload-resume")
        fun UploaResume(
            @Header("authorization") token: String,
            @Part resume: MultipartBody.Part,
            @Part ("information") information: String,
        ): Call<Unit>
        @Multipart
        @POST("candidate/profile/video")
        fun UploadVideoResume(
            @Header("authorization") token: String,
            @Part video: MultipartBody.Part,
            @Part ("candidateId") candidateId: Int,
        ): Call<Unit>
        @Multipart
        @POST("screen/self")
        fun SubmitResponse(
            @Header("authorization") token: String,
            @Part ("information") information: String,
        ): Call<SubmitResponse>

        @Headers("Content-Type:application/json", "Accept:application/json")
        @GET("dashboard/candidate/info")
        fun getRecommendedJobs(@Header("authorization") token: String): Call<RecommendedjobData>

        @Multipart
        @PUT("candidate/professional-detail")
        fun UpdateProfessionalDetail(
            @Header("authorization") token: String,
            @Part("resume") resume: String,
            @Part ("information") information: String,
        ): Call<UpdatePrefResponse>

        @Multipart
        @POST("https://www.searchbuddy.in/api/register/candidate/mobile")
        fun CreateProfile(
            @Part("information") information: String
        ): Call<SignupResponse>

        @Multipart
        @POST("profile")
        fun UpdateProfile(
            @Header("authorization") token: String,
            @Part image: MultipartBody.Part,
            @Part("user") user: Any
        ): Call<UpdateProfileResponse>
        @Multipart
        @POST("profile")
        fun UpdateProfilewithoutImage(
            @Header("authorization") token: String,
            @Part("user") user: Any
        ): Call<UpdateProfileResponse>
        @Headers("Content-Type:application/json", "Accept:application/json")
        @GET("https://www.searchbuddy.in/api/position/jd/detail/{position_id}")
        fun getJobdescription(
            @Path("position_id") id: Int
        ): Call<JobdescriptionResponse>
        @Headers("Content-Type:application/json", "Accept:application/json")
        @GET("https://www.searchbuddy.in/api/position/candidate/jd/detail/{position_id}")
        fun getLoggedJobdescription(
            @Header("authorization") token: String,
            @Path("position_id") id: Int
        ): Call<JobdescriptionResponse>
    }

}