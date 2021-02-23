package ru.example.translator.search.di

import dagger.Binds
import dagger.Component
import dagger.Module
import ru.example.core.di.BaseModule
import ru.example.core.di.CoreComponent
import ru.example.core.di.scopes.FragmentScope
import ru.example.translator.search.data.SearchRepositoryImpl
import ru.example.translator.search.domain.SearchRepository
import ru.example.translator.search.domain.interactor.SearchInteractor
import ru.example.translator.search.domain.interactor.SearchInteractorImpl
import ru.example.translator.search.presentation.mapper.MapperViewModel
import ru.example.translator.search.presentation.mapper.MapperViewModelImpl
import ru.example.translator.search.presentation.SearchFragment
import ru.example.translator.search.presentation.SearchViewModel

@FragmentScope
@Component(
    modules = [
        SearchModuleBind::class,
        SearchModule::class
    ],
    dependencies = [CoreComponent::class]
)
interface SearchFragmentComponent {
    fun inject(listFragment: SearchFragment)

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): SearchFragmentComponent
    }
}

@Module
class SearchModule : BaseModule<SearchViewModel>()

@Module
abstract class SearchModuleBind {

    @Binds
    abstract fun provideViewMapper(mapperViewModel: MapperViewModelImpl): MapperViewModel

    @Binds
    abstract fun provideSearchInteractor(interactorImpl: SearchInteractorImpl): SearchInteractor

    @Binds
    abstract fun provideSearchRepository(searchRepository: SearchRepositoryImpl): SearchRepository

}