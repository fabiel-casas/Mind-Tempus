package virtus.synergy.design_system.components

import android.app.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 *
 * Created on 08/05/2023.
 */
@Composable
fun alertDatePicker(
    currentZonedDateTime: ZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault()),
    onTimeSelected: (ZonedDateTime) -> Unit,
): TimePickerDialog {
    val time = currentZonedDateTime.toLocalTime()
    val context = LocalContext.current
    return TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            val localTime = LocalTime.of(hour, minute)
            val pickedDate = ZonedDateTime.of(
                currentZonedDateTime.toLocalDate(),
                localTime,
                ZoneId.systemDefault()
            )
            onTimeSelected(pickedDate)
        },
        time.hour,
        time.minute,
        false
    )
}