package virtus.synergy.journal.screens.journal.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.mandatorySystemGesturesPadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
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
import virtus.synergy.journal.ui.PageRowInput
import virtus.synergy.journal.ui.TextEditorTools

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
            viewModel.onSaveJournalNotes()
            onBackAction()
        },
        onJournalEntryChanged = viewModel::onParagraphTextChanged,
        onParagraphFocusChanged = viewModel::onParagraphFocusChanged,
        onNewRowAdded = viewModel::onAddNewRow,
        onJournalToolAction = viewModel::onToolActionSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JournalEntryScreenContent(
    state: JournalDetailsState,
    onBackAction: () -> Unit,
    onSaveAction: () -> Unit,
    onJournalEntryChanged: (paragraph: Paragraph, textField: TextFieldValue) -> Unit,
    onParagraphFocusChanged: (paragraph: Paragraph, cursorSelection: TextRange) -> Unit,
    onNewRowAdded: (index: Int) -> Unit,
    onJournalToolAction: (JournalParagraphToolsState) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .mandatorySystemGesturesPadding()
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
                        onJournalEntryTextChanged = onJournalEntryChanged,
                        onParagraphFocusChanged = onParagraphFocusChanged,
                        onNewRowAdded = onNewRowAdded,
                    )
                }
                TextEditorTools(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .imePadding(),
                    journalToolsState = state.paragraphTools.value,
                    onToolAction = onJournalToolAction
                )
            }
        }
    )
}

@Composable
fun JournalPage(
    modifier: Modifier = Modifier,
    paragraphs: List<Paragraph>,
    onJournalEntryTextChanged: (paragraph: Paragraph, textField: TextFieldValue) -> Unit,
    onParagraphFocusChanged: (paragraph: Paragraph, cursorSelection: TextRange) -> Unit,
    onNewRowAdded: (index: Int) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
    ) {
        itemsIndexed(paragraphs, key = { _, item -> item.index }) { index, paragraph ->
            PageRowInput(
                modifier = Modifier.fillMaxWidth(),
                isTitle = paragraph.isTitle,
                isFocused = paragraph.isFocused,
                textField = paragraph.textFieldValue,
                onParagraphTextChanged = {
                    onJournalEntryTextChanged(paragraph, it)
                },
                onParagraphFocusChanged = { isFocused ->
                    onParagraphFocusChanged(
                        paragraph.copy(isFocused = isFocused),
                        TextRange(paragraph.data.length)
                    )
                },
                onAddNewRow = {
                    onNewRowAdded(index)
                }
            )
        }
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
                            data = "Title",
                            textFieldValue = TextFieldValue("Title")
                        ),
                        Paragraph(
                            data = "Body",
                            textFieldValue = TextFieldValue("Body")
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
            onJournalEntryChanged = { _, _ -> },
            onNewRowAdded = { /*TODO*/ },
            onJournalToolAction = { _ -> /*TODO*/ },
            onParagraphFocusChanged = { _, _ -> /*TODO*/ },
        )
    }
}
