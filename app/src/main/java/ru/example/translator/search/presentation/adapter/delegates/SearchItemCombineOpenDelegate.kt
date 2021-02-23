package ru.example.translator.search.presentation.adapter.delegates

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.search_item_combine_closed_vh.view.*
import kotlinx.android.synthetic.main.search_item_combine_open_vh.view.*
import ru.example.core.adapter.AdapterDelegate
import ru.example.core.adapter.BaseViewHolder
import ru.example.translator.R
import ru.example.translator.search.presentation.model.SearchItem

class SearchItemCombineOpenDelegate(
	private val openItemClickListener: (id: Long) -> Unit,
	private val combineOpenAnimListener: (id: Long) -> Unit
) : AdapterDelegate<SearchItem> {
	override fun onCreateViewHolder(parent: ViewGroup) = CombineClosedVH(parent)

	override fun isValidType(viewData: SearchItem) = viewData is SearchItem.SearchItemOpenCombine

	inner class CombineClosedVH(parent: ViewGroup) :
		BaseViewHolder<SearchItem>(parent, R.layout.search_item_combine_open_vh) {

		override fun bind(data: SearchItem) {
			data as SearchItem.SearchItemOpenCombine
			with(itemView) {
				if (ivCombineOpenArrow.y > 0f){
					ObjectAnimator.ofFloat(
						ivCombineOpenArrow, "rotation", 180f, 0f
					).apply {
						duration = 1
						start()
					}
				}
				tvCombineOpenText.text = data.text
				tvCombineOpenTranslation.text = data.translation
				tvCombineOpenWordsNumber.text = data.wordsNumber.toString()
				setOnClickListener { rotateArrowToCloseState(data.id) }
			}
		}

		private fun View.rotateArrowToCloseState(id: Long) {
			ObjectAnimator.ofFloat(
				ivCombineOpenArrow, "rotation", 0f, 180f
			).apply {
				duration = 500
				addListener(object : Animator.AnimatorListener {
					override fun onAnimationStart(animation: Animator?) {
						openItemClickListener.invoke(id)
					}

					override fun onAnimationEnd(animation: Animator?) {
						combineOpenAnimListener.invoke(id)
					}

					override fun onAnimationCancel(animation: Animator?) {
						//empty
					}

					override fun onAnimationRepeat(animation: Animator?) {
						//empty
					}

				})
				start()
			}
		}
	}
}