package ru.example.translator.wordinfo.presentation.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.example.core.di.CoreComponentProvider
import ru.example.translator.R
import ru.example.translator.databinding.WordInfoDialogFragmentBinding
import ru.example.translator.wordinfo.di.DaggerWordInfoComponent
import ru.example.translator.wordinfo.presentation.WordInfoViewModel
import ru.example.translator.wordinfo.presentation.WordInfoViewState
import javax.inject.Inject

class WordInfoDialog : BottomSheetDialogFragment() {

	@Inject
	lateinit var factory: ViewModelProvider.Factory
	private lateinit var dismissDialogListener: () -> Unit
	private lateinit var binding: WordInfoDialogFragmentBinding
	private lateinit var viewModel: WordInfoViewModel
	private var behavior: BottomSheetBehavior<View>? = null

	/**
	 * Needed for checking if there was rotation or user dismissed the dialog
	 * */
	private var rotationFlag = 0

	override fun onAttach(context: Context) {
		super.onAttach(context)
		DaggerWordInfoComponent.factory()
			.create((requireActivity().application as CoreComponentProvider).getCoreComponent())
			.inject(this)
		viewModel = ViewModelProvider(this, factory).get(WordInfoViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setStyle(STYLE_NORMAL, R.style.TransparentDialogTheme)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		return WordInfoDialogFragmentBinding.inflate(inflater, container, false)
			.apply { binding = this }
			.root
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		initDialog()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setListeners()
		subscribeViewState()
		loadData()
	}

	override fun onDismiss(dialog: DialogInterface) {
		rotationFlag++
		super.onDismiss(dialog)
	}


	override fun onPause() {
		rotationFlag++
		viewModel.onPause()
		super.onPause()
	}

	override fun onStop() {
		/**
		 * If rotationFlag == 2 dialog was dismissed by user
		 * */
		if (rotationFlag == 2) {
			dismissDialogListener.invoke()
		}
		rotationFlag = 0
		super.onStop()
	}

	fun setDialogListener(listener: () -> Unit) {
		dismissDialogListener = listener
	}

	private fun loadData() {
		arguments?.apply {
			viewModel.onViewCreated(
				getLong(KEY_TEXT_ID, 0),
				getLong(KEY_TRANSLATION_ID, 0)
			)
		}
	}

	private fun initDialog() {
		dialog?.findViewById<FrameLayout>(R.id.design_bottom_sheet)
			?.let { bottomSheet -> behavior = BottomSheetBehavior.from(bottomSheet) }
		behavior?.apply {
			peekHeight = 0
			skipCollapsed = true
			state = BottomSheetBehavior.STATE_EXPANDED
		}
	}

	private fun subscribeViewState() {
		viewModel.wordInfoState.observe(viewLifecycleOwner, {
			when (it) {
				is WordInfoViewState.DataState -> handleDataState(it)
				is WordInfoViewState.ErrorState -> handleErrorState()
			}
		})
	}

	private fun handleErrorState() {
		binding.errorLayout.root.visibility = View.VISIBLE
		binding.wordInfoLayout.root.visibility = View.GONE
	}

	private fun handleDataState(state: WordInfoViewState.DataState) {
		showImageLoading()
		loadImage(state)
		setTexts(state)
	}

	private fun setListeners() {
		binding.wordInfoLayout.ivPlayBtn.setOnClickListener { viewModel.onPlayBtnClicked() }
		binding.errorLayout.btnErrorRetry.setOnClickListener { loadData() }
	}

	private fun setTexts(state: WordInfoViewState.DataState) {
		with(binding.wordInfoLayout) {
			tvWordInfoTranscription.text = state.info.transcription
			tvWordInfoText.text = state.info.text
			tvWordInfoTranslation.text = state.info.translation
		}
	}

	private fun showImageLoading() {
		with(binding) {
			errorLayout.root.visibility = View.GONE
			wordInfoLayout.root.visibility = View.VISIBLE
			Glide.with(requireContext())
				.asGif()
				.load(R.drawable.loader_img)
				.into(wordInfoLayout.ivWordInfoLoaderImage)
		}
	}

	private fun loadImage(state: WordInfoViewState.DataState) {
		with(binding.wordInfoLayout) {
			ivWordInfoImage.clipToOutline = true
			Glide.with(requireContext())
				.load(state.info.imageUrl)
				.addListener(object : RequestListener<Drawable> {
					override fun onLoadFailed(
						e: GlideException?,
						model: Any?,
						target: Target<Drawable>?,
						isFirstResource: Boolean
					): Boolean {
						ivWordInfoImage.background = ContextCompat.getDrawable(
							requireContext(),
							R.drawable.no_image_available
						)
						ivWordInfoLoaderImage.visibility = View.GONE
						return false
					}

					override fun onResourceReady(
						resource: Drawable?,
						model: Any?,
						target: Target<Drawable>?,
						dataSource: DataSource?,
						isFirstResource: Boolean
					): Boolean {
						ivWordInfoLoaderImage.visibility = View.GONE
						return false
					}

				})
				.transition(DrawableTransitionOptions.withCrossFade())
				.into(ivWordInfoImage)
		}
	}

	companion object {
		private const val KEY_TEXT_ID = "text id"
		private const val KEY_TRANSLATION_ID = "translation id"
		fun getInstance(textId: Long, translationId: Long) = WordInfoDialog().apply {
			arguments = Bundle().apply {
				putLong(KEY_TEXT_ID, textId)
				putLong(KEY_TRANSLATION_ID, translationId)
			}
		}
	}
}