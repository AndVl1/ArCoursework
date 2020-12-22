package ru.andvl.arapp.models

import android.util.Log
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 *
 */
object ModelsListContent {

    /**
     * An array of AR models.
     */
    val ITEMS: MutableList<ListItem> = ArrayList()

    /**
     * A map of sample models, by their names.
     */
    private val ITEM_MAP: MutableMap<String, ListItem> = HashMap()

    suspend fun init() {
        val job = Job()
        val ioCoroutineScope = CoroutineScope(Dispatchers.IO + job)
        ioCoroutineScope.launch {
            getModels()
        }.join()
    }

    private suspend fun getModels() {
        val db = Firebase.firestore
        val data = db.collection("models")
            .get()
            .await()
        for (obj in data) {
            addItem(serialiseQueryObject(obj))
        }
    }

    private fun serialiseQueryObject(obj: QueryDocumentSnapshot): ListItem {
        Log.d("MODELLISTCONTENT", "AAA")
        return ListItem(
            name = obj["name"] as String,
            modelLink = obj["modelLink"] as String,
            gifLink = obj["gifLink"] as String,
        )
    }

    private fun addItem(item: ListItem) {
        ITEMS.add(item)
        ITEM_MAP[item.name] = item
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class ListItem(val name: String, val gifLink: String, val modelLink: String) {
        override fun toString(): String = "$name: {\ngif: $gifLink,\nmodel: $modelLink }"
    }
}