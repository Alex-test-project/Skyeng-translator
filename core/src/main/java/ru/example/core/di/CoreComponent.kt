package ru.example.core.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import ru.example.core.di.viewmodelprovider.ViewModelProviderFactory
import ru.example.core.network.RetrofitFactory
import ru.example.core.player.MediaPlayerHelper
import ru.example.core.player.MediaPlayerHelperImpl
import ru.example.database.AppDataBase
import ru.example.database.dao.TextDao
import ru.example.database.dao.TranslationDao
import ru.example.database.databaseworker.DataBaseWorker
import ru.example.database.databaseworker.DataBaseWorkerImpl

@Singleton
@Component(
	modules = [
		DataBaseModule::class,
		CoreModuleBind::class
	]
)
interface CoreComponent {

	@Component.Factory
	interface Factory {
		fun create(@BindsInstance context: Context): CoreComponent
	}

	fun getRetrofitFactory(): RetrofitFactory
	fun getDataBaseWorker(): DataBaseWorker
	fun getMediaPlayerHelper(): MediaPlayerHelper
}

@Module
abstract class BaseModule<T : ViewModel> {
	@Provides
	fun provideViewModelFactory(viewModel: T): ViewModelProvider.Factory {
		return ViewModelProviderFactory(viewModel)
	}
}

@Module
class DataBaseModule {
	@Singleton
	@Provides
	fun prideAppDataBase(context: Context): AppDataBase {
		return AppDataBase.getInstance(context)
	}

	@Singleton
	@Provides
	fun provideTextDao(db: AppDataBase): TextDao {
		return db.textDao()
	}

	@Singleton
	@Provides
	fun provideTranslationDao(db: AppDataBase): TranslationDao {
		return db.translationDao()
	}
}

@Module
abstract class CoreModuleBind {
	@Singleton
	@Binds
	abstract fun provideDataBaseWorker(dataBaseWorkerImpl: DataBaseWorkerImpl): DataBaseWorker

	@Singleton
	@Binds
	abstract fun provideMediaPlayerHelper(mediaPlayerHelper: MediaPlayerHelperImpl): MediaPlayerHelper
}
