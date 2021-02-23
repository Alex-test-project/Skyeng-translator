package ru.example.translator.search.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.example.core.TestCoroutineRule
import ru.example.translator.search.domain.interactor.SearchInteractor
import ru.example.translator.search.domain.model.Text
import ru.example.translator.search.domain.model.Translation
import ru.example.translator.search.presentation.mapper.MapperViewModel
import ru.example.translator.search.presentation.model.SearchItem

private const val TEST_QUERY = "some text"
private const val TEST_ID = 4L

class SearchViewModelTest {

	@Rule
	@JvmField
	var instantTaskExecutorRule = InstantTaskExecutorRule()

	@ExperimentalCoroutinesApi
	@Rule
	@JvmField
	var testCoroutineRule = TestCoroutineRule()

	private val viewStateObserver: Observer<SearchViewState> = mock()
	private val searchInteractor: SearchInteractor = mock()
	private val mapperViewModel: MapperViewModel = mock()
	private lateinit var viewModel: SearchViewModel

	@Before
	fun setUpViewModel() {
		viewModel = SearchViewModel(searchInteractor, mapperViewModel)
		viewModel.searchViewState.observeForever(viewStateObserver)
	}

	@ExperimentalCoroutinesApi
	@Test
	fun testViewStateLoading() = testCoroutineRule.runBlockingTest {
		//when
		viewModel.onQueryTextChanged(TEST_QUERY)
		//then
		verify(viewStateObserver).onChanged(SearchViewState.LoadingState)
	}

	@ExperimentalCoroutinesApi
	@Test
	fun testViewStateData() = testCoroutineRule.runBlockingTest {
		//given
		val viewItems = getViewItems()
		initViewModel(getMeanings(), viewItems)
		//when
		viewModel.onQueryTextChanged(TEST_QUERY)
		//then
		verify(viewStateObserver).onChanged(SearchViewState.LoadingState)
		verify(viewStateObserver).onChanged(SearchViewState.DataState(viewItems))
		assertEquals(viewModel.searchViewState.value, SearchViewState.DataState(viewItems))
	}

	@ExperimentalCoroutinesApi
	@Test
	fun testViewStateError() = testCoroutineRule.runBlockingTest {
		//given
		whenever(searchInteractor.fetchMeanings(TEST_QUERY)).thenThrow(RuntimeException())
		//when
		viewModel.onQueryTextChanged(TEST_QUERY)
		//then
		verify(viewStateObserver, times(1)).onChanged(SearchViewState.StartScreenState)
		verify(viewStateObserver, times(1)).onChanged(SearchViewState.LoadingState)
		verify(viewStateObserver, times(1)).onChanged(SearchViewState.ErrorState)
		assertEquals(viewModel.searchViewState.value, SearchViewState.ErrorState)
	}

	@ExperimentalCoroutinesApi
	@Test
	fun testViewStateEmpty() = testCoroutineRule.runBlockingTest {
		//given
		whenever(searchInteractor.fetchMeanings(TEST_QUERY)).thenReturn(emptyList())
		//when
		viewModel.onQueryTextChanged(TEST_QUERY)
		//then
		verify(viewStateObserver).onChanged(SearchViewState.LoadingState)
		verify(viewStateObserver).onChanged(SearchViewState.EmptyDataState)
	}

	@Test
	fun testNoInteractionWithEmptyQuery() {
		//when
		viewModel.onQueryTextChanged("")
		//then
		verify(viewStateObserver).onChanged(SearchViewState.StartScreenState)
		verifyZeroInteractions(searchInteractor)
		verifyZeroInteractions(mapperViewModel)
		verifyNoMoreInteractions(viewStateObserver)
	}

	@ExperimentalCoroutinesApi
	@Test
	fun testNoInteractionWithTheSameQuery() = testCoroutineRule.runBlockingTest {
		//given
		whenever(searchInteractor.lastQuery).thenReturn(TEST_QUERY)
		//when
		viewModel.onQueryTextChanged(TEST_QUERY)
		//then
		verify(searchInteractor, never()).fetchMeanings(TEST_QUERY)
		verify(viewStateObserver).onChanged(SearchViewState.StartScreenState)
		verifyZeroInteractions(mapperViewModel)
		verifyNoMoreInteractions(viewStateObserver)
	}

	@ExperimentalCoroutinesApi
	@Test
	fun testClickOnClosedCombineItem() = testCoroutineRule.runBlockingTest {
		//given
		val viewItems = getViewItems()
		initViewModel(getMeanings(), viewItems)
		//when
		viewModel.onQueryTextChanged(TEST_QUERY)
		viewModel.onClosedCombineClicked(TEST_ID)
		//then
		verify(mapperViewModel).addSearchItemTranslated(TEST_ID, viewItems)
		verify(mapperViewModel, never()).removeSearchItemTranslated(TEST_ID, viewItems)
		verify(mapperViewModel, never()).changeItemClosedToOpen(TEST_ID, viewItems)
		verify(mapperViewModel, never()).changeItemOpenToClosed(TEST_ID, viewItems)
	}

	@ExperimentalCoroutinesApi
	@Test
	fun testClickOnOpenedCombineItem() = testCoroutineRule.runBlockingTest {
		//given
		val viewItems = getViewItems()
		initViewModel(getMeanings(), viewItems)
		//when
		viewModel.onQueryTextChanged(TEST_QUERY)
		viewModel.onOpenCombineClicked(TEST_ID)
		//then
		verify(mapperViewModel).removeSearchItemTranslated(TEST_ID, viewItems)
		verify(mapperViewModel, never()).addSearchItemTranslated(TEST_ID, viewItems)
		verify(mapperViewModel, never()).changeItemClosedToOpen(TEST_ID, viewItems)
		verify(mapperViewModel, never()).changeItemOpenToClosed(TEST_ID, viewItems)
	}

	@ExperimentalCoroutinesApi
	@Test
	fun testOpenAnimFinished() = testCoroutineRule.runBlockingTest {
		//given
		val viewItems = getViewItems()
		initViewModel(getMeanings(), viewItems)
		//when
		viewModel.onQueryTextChanged(TEST_QUERY)
		viewModel.onOpenAnimFinished(TEST_ID)
		//then
		verify(mapperViewModel).changeItemOpenToClosed(TEST_ID, viewItems)
		verify(mapperViewModel, never()).changeItemClosedToOpen(TEST_ID, viewItems)
		verify(mapperViewModel, never()).removeSearchItemTranslated(TEST_ID, viewItems)
		verify(mapperViewModel, never()).addSearchItemTranslated(TEST_ID, viewItems)
	}

	@ExperimentalCoroutinesApi
	@Test
	fun testClosedAnimFinished() = testCoroutineRule.runBlockingTest {
		//given
		val viewItems = getViewItems()
		initViewModel(getMeanings(), viewItems)
		//when
		viewModel.onQueryTextChanged(TEST_QUERY)
		viewModel.onClosedAnimFinished(TEST_ID)
		//then
		verify(mapperViewModel).changeItemClosedToOpen(TEST_ID, viewItems)
		verify(mapperViewModel, never()).changeItemOpenToClosed(TEST_ID, viewItems)
		verify(mapperViewModel, never()).removeSearchItemTranslated(TEST_ID, viewItems)
		verify(mapperViewModel, never()).addSearchItemTranslated(TEST_ID, viewItems)
	}

	@ExperimentalCoroutinesApi
	@Test
	fun testChangingStateWhenOnClickSearchViewClosed() = testCoroutineRule.runBlockingTest {
		//given
		whenever(searchInteractor.fetchMeanings(TEST_QUERY)).thenReturn(emptyList())
		//when
		viewModel.onQueryTextChanged(TEST_QUERY)
		viewModel.onClosedClicked()
		//then
		verify(viewStateObserver).onChanged(SearchViewState.LoadingState)
		verify(viewStateObserver).onChanged(SearchViewState.EmptyDataState)
		verify(viewStateObserver, times(2)).onChanged(SearchViewState.StartScreenState)
		verifyNoMoreInteractions(viewStateObserver)
	}

	@ExperimentalCoroutinesApi
	@Test
	fun testChangingStateWhenDialogDismissed() = testCoroutineRule.runBlockingTest {
		//given
		val viewItems = getViewItems()
		initViewModel(getMeanings(), viewItems)
		//when
		viewModel.onQueryTextChanged(TEST_QUERY)
		viewModel.onItemClicked(TEST_ID, TEST_ID)
		//then
		verify(viewStateObserver, times(1)).onChanged(SearchViewState.StartScreenState)
		verify(viewStateObserver, times(1)).onChanged(SearchViewState.LoadingState)
		verify(viewStateObserver, times(1)).onChanged(SearchViewState.DataState(viewItems))
		verify(viewStateObserver, times(1)).onChanged(
			SearchViewState.DialogState(
				TEST_ID,
				TEST_ID,
				viewItems
			)
		)
		verifyNoMoreInteractions(viewStateObserver)
	}

	private suspend fun initViewModel(
		meanings: List<Text>,
		viewItems: List<SearchItem.SearchItemClosedCombine>
	) {
		whenever(searchInteractor.fetchMeanings(TEST_QUERY)).thenReturn(meanings)
		whenever(mapperViewModel.mapToViewModel(meanings)).thenReturn(viewItems)
	}

	private fun getMeanings() = listOf(
		Text(
			id = TEST_ID,
			text = "text",
			translations = listOf(
				Translation(
					TEST_ID,
					partOfSpeechCode = "partOfSpeechCode",
					previewUrl = "previewUrl",
					imageUrl = "imageUrl",
					transcription = "transcription",
					soundUrl = "soundUrl",
					translation = "translation",
					note = "note"
				)
			)
		)
	)

	private fun getViewItems() = listOf(
		SearchItem.SearchItemClosedCombine(
			TEST_ID,
			text = "text",
			translation = "translation",
			wordsNumber = 1,
			translationItems = listOf(getViewItemsTranslation())
		)
	)

	private fun getViewItemsTranslation() = SearchItem.SearchItemTranslation(
		TEST_ID,
		TEST_ID,
		translation = "translation",
		previewUrl = "previewUrl"
	)
}
