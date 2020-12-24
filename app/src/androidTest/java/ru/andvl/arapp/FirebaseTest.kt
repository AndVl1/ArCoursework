package ru.andvl.arapp

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import ru.andvl.arapp.models.ModelsListContent

@RunWith(AndroidJUnit4::class)
class FirebaseTest {
    @Test
    fun getModelsListTest() = runBlocking {
        ModelsListContent.init()
        ModelsListContent.ITEMS.forEach {
            Log.d("list item", it.toString())
        }
        delay(5000)
    }
}