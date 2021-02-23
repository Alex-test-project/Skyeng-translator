package ru.example.translator

import android.app.Application
import ru.example.core.di.CoreComponent
import ru.example.core.di.CoreComponentProvider
import ru.example.core.di.DaggerCoreComponent
import timber.log.Timber

class TranslatorApp : Application(), CoreComponentProvider {

	private val appComponent: CoreComponent by lazy(LazyThreadSafetyMode.NONE) {
		DaggerCoreComponent.factory().create(applicationContext)
	}

	override fun onCreate() {
		super.onCreate()
		Timber.plant(Timber.DebugTree())
	}

	override fun getCoreComponent(): CoreComponent {
		return appComponent
	}
}