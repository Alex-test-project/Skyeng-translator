package ru.example.translator.search.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.example.core.adapter.PostAdapter
import ru.example.translator.search.presentation.adapter.delegates.SearchItemCombineClosedDelegate
import ru.example.translator.search.presentation.adapter.delegates.SearchItemCombineOpenDelegate
import ru.example.translator.search.presentation.adapter.delegates.SearchItemTranslationDelegate
import ru.example.translator.search.presentation.adapter.delegates.SearchItemTranslationWithTextDelegate
import ru.example.translator.search.presentation.model.SearchItem

class SearchAdapter(
	searchItemClickListener: (textId: Long, translationId: Long) -> Unit,
	combineOpenClickListener: (id: Long) -> Unit,
	combineOpenAnimListener: (id: Long) -> Unit,
	combineClosedClickListener: (id: Long) -> Unit,
	combineClosedAnimListener: (id: Long) -> Unit,
) : PostAdapter<SearchItem>(
	SearchItemTranslationWithTextDelegate(searchItemClickListener),
	SearchItemTranslationDelegate(searchItemClickListener),
	SearchItemCombineClosedDelegate(combineClosedClickListener, combineClosedAnimListener),
	SearchItemCombineOpenDelegate(combineOpenClickListener, combineOpenAnimListener)
) {

	override fun replaceWithDiff(input: List<SearchItem>) {
		val diffResult = DiffUtil.calculateDiff(
			SearchItemsDiffUtilCallBack(postModels, input)
		)
		diffResult.dispatchUpdatesTo(this)
		postModels.clear()
		postModels.addAll(input)
	}

}


class SearchItemsDiffUtilCallBack(
	private val old: List<SearchItem>,
	private val new: List<SearchItem>
) : DiffUtil.Callback() {

	override fun getOldListSize(): Int = old.size

	override fun getNewListSize(): Int = new.size

	override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
		old[oldItemPosition] == new[newItemPosition]

	override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return old[oldItemPosition].id == new[newItemPosition].id
	}
}