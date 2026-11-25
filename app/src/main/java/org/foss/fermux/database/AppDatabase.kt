package org.foss.fermux.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [DownloadsDatabaseField::class],
    version = 7,
    exportSchema = true,
    autoMigrations = []
)
abstract class AppDatabase : RoomDatabase() { abstract val downloadsDao: DownloadsDao }

// TODO, add delete all

object DownloaderDb {
    @Volatile
    private var instance: AppDatabase? = null
    fun getDatabase(context: Context): AppDatabase = instance ?: synchronized(this) {
        instance ?: Room.databaseBuilder(
            context.applicationContext,
            klass = AppDatabase::class.java,
            name = "downloader.db"
        ).build().also { instance = it }
    }
}