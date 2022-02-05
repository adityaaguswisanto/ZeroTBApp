package com.zerotb.zerotb.data.network

import com.zerotb.zerotb.data.responses.auth.*
import com.zerotb.zerotb.data.responses.consult.BookResponse
import com.zerotb.zerotb.data.responses.consult.ConsultResponse
import com.zerotb.zerotb.data.responses.patient.PatientResponse
import com.zerotb.zerotb.data.responses.pill.DrugResponse
import com.zerotb.zerotb.data.responses.pill.PillResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface MyApi {

    @FormUrlEncoded
    @POST("api/auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("fcm") fcm: String,
    ): LoginResponse

    @FormUrlEncoded
    @POST("api/auth/forgot")
    suspend fun forgot(
        @Field("email") email: String,
    ): ForgotResponse

    @FormUrlEncoded
    @POST("api/auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("gender") gender: String,
        @Field("hp") hp: String,
        @Field("password") password: String,
        @Field("role") role: String,
        @Field("medical") medical: String,
    ): RegisterResponse

    @GET("api/auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String,
    ): LogoutResponse

    @GET("api/auth/user")
    suspend fun user(
        @Header("Authorization") token: String,
    ): UserResponse

    @Multipart
    @POST("api/auth/profile")
    suspend fun profile(
        @Header("Authorization") token: String,
        @Part profile: MultipartBody.Part,
    ): ProfileResponse

    @FormUrlEncoded
    @PUT("api/auth/user/{id}")
    suspend fun update(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("gender") gender: String,
        @Field("hp") hp: String,
    ): ProfileResponse

    @GET("api/patient/")
    suspend fun patient(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("q") q: String,
    ): PatientResponse

    @GET("api/pill/")
    suspend fun pill(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("q") q: Int,
    ): PillResponse

    @FormUrlEncoded
    @POST("api/drug")
    suspend fun drug(
        @Header("Authorization") token: String,
        @Field("drug") drug: String,
        @Field("hour") hour: String,
        @Field("user") user: Int,
        @Field("medical") medical: String,
        @Field("patient") patient: String,
        @Field("doctor") doctor: String,
    ): DrugResponse

    @DELETE("api/drug/{id}")
    suspend fun drug(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): DrugResponse

    @FormUrlEncoded
    @PUT("api/drug/{id}")
    suspend fun drug(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("drug") drug: String,
        @Field("hour") hour: String,
        @Field("user") user: Int,
        @Field("medical") medical: String,
        @Field("patient") patient: String,
        @Field("doctor") doctor: String,
    ): DrugResponse

    @FormUrlEncoded
    @PUT("api/auth/medical/{id}")
    suspend fun medical(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("medical") medical: String,
    ): ProfileResponse

    @GET("api/consult/")
    suspend fun consult(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
    ): ConsultResponse

    @FormUrlEncoded
    @POST("api/consult")
    suspend fun book(
        @Header("Authorization") token: String,
        @Field("patient") patient: String,
        @Field("consult") consult: String,
        @Field("doctor") doctor: String,
    ): BookResponse

}