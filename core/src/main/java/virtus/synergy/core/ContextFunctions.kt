package virtus.synergy.core

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.widget.Toast

/**
 * Mind Tempus
 * Created on 29/05/2023.
 * Author: johan
 */
fun Context.isDarkMode(): Boolean {
    val configuration: Configuration = resources.configuration
    val currentNightMode: Int = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == Configuration.UI_MODE_NIGHT_YES
}

fun Context.showToast(text: Int) {
    showToast(getString(text))
}

fun Context.showToast(text: String) {
    Toast.makeText(
        this,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

fun Context.alarmManager(): AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

fun Context.notifications(): NotificationManager =
    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager