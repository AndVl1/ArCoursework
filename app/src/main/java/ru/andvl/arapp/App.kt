package ru.andvl.arapp

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.github.terrakok.cicerone.Cicerone
import ru.andvl.arapp.dagger.AppComponent
import ru.andvl.arapp.dagger.DaggerAppComponent

class App: Application(), ImageLoaderFactory {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().build()
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext)
            .componentRegistry{
                if (android.os.Build.VERSION.SDK_INT >= 28){
                    add(ImageDecoderDecoder())
                } else {
                    add(GifDecoder())
                }
            }
            .crossfade(true)
            .build()
    }

    companion object {
        lateinit var INSTANCE: App
    }
}
