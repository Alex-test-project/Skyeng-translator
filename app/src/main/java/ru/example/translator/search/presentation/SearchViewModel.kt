package ru.example.translator.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.example.translator.search.domain.interactor.SearchInteractor
import ru.example.translator.search.presentation.mapper.MapperViewModel
import timber.log.Timber
import javax.inject.Inject

class SearchViewModel @Inject constructor(
	private val searchInteractor: SearchInteractor,
	private val mapper: MapperViewModel
) : ViewModel() {

	private var job: Job? = null

	private val _searchViewState = MutableLiveData<SearchViewState>()
	val searchViewState: LiveData<SearchViewState> = _searchViewState

	init {
		_searchViewState.value = SearchViewState.StartScreenState
	}

	fun onItemClicked(textId: Long, translationId: Long) {
		if(_searchViewState.value is SearchViewState.DataState){
			_searchViewState.value =
				SearchViewState.DialogState(
					textId,
					translationId,
					(_searchViewState.value as SearchViewState.DataState).items
				)
		}
	}

	fun onQueryTextChanged(newText: String, isNeedToRetry: Boolean = false) {
		if (isNeedToRetry || newText.isNotEmpty() && newText != searchInteractor.lastQuery) {
			when {
				job == null || job?.isCompleted == true -> startCoroutine(newText)
				else -> {
					cancelJob()
					startCoroutine(newText)
				}
			}
		}
	}

	fun onDialogDismiss() {
		if (_searchViewState.value is SearchViewState.DialogState) {
			_searchViewState.value =
				SearchViewState.DataState((_searchViewState.value as SearchViewState.DialogState).items)
		}
	}

	fun onClosedCombineClicked(id: Long) {
		if(_searchViewState.value is SearchViewState.DataState){
			mapper.addSearchItemTranslated(id, (_searchViewState.value as SearchViewState.DataState).items)
				.also { newSearchItems -> _searchViewState.value = SearchViewState.DataState(newSearchItems) }
		}
	}

	fun onClosedAnimFinished(id: Long) {
		if(searchViewState.value is SearchViewState.DataState){
			mapper.changeItemClosedToOpen(id, (searchViewState.value as SearchViewState.DataState).items)
				.also { newSearchItems -> _searchViewState.value = SearchViewState.DataState(newSearchItems) }
		}
	}

	fun onOpenCombineClicked(id: Long) {
		if(searchViewState.value is SearchViewState.DataState){
			mapper.removeSearchItemTranslated(id, (searchViewState.value as SearchViewState.DataState).items)
				.also { newSearchItems -> _searchViewState.value = SearchViewState.DataState(newSearchItems) }
		}
	}

	fun onOpenAnimFinished(id: Long) {
		if(searchViewState.value is SearchViewState.DataState){
			mapper.changeItemOpenToClosed(id, (searchViewState.value as SearchViewState.DataState).items)
				.also { newSearchItems -> _searchViewState.value = SearchViewState.DataState(newSearchItems) }
		}
	}

	fun onClosedClicked() {
		if (_searchViewState.value is SearchViewState.EmptyDataState) {
			_searchViewState.value = SearchViewState.StartScreenState
		}
	}

	private fun startCoroutine(query: String) {
		job = viewModelScope.launch {
			try {
				_searchViewState.value = SearchViewState.LoadingState
				_searchViewState.value = searchInteractor.fetchMeanings(query)
					.let(mapper::mapToViewModel)
					.let { searchItems ->
						if (searchItems.isEmpty()) SearchViewState.EmptyDataState
						else SearchViewState.DataState(searchItems)
					}
			} catch (ex: Exception) {
				if (ex !is CancellationException) {
					_searchViewState.value = SearchViewState.ErrorState
					Timber.e(ex)
				}
			}
		}
	}

	private fun cancelJob() {
		job?.cancel()
	}

}