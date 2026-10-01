package org.foss.fermux.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
     entities = [DownloadsDatabaseField::class],
     version = 1
)
abstract class DownloadsDatabase: RoomDatabase() {

     abstract val dao: DownloadsDao
}