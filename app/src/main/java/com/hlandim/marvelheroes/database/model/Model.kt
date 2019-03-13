package com.hlandim.marvelheroes.database.model

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable
import com.hlandim.marvelheroes.database.Converter


@Entity(tableName = "thumbnail")
class Thumbnail(
    @PrimaryKey
    @ColumnInfo(name = "thumbnail_id")
    var id: Int,
    @ColumnInfo(name = "path")
    var path: String?,
    @ColumnInfo(name = "extension")
    var extension: String?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(path)
        dest?.writeString(extension)
    }

    override fun describeContents(): Int {
        return 0//To change body of created functions use File | Settings | File Templates.
    }

    fun getFullThumbnailUrl(): String {
        return "$path.$extension"
    }

    companion object CREATOR : Parcelable.Creator<Thumbnail> {
        override fun createFromParcel(parcel: Parcel): Thumbnail {
            return Thumbnail(parcel)
        }

        override fun newArray(size: Int): Array<Thumbnail?> {
            return arrayOfNulls(size)
        }
    }
}

@Entity(
    foreignKeys = [ForeignKey(
        entity = Hero::class,
        parentColumns = arrayOf("hero_id"),
        childColumns = arrayOf("participation_response_id")
    )]
)
class ParticipationResponse(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "participation_response_id")
    var id: Int = 0,
    @ColumnInfo(name = "available")
    var available: Int,
    @ColumnInfo(name = "returned")
    var returned: Int,
    @ColumnInfo(name = "hero_id")
    var heroId: Int,
    @TypeConverters(Converter::class)
    var items: List<Participation> = emptyList()
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        arrayListOf<Participation>().apply {
            parcel.readList(this, Participation::class.java.classLoader)
        }
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeInt(available)
        dest?.writeInt(returned)
        dest?.writeInt(heroId)
        dest?.writeList(items)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor() : this(0, 0, 0, 0, emptyList())

    companion object CREATOR : Parcelable.Creator<ParticipationResponse> {
        override fun createFromParcel(parcel: Parcel): ParticipationResponse {
            return ParticipationResponse(parcel)
        }

        override fun newArray(size: Int): Array<ParticipationResponse?> {
            return arrayOfNulls(size)
        }
    }
}

@Entity(
    indices = [Index("participation_response_id")],
    foreignKeys = [ForeignKey(
        entity = ParticipationResponse::class,
        parentColumns = arrayOf("participation_response_id"),
        childColumns = arrayOf("participation_id")
    )]
)
class Participation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "participation_id")
    var id: Int = 0,
    @ColumnInfo(name = "resourceURI")
    var resourceURI: String,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "description")
    var description: String?,
    @ColumnInfo(name = "participation_response_id")
    var participationResponseId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(resourceURI)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(participationResponseId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Participation> {
        override fun createFromParcel(parcel: Parcel): Participation {
            return Participation(parcel)
        }

        override fun newArray(size: Int): Array<Participation?> {
            return arrayOfNulls(size)
        }
    }
}