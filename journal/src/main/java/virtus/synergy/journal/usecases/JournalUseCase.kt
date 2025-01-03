package virtus.synergy.journal.usecases

import virtus.synergy.journal.screens.journal.details.JournalInfo
import virtus.synergy.journal.screens.journal.list.JournalItemState
import kotlinx.coroutines.flow.Flow

/**
 *
 * Created on 01/05/2023.
 */
interface JournalUseCase {
    suspend fun createJournalEntryWithEmotionLevel(emotionLevel: Int, emoji: String): String
    suspend fun updateJournalNote(journalId: String, note: String)
    fun journalFLow(): Flow<Map<String, List<JournalItemState>>>
    suspend fun getOrCreateJournalEntryBy(journalId: String): JournalInfo
}