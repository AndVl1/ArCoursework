package ru.andvl.arapp.subnavigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Cicerone.Companion.create
import com.github.terrakok.cicerone.Router

class LocalCiceroneHolder {
    private val mContainers = HashMap<String, Cicerone<Router>>()

    fun getCicerone(containerTag: String): Cicerone<Router> =
        mContainers.getOrPut(containerTag){
            create()
        }
}