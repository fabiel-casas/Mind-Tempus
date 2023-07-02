package virtus.synergy.journal.usecases

import virtus.synergy.core.toDayMonthYearTime
import virtus.synergy.core.toDayOfTheWeek
import virtus.synergy.core.toHourMinutes
import virtus.synergy.core.toMonthYear
import virtus.synergy.journal.model.db.MindTempusDataBase
import virtus.synergy.journal.model.tables.JournalEntryTable
import virtus.synergy.journal.screens.journal.details.JournalInfo
import virtus.synergy.journal.screens.journal.list.JournalItemState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 *
 * Created on 01/05/2023.
 */
class JournalUseCaseImpl(
    dataBase: MindTempusDataBase
) : JournalUseCase {
    private val journalDao = dataBase.journalDao()
    override suspend fun createEmotionEntry(emotionLevel: Int, emoji: String): String {
        val newEmotionalStatus = journalEntryTable(emotionLevel = emotionLevel, emoji = emoji)
        journalDao.createJournalEntry(newEmotionalStatus)
        return newEmotionalStatus.id
    }

    override suspend fun updateJournalNote(journalId: String, note: String) {
        journalDao.getJournalBy(journalId = journalId)?.let { journalEntryTable ->
            journalDao.updateEmotion(
                journalEntryTable.copy(note = note)
            )
        } ?: throw Exception("Journal not found")
    }

    private fun journalEntryTable(
        emotionLevel: Int,
        emoji: String = "",
        entry: String = "",
    ) = JournalEntryTable(
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

    override fun getJournalBy(journalId: String): JournalInfo {
        return journalDao.getJournalBy(journalId = journalId)
            ?.toJournalInfo() ?: throw Exception("Journal not found")
    }

    private fun JournalEntryTable.toJournalInfo() = JournalInfo(
        title = creationTime.toDayMonthYearTime(),
        note = note,
        emoji = emoji,
        emotionalIndex = emotionalLevel
    )

    private fun JournalEntryTable.toState(): JournalItemState = JournalItemState(
        id = id,
        emotion = emoji,
        dayOfTheWeek = creationTime.toDayOfTheWeek(),
        monthYear = creationTime.toMonthYear(),
        time = creationTime.toHourMinutes(),
        description = note
    )
}