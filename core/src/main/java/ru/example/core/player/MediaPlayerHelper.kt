package ru.example.core.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import javax.inject.Inject

interface MediaPlayerHelper {
	fun playAudio(audioUrl: String)
	fun releasePlayer()
}

class MediaPlayerHelperImpl @Inject constructor(
	private val appContext: Context
) : MediaPlayerHelper,
	MediaPlayer.OnCompletionListener,
	MediaPlayer.OnErrorListener,
	MediaPlayer.OnPreparedListener
{

	private var player: MediaPlayer? = null

	override fun playAudio(audioUrl: String) {
		if (player == null) {
			player = MediaPlayer().apply {
				setAudioAttributes(
					AudioAttributes.Builder()
						.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
						.setUsage(AudioAttributes.USAGE_MEDIA)
						.build()
				)
				setOnCompletionListener(this@MediaPlayerHelperImpl)
				setOnPreparedListener(this@MediaPlayerHelperImpl)
				setOnErrorListener(this@MediaPlayerHelperImpl)
			}
		}
		player?.apply {
			if (!isPlaying) {
				setDataSource(audioUrl)
				prepare()
			}
		}
	}

	override fun releasePlayer() {
		player?.release()
		player = null
	}

	override fun onCompletion(mp: MediaPlayer?) {
		player?.reset()
	}

	override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
		when (what) {
			MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK -> {
				println("MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK $extra")
			}
			MediaPlayer.MEDIA_ERROR_SERVER_DIED -> {
				println("MEDIA ERROR SERVER DIED $extra")
			}
			MediaPlayer.MEDIA_ERROR_UNKNOWN -> {
				println("MEDIA ERROR UNKNOWN $extra")
			}
			else -> println("$what, $extra")
		}
		return false
	}

	override fun onPrepared(mp: MediaPlayer?) {
		player?.start()
	}

}