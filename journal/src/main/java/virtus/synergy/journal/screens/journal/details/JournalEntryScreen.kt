package virtus.synergy.journal.screens.journal.details

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import virtus.synergy.analytics.ui.elementTag
import virtus.synergy.analytics.ui.flowTag
import virtus.synergy.analytics.ui.screenTag
import virtus.synergy.analytics.ui.trackOnDisplay
import virtus.synergy.design_system.R
import virtus.synergy.design_system.components.MTIconButton
import virtus.synergy.design_system.components.NavigationTopAppBar
import virtus.synergy.design_system.theme.MindTempusTheme
import java.util.UUID

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
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .flowTag("journal")
            .screenTag("createJournal")
            .elementTag("createJournalEntry")
            .trackOnDisplay(),
        topBar = {
            NavigationTopAppBar(
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
        content = { paddingValues ->
            JournalPage(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                paragraphs = state.journalInfo.value.paragraph,
                onJournalEntryChange = onJournalEntryChange,
                onNewRowAdded = onNewRowAdded,
            )
        }
    )
}

@Serializable
data class Paragraph(
    val index: String = UUID.randomUUID().toString(),
    val type: ParagraphType,
    val data: String,
    val isFocused: Boolean = false,
)

@Serializable
enum class ParagraphType {
    TITLE,
    BODY,
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
            when (paragraph.type) {
                ParagraphType.TITLE -> {
                    EmotionalDescription(
                        modifier = Modifier.fillMaxWidth(),
                        paragraph = paragraph,
                        onTextChange = { newParagraph ->
                            onJournalEntryChange(index, newParagraph)
                        },
                        onAddNewRow = {
                            onNewRowAdded(index)
                        }
                    )
                }

                ParagraphType.BODY -> {
                    EmotionalDescription(
                        modifier = Modifier.fillMaxWidth(),
                        paragraph = paragraph,
                        onTextChange = { newParagraph ->
                            onJournalEntryChange(index, newParagraph)
                        },
                        onAddNewRow = {
                            onNewRowAdded(index)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmotionalDescription(
    modifier: Modifier,
    paragraph: Paragraph,
    onTextChange: (Paragraph) -> Unit,
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
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = paragraph.data,
            onValueChange = { text ->
                onTextChange(paragraph.copy(data = text))
            },
            placeholder = {},
            textStyle = MaterialTheme.typography.bodyLarge,
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
                            type = ParagraphType.TITLE,
                            data = "Title"
                        ),
                        Paragraph(
                            type = ParagraphType.BODY,
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
            onNewRowAdded = { /*TODO*/ }
        )
    }
}
