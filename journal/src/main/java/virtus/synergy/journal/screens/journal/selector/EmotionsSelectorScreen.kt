package virtus.synergy.journal.screens.journal.selector

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import virtus.synergy.analytics.ui.analyticsProperties
import virtus.synergy.analytics.ui.elementTag
import virtus.synergy.analytics.ui.flowTag
import virtus.synergy.analytics.ui.screenTag
import virtus.synergy.analytics.ui.trackOnDisplay
import virtus.synergy.analytics.ui.trackableClick
import virtus.synergy.design_system.R
import virtus.synergy.design_system.components.NavigationTopAppBar
import virtus.synergy.design_system.theme.MindTempusTheme

/**
 *
 * Created on 10/05/2023.
 */
@Composable
fun EmotionsSelectorScreen(
    viewModel: EmotionsSelectorViewModel = koinViewModel(),
    onBackAction: () -> Unit,
    onNavigateToJournal: (journalId: String) -> Unit,
) {
    LaunchedEffect(key1 = viewModel.navigationEvent.value, block = {
        viewModel.navigationEvent.value?.let {
            onNavigateToJournal(it)
        }
    })
    EmotionsSelectorContent(
        onBackAction = onBackAction,
        state = viewModel.state,
        onEmotionLevelChange = {
            viewModel.updateEmotionLevel(it)
        },
        onEmotionSelectedAction = {
            viewModel.onEmojiSelected(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionsSelectorContent(
    state: EmotionsSelectorState,
    onBackAction: () -> Unit,
    onEmotionLevelChange: (Float) -> Unit,
    onEmotionSelectedAction: (String) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .flowTag("journal")
            .screenTag("emotionalLevel")
            .elementTag("emotionalLevelScreen")
            .trackOnDisplay(),
        topBar = {
            NavigationTopAppBar(
                title = "",
                onBackAction = onBackAction,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp),
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.emotionSelector_text_what_is_your_emotional_level),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            val slideValue = remember(state.emotionalLevel.value) {
                mutableStateOf(state.emotionalLevel.value)
            }
            Slider(
                value = slideValue.value,
                onValueChange = {
                    slideValue.value = it
                },
                valueRange = 1f..5f,
                onValueChangeFinished = {
                    onEmotionLevelChange(slideValue.value)
                },
                steps = 3
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.emotional_selector_slider_negative),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = stringResource(R.string.emotional_selector_slider_positive),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (state.emojis.value.isNotEmpty()) {
                AnimatedCard(
                    state = state,
                    onEmotionSelectedAction = onEmotionSelectedAction
                )
            }
        }
    }
}

// Keep this; is required for production builds.
@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimatedCard(
    state: EmotionsSelectorState,
    onEmotionSelectedAction: (String) -> Unit
) {
    AnimatedContent(targetState = state.emotionalLevel.value) {
        Card(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .aspectRatio(1f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 16.dp
            )
        ) {
            LazyVerticalGrid(
                modifier = Modifier.padding(16.dp),
                columns = GridCells.Adaptive(minSize = 70.dp),
                content = {
                    items(state.emojis.value) { item ->
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .elementTag("emoji")
                                .analyticsProperties(
                                    Pair(
                                        "emotionalLevel",
                                        state.emotionalLevel.value.toString()
                                    )
                                )
                                .analyticsProperties(Pair("emoji", item))
                                .trackableClick { onEmotionSelectedAction(item) },
                            text = item,
                            style = MaterialTheme.typography.displayLarge,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmotionsSelectorScreenPreview() {
    MindTempusTheme {
        EmotionsSelectorContent(
            state = remember {
                EmotionsSelectorState()
            },
            onBackAction = {},
            onEmotionLevelChange = {},
            onEmotionSelectedAction = {},
        )
    }
}