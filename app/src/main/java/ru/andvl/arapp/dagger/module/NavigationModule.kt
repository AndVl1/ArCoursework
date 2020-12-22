package ru.andvl.arapp.dagger.module

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NavigationModule {
    private val mCicerone: Cicerone<Router> = Cicerone.create()

    @Provides
    @Singleton
    fun provideRouter(): Router =
        mCicerone.router

    @Provides
    @Singleton
    fun providesNavigationHolder(): NavigatorHolder =
        mCicerone.getNavigatorHolder()
}
