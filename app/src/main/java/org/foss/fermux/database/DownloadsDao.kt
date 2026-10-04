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

     @Query("SELECT * FROM downloads WHERE extractor = :extractor AND videoId = :videoId")
     suspend fun getSimilarInstance(extractor: String, videoId: String): DownloadsDatabaseField?

     @Query("SELECT * FROM downloads ORDER BY title COLLATE NOCASE ASC")
     fun getDownloadsOrderedByTitleASC(): Flow<List<DownloadsDatabaseField>>

     @Query("SELECT * FROM downloads ORDER BY size ASC")
     fun getDownloadsOrderedBySizeASC(): Flow<List<DownloadsDatabaseField>>

     @Query("SELECT * FROM downloads ORDER BY extractor ASC")
     fun getDownloadsOrderedByExtractorASC(): Flow<List<DownloadsDatabaseField>>

     @Query("SELECT * FROM downloads ORDER BY title DESC")
     fun getDownloadsOrderByTitleDESC(): Flow<List<DownloadsDatabaseField>>

     @Query("SELECT * FROM downloads ORDER BY size DESC")
     fun getDownloadsOrderBySizeDESC(): Flow<List<DownloadsDatabaseField>>

     @Query("SELECT * FROM downloads ORDER BY extractor DESC")
     fun getDownloadsOrderByExtractorDESC(): Flow<List<DownloadsDatabaseField>>

     // @Query("SELECT * FROM downloads ORDER BY format ") TODO. Implement this
}