package com.hlandim.marvelheroes.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.hlandim.marvelheroes.R
import com.hlandim.marvelheroes.view.details.ParticipationParent
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Entity(tableName = "hero")
class Hero(
    @PrimaryKey
    @ColumnInfo(name = "hero_id")
    var id: Int = 0,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "modified")
    var modified: String,
    @Embedded
    var thumbnail: Thumbnail,
    @ColumnInfo(name = "resourceURI")
    var resourceURI: String,
    @ColumnInfo(name = "favorite")
    var favorite: Boolean,
    @Embedded(prefix = "comics_")
    var comics: ParticipationResponse,
    @Embedded(prefix = "series_")
    var series: ParticipationResponse,
    @Embedded(prefix = "stories_")
    var stories: ParticipationResponse,
    @Embedded(prefix = "events_")
    var events: ParticipationResponse
) : Parcelable {

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(name)
        dest?.writeString(modified)
        dest?.writeParcelable(thumbnail, flags)
        dest?.writeString(resourceURI)
        if (favorite) {
            dest?.writeInt(1)
        } else {
            dest?.writeInt(0)
        }
        dest?.writeParcelable(comics, flags)
        dest?.writeParcelable(series, flags)
        dest?.writeParcelable(stories, flags)
        dest?.writeParcelable(events, flags)
    }

    override fun describeContents(): Int {
        return 0
    }


    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable<Thumbnail>(Thumbnail::class.java.classLoader),
        parcel.readString(),
        parcel.readInt() != 0,
        parcel.readParcelable<ParticipationResponse>(ParticipationResponse::class.java.classLoader),
        parcel.readParcelable<ParticipationResponse>(ParticipationResponse::class.java.classLoader),
        parcel.readParcelable<ParticipationResponse>(ParticipationResponse::class.java.classLoader),
        parcel.readParcelable<ParticipationResponse>(ParticipationResponse::class.java.classLoader)
    )


    constructor() :

            this(
                0,
                "",
                "",
                Thumbnail(0, "", ""),
                "",
                false,
                ParticipationResponse(0, 0, 0, 0, emptyList()),
                ParticipationResponse(0, 0, 0, 0, emptyList()),
                ParticipationResponse(0, 0, 0, 0, emptyList()),
                ParticipationResponse(0, 0, 0, 0, emptyList())
            )

    constructor(id: Int, name: String) : this(
        id,
        name,
        "",
        Thumbnail(0, "", ""),
        "",
        false,
        ParticipationResponse(0, 0, 0, 0, emptyList()),
        ParticipationResponse(0, 0, 0, 0, emptyList()),
        ParticipationResponse(0, 0, 0, 0, emptyList()),
        ParticipationResponse(0, 0, 0, 0, emptyList())
    )


    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Hero)
            return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id + 30
    }

    fun getAllParticipation(): List<ParticipationParent> {
        val participation = arrayListOf<ParticipationParent>()
        if (this.comics.items.isNotEmpty()) {
            val comicsParticipation = ParticipationParent("Comics", this.comics.items.take(MAX_PARTICIPATION))
            participation.add(comicsParticipation)
        }
        if (this.events.items.isNotEmpty()) {
            val eventsParticipation = ParticipationParent("Events", this.events.items.take(MAX_PARTICIPATION))
            participation.add(eventsParticipation)
        }
        if (this.stories.items.isNotEmpty()) {
            val storiesParticipation = ParticipationParent("Stories", this.stories.items.take(MAX_PARTICIPATION))
            participation.add(storiesParticipation)
        }
        if (this.series.items.isNotEmpty()) {
            val seriesParticipation = ParticipationParent("Series", this.series.items.take(MAX_PARTICIPATION))
            participation.add(seriesParticipation)
        }

        return participation
    }

    fun getFavoriteImage(): Int {
        return when {
            favorite -> R.drawable.ic_star_filled
            else -> R.drawable.ic_star
        }
    }

    fun getModifiedString(): String? {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault())
        val formatNew = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatNew.format(format.parse(modified))
    }

    companion object CREATOR : Parcelable.Creator<Hero> {
        const val MAX_PARTICIPATION = 3
        override fun createFromParcel(parcel: Parcel): Hero {
            return Hero(parcel)
        }

        override fun newArray(size: Int): Array<Hero?> {
            return arrayOfNulls(size)
        }
    }
}