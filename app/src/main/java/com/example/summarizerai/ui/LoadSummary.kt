import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

// Function to load and parse summary data from assets
suspend fun loadSummaryFromAssets(context: Context): SummaryData {
    return withContext(Dispatchers.IO) {
        val jsonString = context.assets.open("Summary.txt").bufferedReader().use { it.readText() }
        Json.decodeFromString<SummaryData>(jsonString)
    }
}