import kotlinx.serialization.Serializable

@Serializable
data class SummaryData(
    val Short: String,
    val medium: String,
    val long: String
)