package ru.example.translator.wordinfo.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.example.core.player.MediaPlayerHelper
import ru.example.translator.wordinfo.domain.interactor.WordInfoInteractor
import timber.log.Timber
import javax.inject.Inject

class WordInfoViewModel @Inject constructor(
	private val interactor: WordInfoInteractor,
	private val playerHelper: MediaPlayerHelper
) : ViewModel() {

	private val _wordInfoState = MutableLiveData<WordInfoViewState>()
	val wordInfoState: LiveData<WordInfoViewState> = _wordInfoState

	fun onViewCreated(textId: Long, translationId: Long) {
		viewModelScope.launch {
			try {
				_wordInfoState.value = interactor.getWordInfo(textId, translationId)
						.let { wordInfo -> WordInfoViewState.DataState(wordInfo) }
			} catch (ex: Exception) {
				_wordInfoState.value = WordInfoViewState.ErrorState
				Timber.e(ex)
			}
		}
	}

	fun onPause()  = playerHelper.releasePlayer()

	fun onPlayBtnClicked() {
		if (_wordInfoState.value is WordInfoViewState.DataState) {
			playerHelper.playAudio((_wordInfoState.value as WordInfoViewState.DataState).info.soundUrl)
		}
	}
}