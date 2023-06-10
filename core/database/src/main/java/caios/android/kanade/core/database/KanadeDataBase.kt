package caios.android.kanade.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import caios.android.kanade.core.database.album_detail.AlbumDetailDao
import caios.android.kanade.core.database.album_detail.AlbumDetailEntity
import caios.android.kanade.core.database.album_detail.AlbumTagEntity
import caios.android.kanade.core.database.album_detail.AlbumTrackEntity
import caios.android.kanade.core.database.artist_detail.ArtistDetailDao
import caios.android.kanade.core.database.artist_detail.ArtistDetailEntity
import caios.android.kanade.core.database.artist_detail.ArtistTagEntity
import caios.android.kanade.core.database.artist_detail.SimilarArtistEntity
import caios.android.kanade.core.database.artwork.ArtworkDao
import caios.android.kanade.core.database.artwork.ArtworkEntity

@Database(
    entities = [
        ArtistDetailEntity::class,
        ArtistTagEntity::class,
        SimilarArtistEntity::class,
        AlbumDetailEntity::class,
        AlbumTrackEntity::class,
        AlbumTagEntity::class,
        ArtworkEntity::class,
    ],
    version = 1,
)
abstract class KanadeDataBase : RoomDatabase() {
    abstract fun artistDetailDao(): ArtistDetailDao
    abstract fun albumDetailDao(): AlbumDetailDao
    abstract fun artworkDao(): ArtworkDao
}