package ru.andvl.arapp

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.andvl.arapp.ui.camera.CameraFragment
import ru.andvl.arapp.ui.models.ArModelsFragment

object Screens {
    fun main() = FragmentScreen{
        ArModelsFragment.newInstance(1)
    }
    fun camera(link: String) = FragmentScreen("model($link)") {
        CameraFragment.newInstance(link)
    }
}