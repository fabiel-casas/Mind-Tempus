package virtus.synergy.journal.extensions

import android.content.Context
import virtus.synergy.core.logError
import java.io.IOException
import java.io.InputStream


class JsonFileUtils(
    private val context: Context,
    private val fileName: String = "time-zone.json"
) {

    fun getJsonFileAsString(): String? {
        return try {
            val inputStream: InputStream = context.assets.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer)
        } catch (ex: IOException) {
            ex.logError("Error with input stream")
            return null
        }
    }
}