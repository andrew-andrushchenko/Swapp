package ua.andrii.andrushchenko.swapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "people_table")
@Parcelize
data class Person(
    val name: String,
    val height: String,
    val mass: String,
    @SerializedName("hair_color")
    @ColumnInfo(name = "hair_color")
    val hairColor: String,
    @SerializedName("skin_color")
    @ColumnInfo(name = "skin_color")
    val skinColor: String,
    @SerializedName("eye_color")
    @ColumnInfo(name = "eye_color")
    val eyeColor: String,
    @SerializedName("birth_year")
    @ColumnInfo(name = "birth_year")
    val birthYear: String,
    val gender: String,
    @SerializedName("homeworld")
    @ColumnInfo(name = "homeworld")
    val homeWorld: String,
    val url: String
) : Parcelable {

    private val urlChunks: List<String>
        get() {
            return url.split("/") ?: arrayListOf("")
        }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    val personImagePath: String
        get() = "file:///android_asset/${urlChunks[urlChunks.size - 3]}/${urlChunks[urlChunks.size - 2]}.jpg"

}