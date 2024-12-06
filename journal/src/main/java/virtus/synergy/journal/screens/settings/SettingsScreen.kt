package virtus.synergy.journal.screens.settings


import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel
import virtus.synergy.analytics.ElementType
import virtus.synergy.analytics.ui.elementTag
import virtus.synergy.analytics.ui.flowTag
import virtus.synergy.analytics.ui.screenTag
import virtus.synergy.design_system.R
import java.time.ZonedDateTime


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    onBackAction: () -> Unit,
    viewModel: SettingViewModel = koinViewModel(),
    onOpenExternalActivity: (Intent) -> Unit
) {
    LaunchedEffect(key1 = viewModel, block = {
        viewModel.loadSettings()
    })
    val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }
    val isPermissionGranted =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionState?.status?.isGranted == true
        } else {
            true
        }
    val context = LocalContext.current
    SettingContent(
        onBackAction = onBackAction,
        state = viewModel.state,
        onNotificationTimeSelected = { selectedTime ->
            viewModel.saveNotificationTime(selectedTime)
        },
        onEmojiTimeSelected = { selectedTime ->
            viewModel.saveEmojiTime(selectedTime)
        },
        onNotificationSwitchChange = { isChecked ->
            if (isChecked) {
                permissionState?.launchPermissionRequest()
            } else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${context.packageName}")
                onOpenExternalActivity(intent)
            }
        },
        isPermissionGranted = isPermissionGranted
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingContent(
    onBackAction: () -> Unit,
    state: SettingsState,
    onNotificationTimeSelected: (ZonedDateTime) -> Unit,
    onEmojiTimeSelected: (ZonedDateTime) -> Unit,
    onNotificationSwitchChange: (Boolean) -> Unit,
    isPermissionGranted: Boolean,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .flowTag("settings")
            .screenTag("mainSettings"),
        topBar = {
            virtus.synergy.design_system.components.NavigationTopAppBar(
                title = stringResource(R.string.mind_tempus_settings),
                onBackAction = onBackAction,
            )
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                SwitchComponent(
                    isPermissionGranted = isPermissionGranted,
                    onSwitchChange = onNotificationSwitchChange
                )
                if (isPermissionGranted) {
                    Divider()
                    val datePicker = virtus.synergy.design_system.components.alertDatePicker(
                        currentZonedDateTime = state.settingsInfo.value.notificationTime,
                        onTimeSelected = onNotificationTimeSelected,
                    )
                    ButtonItemComponent(
                        modifier = Modifier.elementTag("notificationTime"),
                        onClick = {
                            datePicker.show()
                        },
                        time = state.settingsInfo.value.notificationDisplayTime,
                        itemName = stringResource(R.string.settings_screen_button_notification_time)
                    )
                    Divider()
                }
                val emojiDatePicker = virtus.synergy.design_system.components.alertDatePicker(
                    currentZonedDateTime = state.settingsInfo.value.emojiTime,
                    onTimeSelected = onEmojiTimeSelected,
                )
                ButtonItemComponent(
                    modifier = Modifier.elementTag("emojiExpTime"),
                    onClick = {
                        emojiDatePicker.show()
                    },
                    time = state.settingsInfo.value.emojiDisplayTime,
                    itemName = stringResource(R.string.settings_screen_button_emoji_time)
                )
            }
        }
    )
}

@Composable
private fun ButtonItemComponent(
    modifier: Modifier,
    itemName: String,
    onClick: () -> Unit,
    time: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_time_stopwatch),
            contentDescription = null,
            modifier = Modifier.requiredSize(24.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = itemName,
            style = MaterialTheme.typography.bodyLarge,
        )
        virtus.synergy.design_system.components.MTAssistChip(
            modifier = modifier,
            onClick = onClick,
            label = {
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        )
    }
}

@Composable
private fun SwitchComponent(
    isPermissionGranted: Boolean,
    onSwitchChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_notification_alarm_bell),
            contentDescription = null,
            modifier = Modifier.requiredSize(24.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.settings_screen_switch_push_notifications),
            style = MaterialTheme.typography.bodyLarge,
        )
        virtus.synergy.design_system.components.MTSwitch(
            modifier = Modifier.elementTag("permissions", ElementType.SWITCH),
            checked = isPermissionGranted,
            onCheckedChange = onSwitchChange
        )
    }
}

@Preview
@Composable
private fun SettingContentPreview() {
    virtus.synergy.design_system.theme.MindTempusTheme {
        SettingContent(
            onBackAction = {},
            state = remember { SettingsState() },
            onNotificationTimeSelected = {},
            onEmojiTimeSelected = {},
            onNotificationSwitchChange = {},
            isPermissionGranted = true,
        )
    }
}




