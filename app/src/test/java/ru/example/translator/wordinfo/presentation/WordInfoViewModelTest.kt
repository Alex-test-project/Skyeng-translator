package ru.example.translator.wordinfo.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.example.core.TestCoroutineRule
import ru.example.core.player.MediaPlayerHelper
import ru.example.translator.wordinfo.domain.interactor.WordInfoInteractor
import ru.example.translator.wordinfo.domain.model.WordInfo
import java.lang.RuntimeException

private const val TEXT_ID = 1L
private const val TRANSLATION_ID = 2L

class WordInfoViewModelTest {

	@Rule
	@JvmField
	var instantTaskExecutorRule = InstantTaskExecutorRule()

	@ExperimentalCoroutinesApi
	@Rule
	@JvmField
	var testCoroutineRule = TestCoroutineRule()

	private val viewStateObserver: Observer<WordInfoViewState> = mock()
	private val interactor: WordInfoInteractor = mock()
	private val playerHelper: MediaPlayerHelper = mock()
	private lateinit var viewModel: WordInfoViewModel

	@Before
	fun setUpViewModel() {
		viewModel = WordInfoViewModel(interactor, playerHelper)
		viewModel.wordInfoState.observeForever(viewStateObserver)
	}

	@ExperimentalCoroutinesApi
	@Test
	fun testDataState() = testCoroutineRule.runBlockingTest {
		//given
		whenever(interactor.getWordInfo(TEXT_ID, TRANSLATION_ID)).thenReturn(getWordInfo())
		//when
		viewModel.onViewCreated(TEXT_ID, TRANSLATION_ID)
		//then
		verify(viewStateObserver).onChanged(WordInfoViewState.DataState(getWordInfo()))
		verifyNoMoreInteractions(viewStateObserver)
	}

	@ExperimentalCoroutinesApi
	@Test
	fun testErrorState() = testCoroutineRule.runBlockingTest {
		//given
		whenever(interactor.getWordInfo(TEXT_ID, TRANSLATION_ID)).thenThrow(RuntimeException())
		//when
		viewModel.onViewCreated(TEXT_ID, TRANSLATION_ID)
		//then
		verify(viewStateObserver).onChanged(WordInfoViewState.ErrorState)
		verifyNoMoreInteractions(viewStateObserver)
	}

	@Test
	fun testReleasePlayer() {
		//when
		viewModel.onPause()
		//then
		verify(playerHelper).releasePlayer()
	}

	@ExperimentalCoroutinesApi
	@Test
	fun testOnPlayButtonClick() = testCoroutineRule.runBlockingTest {
		//given
		whenever(interactor.getWordInfo(TEXT_ID, TRANSLATION_ID)).thenReturn(getWordInfo())
		//when
		viewModel.onViewCreated(TEXT_ID, TRANSLATION_ID)
		viewModel.onPlayBtnClicked()
		//then
		verify(playerHelper).playAudio("soundUrl")
	}

	private fun getWordInfo() = WordInfo(
		text = "text",
		imageUrl = "imageUrl",
		soundUrl = "soundUrl",
		translation = "translation",
		transcription = "transcription"
	)

}