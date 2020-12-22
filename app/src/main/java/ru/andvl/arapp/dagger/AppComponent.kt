package ru.andvl.arapp.dagger

import dagger.Component
import ru.andvl.arapp.ui.main.MainActivity
import ru.andvl.arapp.dagger.module.LocalNavigationModule
import ru.andvl.arapp.dagger.module.NavigationModule
import ru.andvl.arapp.ui.camera.CameraFragment
import ru.andvl.arapp.ui.models.ArModelsFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NavigationModule::class,
    LocalNavigationModule::class
])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: ArModelsFragment)
    fun inject(fragment: CameraFragment)
}