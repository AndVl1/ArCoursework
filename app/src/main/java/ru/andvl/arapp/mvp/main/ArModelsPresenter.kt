package ru.andvl.arapp.mvp.main

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.andvl.arapp.Screens
import ru.andvl.arapp.models.ModelsListContent

@InjectViewState
class ArModelsPresenter(
    private val router: Router,
    private val screenNumber: Int
): MvpPresenter<ArModelsView>() {
    private val mJob = Job()
    private val mUiScope = CoroutineScope(Dispatchers.Main + mJob)

    fun getModels() {
        mUiScope.launch {
            ModelsListContent.init()
            viewState.setupList(ModelsListContent.ITEMS)
        }
    }
}