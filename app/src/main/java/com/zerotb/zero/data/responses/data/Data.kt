package com.zerotb.zero.data.responses.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val created_at: String,
    val email: String,
    val gender: String,
    val hp: String,
    val id: Int,
    val medical: String,
    val name: String,
    val profile: String?,
    val role: String,
    val updated_at: String,
    val username: String
) : Parcelable

data class Doctor(
    val name: String,
    val title: String,
    val imageId: Int
)

data class Patient(
    val name: String,
    val title: String,
    val imageId: Int
)

data class Settings(
    val name: String,
    val title: String,
    val imageId: Int
)

data class Report(
    val name: String,
    val title: String,
    val imageId: Int
)

data class Consult(
    val id_consult: Int,
    val patient: String,
    val consult: String,
    val doctor: String,
    val created_at: String,
    val updated_at: String,
)

data class Result(
    val message_id: String
)

@Parcelize
data class Pill(
    val created_at: String,
    val doctor: String,
    val drug: String,
    val hour: String,
    val id_drug: Int,
    val user: Int,
    val patient: String,
    val medical: String,
    val updated_at: String
) : Parcelable