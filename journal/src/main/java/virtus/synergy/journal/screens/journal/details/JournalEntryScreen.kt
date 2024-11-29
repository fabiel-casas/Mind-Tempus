package virtus.synergy.journal.screens.journal.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import virtus.synergy.analytics.ui.elementTag
import virtus.synergy.analytics.ui.flowTag
import virtus.synergy.analytics.ui.screenTag
import virtus.synergy.analytics.ui.trackOnDisplay
import virtus.synergy.design_system.R
import virtus.synergy.design_system.components.MTIconButton
import virtus.synergy.design_system.components.NavigationTopAppBar
import virtus.synergy.design_system.theme.MindTempusTheme

/**
 *
 * Created on 01/05/2023.
 */
@Composable
fun JournalEntryScreen(
    viewModel: JournalDetailsViewModel = koinViewModel(),
    onBackAction: () -> Unit,
    journalId: String,
) {
    LaunchedEffect(key1 = journalId, block = {
        viewModel.loadJournal(journalId = journalId)
    })
    val state = viewModel.state
    JournalEntryScreenContent(
        state = state,
        onBackAction = onBackAction,
        onSaveAction = {
            viewModel.updateJournalNotes()
            onBackAction()
        },
        onJournalEntryChange = { index, paragraph ->
            viewModel.updateEmotionalDescription(index, paragraph)
        },
        onNewRowAdded = { index ->
            viewModel.addNewRow(index)
        },
        onJournalToolAction = { tool ->
            viewModel.updateSelectedParagraph(tool)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JournalEntryScreenContent(
    state: JournalDetailsState,
    onBackAction: () -> Unit,
    onSaveAction: () -> Unit,
    onJournalEntryChange: (index: Int, paragraph: Paragraph) -> Unit,
    onNewRowAdded: (index: Int) -> Unit,
    onJournalToolAction: (JournalParagraphTools) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .flowTag("journal")
            .screenTag("createJournalEntry")
            .trackOnDisplay(),
        topBar = {
            NavigationTopAppBar(
                modifier = Modifier.elementTag("topBar"),
                title = state.journalInfo.value.emoji,
                onBackAction = onBackAction,
                actions = {
                    MTIconButton(
                        modifier = Modifier.elementTag("save"),
                        onClick = onSaveAction
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(id = R.string.emotional_status_save)
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    JournalPage(
                        modifier = Modifier,
                        paragraphs = state.journalInfo.value.paragraph,
                        onJournalEntryChange = onJournalEntryChange,
                        onNewRowAdded = onNewRowAdded,
                    )
                }
                TextEditorTools(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .imePadding(),
                    onToolAction = onJournalToolAction
                )
            }
        }
    )
}

@Composable
private fun TextEditorTools(
    modifier: Modifier,
    onToolAction: (JournalParagraphTools) -> Unit,
) {
    Surface(tonalElevation = 2.dp, contentColor = MaterialTheme.colorScheme.secondary) {
        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .elementTag("bottomBar")
        ) {
            items(journalTools) { tool ->
                MTIconButton(
                    modifier = Modifier.elementTag(tool.title),
                    onClick = { onToolAction(tool) }
                ) {
                    Text(
                        text = tool.title,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun JournalPage(
    modifier: Modifier = Modifier,
    paragraphs: List<Paragraph>,
    onJournalEntryChange: (index: Int, paragraph: Paragraph) -> Unit,
    onNewRowAdded: (index: Int) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
    ) {
        itemsIndexed(paragraphs, key = { _, item -> item.index }) { index, paragraph ->
            EmotionalDescription(
                modifier = Modifier.fillMaxWidth(),
                paragraph = paragraph,
                onParagraphChange = { newParagraph ->
                    onJournalEntryChange(index, newParagraph)
                },
                onAddNewRow = {
                    onNewRowAdded(index)
                }
            )
        }
    }
}

@Composable
fun EmotionalDescription(
    modifier: Modifier,
    paragraph: Paragraph,
    onParagraphChange: (Paragraph) -> Unit,
    onAddNewRow: () -> Unit,
) {
    Row(
        modifier = modifier,
    ) {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(paragraph.index) {
            if (paragraph.isFocused) {
                focusRequester.requestFocus()
            }
        }
        val textStyle = if (paragraph.isTitle) {
            MaterialTheme.typography.titleLarge
        } else {
            MaterialTheme.typography.bodyLarge
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    onParagraphChange(paragraph.copy(isFocused = focusState.isFocused))
                },
            value = paragraph.data,
            onValueChange = { text ->
                onParagraphChange(paragraph.copy(data = text))
            },
            placeholder = {},
            textStyle = textStyle,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Go
            ),
            keyboardActions = KeyboardActions(
                onGo = {
                    onAddNewRow()
                },
            ),
        )
    }
}

@PreviewScreenSizes
@Composable
private fun JournalContentPreview() {
    MindTempusTheme {
        val journalInfo = remember {
            mutableStateOf(
                JournalInfo(
                    title = "Title",
                    paragraph = listOf(
                        Paragraph(
                            isTitle = true,
                            data = "Title"
                        ),
                        Paragraph(
                            data = "Body"
                        )
                    ),
                    emoji = "😊",
                    emotionalIndex = 1
                )
            )
        }
        JournalEntryScreenContent(
            state = JournalDetailsState(journalInfo = journalInfo),
            onBackAction = { /*TODO*/ },
            onSaveAction = {},
            onJournalEntryChange = { _, _ -> },
            onNewRowAdded = { /*TODO*/ },
            onJournalToolAction = { _ -> /*TODO*/ }
        )
    }
}
