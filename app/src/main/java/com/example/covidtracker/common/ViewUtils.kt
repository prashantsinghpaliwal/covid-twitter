package com.example.covidtracker.common

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewbinding.ViewBinding
import com.example.covidtracker.CovidApp
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.togglePlayState() {
    when (this@togglePlayState.visibility) {
        View.GONE -> this@togglePlayState.visible()
        View.VISIBLE -> this@togglePlayState.gone()
    }
}

fun View.forceToggle() {
    when (this@forceToggle.visibility) {
        View.GONE -> this@forceToggle.visible()
        View.INVISIBLE -> this@forceToggle.visible()
        View.VISIBLE -> Unit
    }
}

fun Double.dpf(): Float {
    if (this == 0.0) {
        return 0f
    }
    val metrics = CovidApp.context.resources?.displayMetrics
    require(metrics != null, { "Metrics is null" })
    return (this * metrics.density).toFloat()
}

fun Int.dp(): Int {
    if (this == 0) {
        return 0
    }
    val metrics = CovidApp.context.resources?.displayMetrics
    require(metrics != null, { "Metrics is null" })
    return (this * metrics.density).toInt()
}

fun Int.dpf(): Float {
    if (this == 0) {
        return 0f
    }
    val metrics = CovidApp.context.resources?.displayMetrics
    require(metrics != null, { "Metrics is null" })
    return this * metrics.density
}

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }

inline fun <T : ViewBinding> Fragment.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }

fun Uri.mimeType(): String {
    return when (scheme) {
        ContentResolver.SCHEME_CONTENT -> CovidApp.context.contentResolver.getType(this)
            ?: "image/*"
        ContentResolver.SCHEME_FILE -> {
            return MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(
                    MimeTypeMap.getFileExtensionFromUrl(toString()).toLowerCase(Locale.getDefault())
                )
                ?: "image/*"
        }
        else -> "image/*"
    }
}

/**
 *
 * Sender usage
 * intent.putExtra(EnumName.SOMETHING)
 * Receiver usage
 * val result = intent.getEnumExtra<EnumName>()
 */
inline fun <reified T : Enum<T>> Intent.putExtra(victim: T): Intent =
    putExtra(T::class.java.name, victim.ordinal)

inline fun <reified T : Enum<T>> Intent.getEnumExtra(): T? =
    getIntExtra(T::class.java.name, -1)
        .takeUnless { it == -1 }
        ?.let { T::class.java.enumConstants?.get(it) }

fun Bundle.putEnum(key: String, enum: Enum<*>) {
    this.putString(key, enum.name)
}

inline fun <reified T : Enum<T>> Bundle.getEnumExtra(): T? {
    return getInt(T::class.java.name, -1)
        .takeUnless { it == -1 }
        ?.let { T::class.java.enumConstants?.get(it) }
}

fun <T : RecyclerView> T.removeDecoration() {
    while (itemDecorationCount > 0) {
        removeItemDecorationAt(0)
    }
}

inline fun <T1 : Any, T2 : Any, R : Any> mlet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, R : Any> mlet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    block: (T1, T2, T3) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> mlet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    block: (T1, T2, T3, T4) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, R : Any> mlet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    p5: T5?,
    block: (T1, T2, T3, T4, T5) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) block(
        p1,
        p2,
        p3,
        p4,
        p5
    ) else null
}


fun RecyclerView.withoutAnimation() {
    (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
}

fun getOrEmpty(item: String?, binding: String = ""): String = item ?: binding

private val ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

//2021-05-04T14:59:23.000Z
fun String.toUtc(): Date? {
    val df = SimpleDateFormat(ISO_8601_24H_FULL_FORMAT)
    return df.parse(this)
}

fun Date.toUtc(): Date {
    val dt = DateTime(this)
    val dtZone = DateTimeZone.UTC
    val dtus = dt.withZone(dtZone)
    return dtus.toLocalDateTime().toDate()
}

fun String.isAfter(startDate: Date): Boolean {
    val d = this.toUtc()
    if (d == null) {
        throw ParseException("Time Stamp has invalid format", 0)
    } else {
        return d.after(startDate)
    }
}


fun Group.addOnClickListener(listener: (view: View) -> Unit) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(listener)
    }
}