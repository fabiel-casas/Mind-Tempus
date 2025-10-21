package virtus.synergy.journal.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import virtus.synergy.journal.data.tables.JournalEntryTable

@Dao
interface JournalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createJournalEntry(journalEntryTable: JournalEntryTable)

    @Query("SELECT * FROM JournalEntryTable ORDER BY creationTime DESC")
    fun getJournalListFlow(): Flow<List<JournalEntryTable>>

    @Query("SELECT * FROM JournalEntryTable ORDER BY creationTime DESC LIMIT 1")
    suspend fun getLastJournalEntry(): JournalEntryTable?

    @Query("SELECT * FROM JournalEntryTable WHERE id = :journalId")
    fun getJournalBy(journalId: String): JournalEntryTable?

    @Delete
    fun deleteEmotion(journalEntryTable: JournalEntryTable)

    @Update
    fun updateEmotion(journalEntryTable: JournalEntryTable)

}