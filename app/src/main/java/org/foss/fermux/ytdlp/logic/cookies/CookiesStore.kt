package org.foss.fermux.ytdlp.logic.cookies

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File


// TODO> This is work for later for a more consistent ytdlp integration.
@Serializable
data class CookieProfile (
     val id: String = java.util.UUID.randomUUID().toString(),
     val url: String,
     val content: String
)

@Serializable
data class CookiesStore (
     val profiles: List<CookieProfile> = emptyList()
)

class CookieRepo(private val context: Context) {
     private val file = File(context.filesDir, "cookies_repo.json")
     private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }


     private fun load(): CookiesStore =
          if (file.exists()) json.decodeFromString(file.readText()) else CookiesStore()

     private fun save(store: CookiesStore) {
          file.writeText(json.encodeToString(store))
     }

     fun getAll(): List<CookieProfile> = load().profiles

     fun getId(id: String): CookieProfile? = load().profiles.find { it.id == id }

     fun getUrl(url: String): CookieProfile? = load().profiles.find { it.url == url }

     fun combine(profile: CookieProfile) {
          val store = load()
          save(store.copy(profiles = store.profiles + profile))
     }

     fun update(profile: CookieProfile) {
          val store = load()
          save(store.copy(profiles = store.profiles.map { if (it.url == profile.url) profile else it } ))
     }

     fun delete(profile: CookieProfile) {
          val store = load()
          save(store.copy(profiles = store.profiles.filterNot { it.id == profile.id }))
     }
}