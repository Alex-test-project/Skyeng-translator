package ru.example.translator.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import ru.example.core.base.BaseFragment
import ru.example.core.clear
import ru.example.core.di.CoreComponentProvider
import ru.example.core.setEnable
import ru.example.translator.R
import ru.example.translator.databinding.SearchFragmentBinding
import ru.example.translator.search.di.DaggerSearchFragmentComponent
import ru.example.translator.search.presentation.adapter.SearchAdapter
import ru.example.translator.wordinfo.presentation.dialog.WordInfoDialog
import javax.inject.Inject

class SearchFragment :
	BaseFragment<SearchViewModel, SearchFragmentBinding>(R.layout.search_fragment) {

	@Inject
	lateinit var factory: ViewModelProvider.Factory
	lateinit var searchView: SearchView
	private val adapter by lazy(LazyThreadSafetyMode.NONE) {
		SearchAdapter(
			viewModel::onItemClicked,
			viewModel::onOpenCombineClicked,
			viewModel::onOpenAnimFinished,
			viewModel::onClosedCombineClicked,
			viewModel::onClosedAnimFinished
		)
	}

	override fun initDi() {
		DaggerSearchFragmentComponent.factory()
			.create((requireActivity().application as CoreComponentProvider).getCoreComponent())
			.inject(this)
		viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = SearchFragmentBinding.inflate(inflater, container, false)
		binding?.tbSearchListToolbar?.inflateMenu(R.menu.search_menu)
		return binding?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initSearchView()
		subscribeViewState()
		initListeners()
	}

	private fun initListeners() {
		initErrorViewListeners()
		initSearchViewListeners()
	}

	private fun initErrorViewListeners() {
		binding?.errorLayout?.btnErrorRetry?.setOnClickListener {
			viewModel.onQueryTextChanged(searchView.query.toString(), isNeedToRetry = true)
		}
	}

	private fun initSearchViewListeners() {
		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String?): Boolean {
				searchView.clear()
				return true
			}

			override fun onQueryTextChange(newText: String?): Boolean {
				viewModel.onQueryTextChanged(newText.orEmpty())
				return true
			}
		})
		searchView.setOnCloseListener {
			viewModel.onClosedClicked()
			return@setOnCloseListener false
		}
	}

	private fun subscribeViewState() {
		viewModel.searchViewState.observe(viewLifecycleOwner, { viewState ->
			when (viewState) {
				is SearchViewState.ErrorState -> handleErrorState()
				is SearchViewState.DataState -> handleDataState(viewState)
				is SearchViewState.LoadingState -> handleLoadingState()
				is SearchViewState.EmptyDataState -> handleEmptyDataState()
				is SearchViewState.StartScreenState -> handleStartScreenState()
				is SearchViewState.DialogState -> handleDialogState(viewState)
			}
		})
	}

	private fun handleDialogState(viewState: SearchViewState.DialogState) {
		binding?.loadingLayout?.hideLoading()
		binding?.tvInfoMessage?.visibility = View.GONE
		searchView.clearFocus()
		if (adapter.itemCount == 0) {
			binding?.rvSearchContainer?.adapter = adapter
			adapter.add(viewState.items)
		}
		fragmentManager?.also { manager ->
			val fragment = manager.findFragmentByTag(WordInfoDialog::class.simpleName)
			if (fragment == null) {
				showWordInfoFragment(viewState, manager)
			} else {
				setWordInfoFragmentListener(fragment)
			}
		}
	}

	private fun handleStartScreenState() {
		binding?.loadingLayout?.hideLoading()
		binding?.errorLayout?.root?.visibility = View.GONE
		binding?.tvInfoMessage?.visibility = View.VISIBLE
		binding?.tvInfoMessage?.text = resources.getString(R.string.start_screen_message)
	}

	private fun handleEmptyDataState() {
		binding?.loadingLayout?.hideLoading()
		binding?.errorLayout?.root?.visibility = View.GONE
		binding?.tvInfoMessage?.visibility = View.VISIBLE
		binding?.tvInfoMessage?.text = resources.getString(R.string.empty_data_state_message)
	}

	private fun handleDataState(viewState: SearchViewState.DataState) {
		searchView.setEnable(true)
		binding?.tvInfoMessage?.visibility = View.GONE
		binding?.errorLayout?.root?.visibility = View.GONE
		binding?.loadingLayout?.hideLoading()
		if (binding?.rvSearchContainer?.adapter == null) {
			binding?.rvSearchContainer?.adapter = adapter
			adapter.add(viewState.items)
		} else {
			adapter.replaceWithDiff(viewState.items)
		}
	}

	private fun handleLoadingState() {
		binding?.errorLayout?.root?.visibility = View.GONE
		binding?.tvInfoMessage?.visibility = View.GONE
		binding?.loadingLayout?.showLoading()
		adapter.clear()
	}

	private fun handleErrorState() {
		binding?.tvInfoMessage?.visibility = View.GONE
		binding?.loadingLayout?.hideLoading()
		binding?.errorLayout?.root?.visibility = View.VISIBLE
		searchView.setEnable(false)
		searchView.clearFocus()
	}

	private fun initSearchView() {
		searchView = binding?.tbSearchListToolbar
			?.menu?.findItem(R.id.actionSearch)?.actionView as SearchView
		searchView.maxWidth = Integer.MAX_VALUE
		searchView.queryHint = resources.getString(R.string.search_hint)
		searchView.imeOptions = EditorInfo.IME_ACTION_DONE
		searchView.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
	}

	private fun setWordInfoFragmentListener(fragment: Fragment?) {
		(fragment as WordInfoDialog).setDialogListener(viewModel::onDialogDismiss)
	}

	private fun showWordInfoFragment(
		viewState: SearchViewState.DialogState,
		manager: FragmentManager
	) {
		WordInfoDialog.getInstance(viewState.textId, viewState.translationId)
			.also { dialog -> dialog.setDialogListener(viewModel::onDialogDismiss) }
			.show(manager, WordInfoDialog::class.simpleName)
	}

}

