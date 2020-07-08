import com.google.gson.annotations.SerializedName

data class Movie(

        @SerializedName("overview") val overview: String,
        @SerializedName("original_language") val original_language: String,
        @SerializedName("original_title") val original_title: String,
        @SerializedName("video") val video: Boolean,
        @SerializedName("title") val title: String,
        @SerializedName("genre_ids") val genre_ids: List<Int>,
        @SerializedName("poster_path") val poster_path: String,
        @SerializedName("backdrop_path") val backdrop_path: String,
        @SerializedName("release_date") val release_date: String,
        @SerializedName("popularity") val popularity: Double,
        @SerializedName("vote_average") val vote_average: Double,
        @SerializedName("id") val id: Int,
        @SerializedName("adult") val adult: Boolean,
        @SerializedName("vote_count") val vote_count: Int
)