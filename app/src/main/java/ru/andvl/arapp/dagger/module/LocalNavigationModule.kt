package ru.andvl.arapp.dagger.module

import dagger.Module
import dagger.Provides
import ru.andvl.arapp.subnavigation.LocalCiceroneHolder
import javax.inject.Singleton

@Module
object LocalNavigationModule {

    @Provides
    @Singleton
    fun provideLocalNavigationHolder(): LocalCiceroneHolder = LocalCiceroneHolder()
}