package virtus.synergy.journal.usecases

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import virtus.synergy.core.logError
import virtus.synergy.core.toDayMonthYearTime
import virtus.synergy.core.toDayOfTheWeek
import virtus.synergy.core.toHourMinutes
import virtus.synergy.core.toMonthYear
import virtus.synergy.journal.model.db.MindTempusDataBase
import virtus.synergy.journal.model.tables.JournalEntryTable
import virtus.synergy.journal.screens.journal.details.JournalInfo
import virtus.synergy.journal.screens.journal.details.Paragraph
import virtus.synergy.journal.screens.journal.list.JournalItemState
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/**
 *
 * Created on 01/05/2023.
 */
class JournalUseCaseImpl(
    dataBase: MindTempusDataBase
) : JournalUseCase {
    private val journalDao = dataBase.journalDao()
    override suspend fun createJournalEntryWithEmotionLevel(emotionLevel: Int, emoji: String): String {
        val newEmotionalStatus = journalEntryTable(emotionLevel = emotionLevel, emoji = emoji)
        createJournalEntry(newEmotionalStatus)
        return newEmotionalStatus.id
    }

    private suspend fun createJournalEntry(newEmotionalStatus: JournalEntryTable) {
        journalDao.createJournalEntry(newEmotionalStatus)
    }

    override suspend fun updateJournalNote(journalId: String, note: String) {
        journalDao.getJournalBy(journalId = journalId)?.let { journalEntryTable ->
            journalDao.updateEmotion(
                journalEntryTable.copy(note = note)
            )
        } ?: throw Exception("Journal not found")
    }

    private fun journalEntryTable(
        id: String = UUID.randomUUID().toString(),
        emotionLevel: Int = -1,
        emoji: String = "",
        entry: String = "",
    ) = JournalEntryTable(
        id = id,
        emotionalLevel = emotionLevel,
        emoji = emoji,
        note = entry,
        creationTime = ZonedDateTime.now(ZoneId.systemDefault())
    )

    override fun journalFLow(): Flow<Map<String, List<JournalItemState>>> {
        return journalDao.getJournalListFlow()
            .map { list ->
                list.map { it.toState() }
                    .groupBy {
                        it.monthYear
                    }
            }
    }

    override suspend fun getOrCreateJournalEntryBy(journalId: String): JournalInfo {
        return journalDao.getJournalBy(journalId = journalId)
            ?.toJournalInfo() ?: journalEntryTable(id = journalId).let {
                createJournalEntry(it)
                it.toJournalInfo()
        }
    }

    private fun JournalEntryTable.toJournalInfo() = JournalInfo(
        title = creationTime.toDayMonthYearTime(),
        paragraph = toParagraphList(),
        emoji = emoji,
        emotionalIndex = emotionalLevel
    )

    private fun JournalEntryTable.toParagraphList(): List<Paragraph> = if (note.isEmpty()) {
        listOf(
            Paragraph(
                data = "",
                isTitle = true,
            )
        )
    } else {
        try {
            Gson().fromJson(note, object : TypeToken<List<Paragraph>>() {}.type)
        } catch (error: JsonSyntaxException) {
            error.logError("Error parsing journal note")
            listOf(
                Paragraph(
                    data = note
                )
            )
        }
    }

    private fun JournalEntryTable.toState(): JournalItemState = JournalItemState(
        id = id,
        emotion = emoji,
        dayOfTheWeek = creationTime.toDayOfTheWeek(),
        monthYear = creationTime.toMonthYear(),
        time = creationTime.toHourMinutes(),
        description = toParagraphList().firstOrNull { it.data.isNotEmpty() }?.data.orEmpty()
    )
}