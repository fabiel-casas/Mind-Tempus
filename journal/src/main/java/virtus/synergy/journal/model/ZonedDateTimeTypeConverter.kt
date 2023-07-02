package virtus.synergy.journal.model

import androidx.room.TypeConverter
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeTypeConverter(
    writeZoneOffset: ZoneId = ZoneOffset.UTC,
    readZoneOffset: ZoneId = ZoneOffset.systemDefault(),
) {

    private val deserializer = ZonedDateTimeDeserializer(
        writeZoneOffset = writeZoneOffset,
        readZoneOffset = readZoneOffset
    )

    @TypeConverter
    fun write(value: ZonedDateTime?): String? {
        return deserializer.write(value)
    }

    @TypeConverter
    fun read(localDate: String?): ZonedDateTime? {
        return deserializer.read(localDate)
    }
}

class ZonedDateTimeDeserializer(
    writeZoneOffset: ZoneId = ZoneOffset.UTC,
    private val readZoneOffset: ZoneId = ZoneOffset.systemDefault(),
) {

    private val writeDateTimeFormatter: DateTimeFormatter = DateTimeFormatter
        .ISO_OFFSET_DATE_TIME
        .withZone(writeZoneOffset)

    fun write(value: ZonedDateTime?): String? = value?.format(writeDateTimeFormatter)

    fun read(dateString: String?): ZonedDateTime? = dateString?.let {
        ZonedDateTime.parse(
            it
        ).withZoneSameInstant(readZoneOffset)
    }
}