package virtus.synergy.journal.data.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import virtus.synergy.core.DataClass
import java.time.ZonedDateTime
import java.util.UUID

@Entity
data class JournalEntryTable(
    @SerializedName("id")
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @SerializedName("emotionalLevel") val emotionalLevel: Int = -1, // Lower than zero means not set
    @SerializedName("emotionalStatus") val emoji: String = "",
    @SerializedName("comment") val note: String,
    @SerializedName("creationTime") val creationTime: ZonedDateTime,
    @SerializedName("updateTime") val updateTime: ZonedDateTime? = null,
) : DataClass
