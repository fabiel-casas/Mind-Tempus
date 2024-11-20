package virtus.synergy.journal.screens.journal.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.MutableWindowInsets
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import virtus.synergy.analytics.ui.elementTag
import virtus.synergy.analytics.ui.flowTag
import virtus.synergy.analytics.ui.screenTag
import virtus.synergy.analytics.ui.trackOnDisplay
import virtus.synergy.analytics.ui.trackableClick
import virtus.synergy.design_system.R
import virtus.synergy.design_system.components.HeaderTopAppBar
import virtus.synergy.design_system.components.MTFloatingActionButton
import virtus.synergy.design_system.theme.MindTempusTheme
import virtus.synergy.design_system.theme.getCardElevation
import virtus.synergy.journal.Constants.emotionalListState

/**
 *
 * Created on 01/05/2023.
 */
@Composable
fun JournalListScreen(
    viewModel: JournalListViewModel = koinViewModel(),
    navigateToCreateEntry: () -> Unit,
    navigateToSetting: () -> Unit,
    navigateToJournal: (journalId: String) -> Unit,
) {
    JournalListContent(
        state = viewModel.state,
        navigateToCreateEntry = navigateToCreateEntry,
        navigateToSetting = navigateToSetting,
        navigateToJournal = navigateToJournal,
    )
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
@Composable
private fun JournalListContent(
    state: EmotionalListState,
    navigateToJournal: (journalId: String) -> Unit,
    navigateToCreateEntry: () -> Unit,
    navigateToSetting: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .flowTag("journal")
            .screenTag("journal_list")
            .elementTag("journalListScreen")
            .trackOnDisplay(),
        floatingActionButton = {
            FloatingButtons(
                navigateToCreateEntry = navigateToCreateEntry
            )
        },
        topBar = {
            HeaderTopAppBar(
                title = stringResource(id = R.string.mindTempus_journal_title),
                actions = {
                    IconButton(
                        onClick = navigateToSetting
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentWindowInsets = MutableWindowInsets()
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
        ) {
            LazyColumn(
                content = {
                    state.journalRecordList.value.keys.toList().forEach { monthName ->
                        stickyHeader {
                            SectionItem(monthName)
                        }
                        items(state.journalRecordList.value[monthName] ?: emptyList()) { item ->
                            JournalEntryItem(
                                journalItemState = item,
                                onJournalEntryClick = {
                                    navigateToJournal(it.id)
                                }
                            )
                        }
                    }

                }
            )
        }
    }
}

@Composable
private fun SectionItem(monthName: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            text = monthName,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}


@Composable
private fun JournalStatisticsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        elevation = getCardElevation(defaultElevation = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Card {
                Column(
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
                    Text(
                        text = "Days",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Text(
                        text = "20",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun JournalEntryItem(
    journalItemState: JournalItemState,
    onJournalEntryClick: (JournalItemState) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth()
            .elementTag("journalEntryCard")
            .trackableClick { onJournalEntryClick(journalItemState) },
        shape = MaterialTheme.shapes.medium,
        elevation = getCardElevation(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
        ) {
            Text(
                text = journalItemState.emotion,
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = journalItemState.dayOfTheWeek,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = journalItemState.time,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                Text(
                    text = journalItemState.description,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
    Spacer(modifier = Modifier.width(8.dp))
}

@Composable
private fun FloatingButtons(
    navigateToCreateEntry: () -> Unit
) {
    MTFloatingActionButton(
        modifier = Modifier.elementTag("addNewEntry"),
        onClick = {
            navigateToCreateEntry()
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.mind_tempus_add_new)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun JournalListContentPreview() {
    val state = remember {
        emotionalListState
    }
    MindTempusTheme {
        JournalListContent(
            state = state,
            navigateToCreateEntry = {},
            navigateToSetting = {},
            navigateToJournal = {},
        )
    }
}