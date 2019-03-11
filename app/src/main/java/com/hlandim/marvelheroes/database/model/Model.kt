package com.hlandim.marvelheroes.database.model

import android.arch.persistence.room.*
import com.hlandim.marvelheroes.database.Converter
import java.io.Serializable


@Entity(tableName = "thumbnail")
class Thumbnail(
    @PrimaryKey
    @ColumnInfo(name = "thumbnail_id")
    var id: Int,
    @ColumnInfo(name = "path")
    val path: String,
    @ColumnInfo(name = "extension")
    val extension: String
) : Serializable {

    fun getFullThumbnailUrl(): String {
        return "$path.$extension"
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
) : Serializable {
    constructor() : this(0, 0, 0, 0, emptyList())
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
) : Serializable