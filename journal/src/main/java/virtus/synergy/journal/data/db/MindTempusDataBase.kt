package virtus.synergy.journal.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import virtus.synergy.journal.data.ZonedDateTimeTypeConverter
import virtus.synergy.journal.data.tables.JournalEntryTable


@Database(
    entities = [
        JournalEntryTable::class
    ],
    version = 2,
    exportSchema = false,
)
@TypeConverters(ZonedDateTimeTypeConverter::class)
abstract class MindTempusDataBase : RoomDatabase() {
    abstract fun journalDao(): JournalDao
}