package virtus.synergy.analytics.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.semantics.Role
import virtus.synergy.analytics.AnalyticsAction
import virtus.synergy.analytics.AnalyticsEvent
import virtus.synergy.analytics.AnalyticsHandler
import virtus.synergy.analytics.ElementType


@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.flowTag(tag: String) = this
    .modifierLocalProvider(ModifierLocalFlowTag) { tag }

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.screenTag(tag: String) = this
    .modifierLocalProvider(ModifierLocalScreenHolder) { tag }

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.elementTag(
    tag: String,
    type: ElementType = ElementType.BUTTON,
) = this
    .modifierLocalProvider(ModifierLocalElementHolder) { tag }
    .modifierLocalProvider(ModifierLocalAnalyticsType) { type }

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.analyticsProperties(properties: Map<String, String>?) = this
    .modifierLocalProvider(ModifierLocalAnalyticsProperties) { properties }

fun Modifier.analyticsProperties(vararg properties: Pair<String, String>) = analyticsProperties(
    mapOf(*properties)
)

fun Modifier.trackableClick(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
) = this.composed {
    var event by remember { mutableStateOf<AnalyticsEvent?>(null) }
    currentAnalyticsEvent() { event = it }
        .clickable(
            enabled = enabled,
            onClickLabel = onClickLabel,
            role = role,
            onClick = {
                event?.let { AnalyticsHandler.trackEvent(it) }
                onClick()
            }
        )
}

fun Modifier.trackableLongPress(onLongPress: (Offset) -> Unit) = this.composed {
    var event by remember { mutableStateOf<AnalyticsEvent?>(null) }
    currentAnalyticsEvent(AnalyticsAction.LONG_PRESS) { event = it }
        .pointerInput(Unit) {
            detectTapGestures(
                onLongPress = {
                    event?.let(AnalyticsHandler::trackEvent)
                    onLongPress(it)
                }
            )
        }
}

fun Modifier.trackOnDisplay() = this.composed {
    var isTracked by remember { mutableStateOf(false) }
    currentAnalyticsEvent(
        callback = {
            if (!isTracked) {
                isTracked = true
                AnalyticsHandler.trackEvent(it)
            }
        },
        actionType = AnalyticsAction.VIEW,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.currentFlowTag(callback: (String) -> Unit) =
    modifierLocalConsumer {
        ModifierLocalFlowTag.current?.let(callback)
    }

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.currentScreenTag(callback: (String) -> Unit) =
    modifierLocalConsumer {
        ModifierLocalScreenHolder.current?.let(callback)
    }

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.currentAnalyticsProperties(callback: (Map<String, String>) -> Unit) =
    modifierLocalConsumer {
        ModifierLocalAnalyticsProperties.current?.let(callback)
    }

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.currentAnalyticsEvent(
    actionType: AnalyticsAction = AnalyticsAction.TAP,
    callback: (AnalyticsEvent) -> Unit
) = this.composed {
    var flowTag by remember { mutableStateOf<String?>(null) }
    var screenTag by remember { mutableStateOf<String?>(null) }
    var elementTag by remember { mutableStateOf<String?>(null) }
    var properties by remember { mutableStateOf<Map<String, String>?>(null) }
    var type by remember { mutableStateOf(ElementType.BUTTON) }

    LaunchedEffect(flowTag, screenTag, elementTag, properties) {
        runWhenNotNull(flowTag, screenTag, elementTag) { (flowTag, screenTag, elementTag) ->
            val event = AnalyticsEvent(
                flow = flowTag,
                screen = screenTag,
                element = elementTag,
                action = actionType,
                elementType = type,
                extraProperties = properties
            )
            callback(event)
        }
    }
    Modifier
        .modifierLocalConsumer { ModifierLocalAnalyticsType.current?.let { type = it } }
        .modifierLocalConsumer { ModifierLocalAnalyticsProperties.current?.let { properties = it } }
        .modifierLocalConsumer { flowTag = ModifierLocalFlowTag.current }
        .modifierLocalConsumer { screenTag = ModifierLocalScreenHolder.current }
        .modifierLocalConsumer { elementTag = ModifierLocalElementHolder.current }
}

@Composable
fun TrackableComposableWrapper(
    content: @Composable (trackableModifier: Modifier) -> Unit,
) {
    var flowTag by remember { mutableStateOf<String?>(null) }
    var screenTag by remember { mutableStateOf<String?>(null) }
    var properties by remember { mutableStateOf<Map<String, String>?>(null) }
    Box(modifier = Modifier
        .currentFlowTag { flowTag = it }
        .currentScreenTag { screenTag = it }
        .currentAnalyticsProperties { properties = it }
    ) {
        content(
            Modifier
                .flowTag(flowTag.orEmpty())
                .screenTag(screenTag.orEmpty())
                .analyticsProperties(properties)
        )
    }
}

fun <T> runWhenNotNull(vararg args: T?, predicate: (args: List<T>) -> Unit) {
    with(args.mapNotNull { it }) {
        if (this.size == args.size) {
            predicate(this)
        }
    }
}

val ModifierLocalFlowTag = modifierLocalOf<String?> { null }
val ModifierLocalScreenHolder = modifierLocalOf<String?> { null }
val ModifierLocalElementHolder = modifierLocalOf<String?> { null }
val ModifierLocalAnalyticsProperties = modifierLocalOf<Map<String, String>?> { null }
val ModifierLocalAnalyticsType = modifierLocalOf<ElementType?> { null }
