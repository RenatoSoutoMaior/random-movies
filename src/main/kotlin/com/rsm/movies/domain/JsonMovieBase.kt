import com.google.gson.annotations.SerializedName

data class JsonMovieBase(

        @SerializedName("page") val page: Int,
        @SerializedName("total_pages") val total_pages: Int,
        @SerializedName("results") val results: List<Movie>,
        @SerializedName("total_results") val total_results: Int
)