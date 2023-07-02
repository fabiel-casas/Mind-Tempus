package virtus.synergy.design_system.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import virtus.synergy.analytics.AnalyticsEvent
import virtus.synergy.analytics.AnalyticsHandler

/**
 *
 * Created on 12/05/2023.
 */

@Composable
fun MTSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    thumbContent: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    colors: SwitchColors = SwitchDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val event = remember {
        mutableStateOf<AnalyticsEvent?>(null)
    }
    Switch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = {
            event.value?.let(AnalyticsHandler::trackEvent)
            onCheckedChange?.invoke(it)
        },
        thumbContent = thumbContent,
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
    )
}