import com.example.summarizerai.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("/summarize/")
    fun uploadPdfForSummary(
        @Part pdf_file: MultipartBody.Part,
        @Part("summary_type") summaryType: RequestBody
    ): Call<SummaryResponse>

    @Multipart
    @POST("/ask-question/")
    fun askQuestion(
        @Part pdf_file: MultipartBody.Part,
        @Part("question") question: RequestBody
    ): Call<QuestionResponse>
}