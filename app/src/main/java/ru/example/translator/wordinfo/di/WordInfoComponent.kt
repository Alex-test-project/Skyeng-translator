package ru.example.translator.wordinfo.di

import dagger.Binds
import dagger.Component
import dagger.Module
import ru.example.core.di.BaseModule
import ru.example.core.di.CoreComponent
import ru.example.core.di.scopes.FragmentScope
import ru.example.translator.wordinfo.data.WordInfoRepositoryImpl
import ru.example.translator.wordinfo.domain.WordInfoRepository
import ru.example.translator.wordinfo.domain.interactor.WordInfoInteractor
import ru.example.translator.wordinfo.domain.interactor.WordInfoInteractorImpl
import ru.example.translator.wordinfo.presentation.WordInfoViewModel
import ru.example.translator.wordinfo.presentation.dialog.WordInfoDialog

@FragmentScope
@Component(
	modules = [
		WordInfoModule::class,
		WordInfoModuleBind::class
	],
	dependencies = [CoreComponent::class]
)
interface WordInfoComponent {
	fun inject(listFragment: WordInfoDialog)

	@Component.Factory
	interface Factory {
		fun create(coreComponent: CoreComponent): WordInfoComponent
	}
}

@Module
class WordInfoModule : BaseModule<WordInfoViewModel>() {

}

@Module
abstract class WordInfoModuleBind {

	@Binds
	abstract fun provideWordInfoInteractor(interactorImpl: WordInfoInteractorImpl): WordInfoInteractor

	@Binds
	abstract fun provideWordInfoRepository(wordInfoRepository: WordInfoRepositoryImpl): WordInfoRepository
}