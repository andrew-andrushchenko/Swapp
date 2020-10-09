package ua.andrii.andrushchenko.swapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person(
    val name: String,
    val height: String,
    val mass: String,
    @SerializedName("hair_color")
    val hairColor: String,
    @SerializedName("skin_color")
    val skinColor: String,
    @SerializedName("eye_color")
    val eyeColor: String,
    @SerializedName("birth_year")
    val birthYear: String,
    val gender: String,
    val homeworld: String,
    val films: List<String>?,
    val species: List<String>?,
    val vehicles: List<String>?,
    val starships: List<String>?,
    val created: String,
    val edited: String,
    val url: String
) : Parcelable {

    fun getPersonImagePath(): String {
        val pathChunks = url.split("/")
        return "file:///android_asset/${pathChunks[pathChunks.size - 3]}/${pathChunks[pathChunks.size - 2]}.jpg"
    }
}