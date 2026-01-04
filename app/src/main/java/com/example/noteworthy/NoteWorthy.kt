package com.example.noteworthy

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.example.noteworthy.di.appModule

class NoteWorthyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NoteWorthyApp)
            modules(appModule)
        }
    }
}
