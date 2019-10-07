package com.example.achar

import android.app.Application
import android.content.Context
import com.example.achar.di.appModule
import com.example.achar.di.networkModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class CoreApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        bind<Context>("ApplicationContext") with singleton { this@CoreApplication.applicationContext }
        bind<CoreApplication>() with singleton { this@CoreApplication }
        import(appModule)
        import(networkModule)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // set default font over pages
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/IRANSansMobile(FaNum).ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }

}