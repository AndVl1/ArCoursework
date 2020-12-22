package ru.andvl.arapp.mvp.main

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.andvl.arapp.models.ModelsListContent

@StateStrategyType(AddToEndSingleStrategy::class)
interface ArModelsView : MvpView {
    fun setupList(list: MutableList<ModelsListContent.ListItem>)
}