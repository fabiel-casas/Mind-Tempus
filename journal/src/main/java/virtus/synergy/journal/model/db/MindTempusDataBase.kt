package virtus.synergy.journal.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import virtus.synergy.journal.model.ZonedDateTimeTypeConverter
import virtus.synergy.journal.model.tables.JournalEntryTable


@Database(
    entities = [
        JournalEntryTable::class
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(ZonedDateTimeTypeConverter::class)
abstract class MindTempusDataBase : RoomDatabase() {
    abstract fun journalDao(): JournalDao
}