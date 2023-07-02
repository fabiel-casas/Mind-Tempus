package virtus.synergy.journal.screens.journal.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import virtus.synergy.analytics.ui.elementTag
import virtus.synergy.analytics.ui.flowTag
import virtus.synergy.analytics.ui.screenTag
import virtus.synergy.analytics.ui.trackOnDisplay
import virtus.synergy.design_system.R
import virtus.synergy.design_system.components.MTIconButton
import virtus.synergy.design_system.components.NavigationTopAppBar
import dev.jeziellago.compose.markdowntext.MarkdownText
import org.koin.androidx.compose.getViewModel

/**
 *
 * Created on 01/05/2023.
 */
@Composable
fun JournalEntryScreen(
    viewModel: JournalDetailsViewModel = getViewModel(),
    onBackAction: () -> Unit,
    journalId: String,
) {
    LaunchedEffect(key1 = journalId, block = {
        viewModel.loadJournal(journalId = journalId)
    })
    val state = viewModel.state
    JournalContent(
        state = state,
        onBackAction = onBackAction,
        onSaveAction = {
            viewModel.updateJournalNotes()
            onBackAction()
        },
        onJournalEntryChange = { newDescription ->
            viewModel.updateEmotionalDescription(newDescription)
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
private fun JournalContent(
    state: JournalDetailsState,
    onBackAction: () -> Unit,
    onSaveAction: () -> Unit,
    onJournalEntryChange: (String) -> Unit,
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
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = state.journalInfo.value.title,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    MarkdownText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp),
                        markdown = state.journalInfo.value.note,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
                EmotionalDescription(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 60.dp, 120.dp),
                    text = state.journalInfo.value.note,
                    onTextChange = onJournalEntryChange
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionalDescription(
    modifier: Modifier,
    text: String,
    onTextChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = {
            onTextChange(it)
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.journal_details_title_talk_about_it),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
            )
        },
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = TextFieldDefaults.outlinedTextFieldColors(),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.None
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun JournalContentPreview() {
    virtus.synergy.design_system.theme.MindTempusTheme {
        JournalContent(
            state = JournalDetailsState(),
            onBackAction = { /*TODO*/ },
            onSaveAction = {},
            onJournalEntryChange = {},
        )
    }
}
