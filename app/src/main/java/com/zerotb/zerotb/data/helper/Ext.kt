package com.zerotb.zerotb.data.helper

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.zerotb.zerotb.BuildConfig
import com.zerotb.zerotb.R
import com.zerotb.zerotb.data.network.Resource
import com.zerotb.zerotb.ui.auth.forgot.ForgotFragment
import com.zerotb.zerotb.ui.auth.login.LoginFragment
import com.zerotb.zerotb.ui.auth.register.RegisterFragment
import com.zerotb.zerotb.ui.home.HomeActivity
import com.zerotb.zerotb.ui.home.home.entry.EntryFragment
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/** For Make Toast a View */
fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

/** For Make Launch Intent a Activity */
fun <A : Activity> Activity.launchActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

/** For Download PDF */
fun pdfPatient() = "${BuildConfig.BASE_URL}/api/report/patient"

fun pdfDrug() = "${BuildConfig.BASE_URL}/api/report/drug"

/** For Make alert Dialog a View */
fun Context.alertDialog(
    msg: String,
    positive: String,
    negative: String,
    listener: DialogInterface.OnClickListener
) {
    val alert = AlertDialog.Builder(this)
    alert.setTitle("Apakah Anda Yakin ?")
    alert.setMessage(msg)
    alert.setPositiveButton(
        positive, listener
    )
    alert.setNegativeButton(
        negative, null
    )
    val alertDialog = alert.create()
    alertDialog.show()
}

/** For Make Visible, Gone and Invisible a View */
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

/**For Email Validation*/
fun isValidEmail(charSequence: CharSequence): Boolean {
    return !TextUtils.isEmpty(charSequence) && Patterns.EMAIL_ADDRESS.matcher(charSequence)
        .matches()
}

/** For Make Format Date*/
fun formatDate(dateString: String): String? {
    try {
        var sd = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sd.timeZone = TimeZone.getTimeZone("GMT")
        val d: Date = sd.parse(dateString)
        sd = SimpleDateFormat("dd MMMM yyyy")
        return sd.format(d)
    } catch (e: ParseException) {
    }
    return ""
}

fun formatDateWithTime(dateString: String): String? {
    try {
        var sd = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sd.timeZone = TimeZone.getTimeZone("GMT")
        val d: Date = sd.parse(dateString)
        sd = SimpleDateFormat("dd MMMM yyyy - HH:mm:ss")
        return sd.format(d)
    } catch (e: ParseException) {
    }
    return ""
}

/** For Make Url from Api */
fun urlAssets() = "${BuildConfig.BASE_URL}/profiles/"

/** For Make Glide a View */
fun ImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .error(R.drawable.ic_broken)
        .into(this)
}

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()
}

/** For Make Multipart Filing a Image Post */
fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}

fun Fragment.logout() = lifecycleScope.launch {
    if (activity is HomeActivity) {
        (activity as HomeActivity).performLogout()
    }
}

/** For Open WhatsApp*/
fun Context.openWhatsAppForDoctor(number: String) {
    val url =
        "https://api.whatsapp.com/send?phone=${number}&text=Halo%20dokter%2C%20hari%20ini%20saya%20ingin%20berkonsultasi%20via%20video%20call%2C%20Apakah%20dokter%20mempunyai%20waktu%20luang%20untuk%20saya%20berkonsultasi%20hari%20ini%20%3F"
    val intent = Intent(Intent.ACTION_VIEW)
    intent.apply {
        data = Uri.parse(url)
    }
    startActivity(intent)
}

/** For Open WhatsApp*/
fun Context.openWhatsAppForPatient(number: String) {
    val url =
        "https://api.whatsapp.com/send?phone=$number"
    val intent = Intent(Intent.ACTION_VIEW)
    intent.apply {
        data = Uri.parse(url)
    }
    startActivity(intent)
}

fun Fragment.handleApiError(
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> requireView().snackbar(
            "Terjadi Kesalahan Pada Server", retry
        )
        failure.errorCode == 401 -> {
            if (this is LoginFragment) {
                requireView().snackbar("Email atau password salah")
            } else {
                logout()
            }
        }
        failure.errorCode == 500 -> {
            if (this is ForgotFragment) {
                requireView().snackbar("Kami tidak dapat menemukan pengguna dengan alamat email tersebut.")
            }
        }
        failure.errorCode == 422 -> {
            if (this is ForgotFragment) {
                requireView().snackbar("Mohon tunggu sebentar untuk coba kembali")
            }
            if (this is RegisterFragment) {
                requireView().snackbar("Email atau Username sudah ada !")
            }
            if (this is EntryFragment) {
                requireView().snackbar("Email atau Username sudah ada !")
            }
        }
        else -> {
            val error = failure.errorBody?.string().toString()
            requireView().snackbar(error)
        }
    }
}