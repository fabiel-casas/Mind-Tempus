package virtus.synergy.core

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 *
 * Created on 01/05/2023.
 */

fun ZonedDateTime.toHourMinutes(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return this.format(formatter)
}

fun ZonedDateTime.toHourMinutesSeconds(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    return this.format(formatter)
}

fun ZonedDateTime.toDayMonthDay(): String {
    val formatter = DateTimeFormatter.ofPattern("EEE, MMM dd")
    return this.format(formatter).capitalize()
}

fun ZonedDateTime.toDayMonthYearTime(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy HH:mm")
    return this.format(formatter).capitalize()
}

fun ZonedDateTime.toDayOfTheWeek(textStyle: TextStyle = TextStyle.FULL): String {
    return this.dayOfWeek.getDisplayName(textStyle, Locale.getDefault())
}

fun ZonedDateTime.toMonthYear(): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM - yyyy")
    return this.format(formatter).capitalize()
}