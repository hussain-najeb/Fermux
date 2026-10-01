package org.foss.fermux.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface DownloadsDao {

     @Upsert
     suspend fun upsertDownload(downloads: DownloadsDatabaseField)

     @Delete
     suspend fun deleteDownload(downloads: DownloadsDatabaseField)

     @Query("SELECT * FROM downloads ORDER BY title COLLATE NOCASE ASC")
     fun getDownloadsOrderedByTitle(): Flow<List<DownloadsDatabaseField>>

     @Query("SELECT * FROM downloads ORDER BY duration ASC")
     fun getDownloadsOrderedByDuration(): Flow<List<DownloadsDatabaseField>>

     @Query("SELECT * FROM downloads ORDER BY size ASC")
     fun getDownloadsOrderedBySize(): Flow<List<DownloadsDatabaseField>>

     @Query("SELECT * FROM downloads ORDER BY extractor ASC")
     fun getDownloadsOrderedByExtractor(): Flow<List<DownloadsDatabaseField>>

     // @Query("SELECT * FROM downloads ORDER BY format ") TODO. Implement this
}