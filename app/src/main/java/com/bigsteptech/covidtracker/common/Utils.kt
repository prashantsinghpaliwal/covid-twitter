package com.bigsteptech.covidtracker.common

import android.content.Context
import android.util.Log
import java.util.*

object Utils {

    fun doesPackageExist(context: Context, targetPackage: String): Boolean {
        val pm = context.packageManager
        val packages = pm.getInstalledApplications(0)
        for (packageInfo in packages) {
            if (packageInfo.packageName == targetPackage) return true
        }
        return false
    }


    fun getAgoTimeString(postedDate: Date): String {
        var millisUntilFinished = Date(System.currentTimeMillis()).toUtc().time - postedDate.time
        Log.v("TweetLogs", "timeDiff : ${millisUntilFinished}")

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val days: Long = millisUntilFinished / daysInMilli
        millisUntilFinished = millisUntilFinished % daysInMilli

        val hours: Long = millisUntilFinished / hoursInMilli
        millisUntilFinished = millisUntilFinished % hoursInMilli

        val minutes: Long = millisUntilFinished / minutesInMilli
        millisUntilFinished = millisUntilFinished % minutesInMilli

        val seconds: Long = millisUntilFinished / secondsInMilli

        var timeAgo = ""
        if (days > 0) {
            timeAgo = "${days}d ago "
        } else if (days.toInt() == 0) {
            if (hours > 0)
                timeAgo = "${hours}h ago "
            else if (hours.toInt() == 0) {

                if (minutes > 0)
                    timeAgo = "${minutes}m ago "
                else timeAgo = "Just Now"
            }
        }

        val timeLeft = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        Log.v("TweetLogs", "timeDiff : ${timeLeft} timeAgo : ${timeAgo}")

        return timeAgo
    }
}