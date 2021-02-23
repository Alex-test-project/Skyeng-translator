package ru.example.translator.search.presentation.adapter.delegates

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.search_item_combine_closed_vh.view.*
import ru.example.core.adapter.AdapterDelegate
import ru.example.core.adapter.BaseViewHolder
import ru.example.translator.R
import ru.example.translator.search.presentation.model.SearchItem

class SearchItemCombineClosedDelegate(
	private val closedItemClickListener: (id: Long) -> Unit,
	private val combineClosedAnimListener: (id: Long) -> Unit
) : AdapterDelegate<SearchItem> {
	override fun onCreateViewHolder(parent: ViewGroup) = CombineClosedVH(parent)

	override fun isValidType(viewData: SearchItem) = viewData is SearchItem.SearchItemClosedCombine

	inner class CombineClosedVH(parent: ViewGroup) :
		BaseViewHolder<SearchItem>(parent, R.layout.search_item_combine_closed_vh) {

		override fun bind(data: SearchItem) {
			data as SearchItem.SearchItemClosedCombine
			with(itemView) {
				if (ivCombineClosedArrow.y > 0f){
					ObjectAnimator.ofFloat(
						ivCombineClosedArrow, "rotation", 180f, 0f
					).apply {
						duration = 1
						start()
					}
				}
				tvCombineClosedText.text = data.text
				tvCombineClosedTranslation.text = data.translation
				tvCombineClosedWordsNumber.text = data.wordsNumber.toString()
				setOnClickListener { rotateArrowToOpenState(data.id) }
			}
		}

		private fun View.rotateArrowToOpenState(id: Long) {
			ObjectAnimator.ofFloat(
				ivCombineClosedArrow, "rotation", 0f, 180f
			).apply {
				duration = 500
				addListener(object : Animator.AnimatorListener {
					override fun onAnimationStart(animation: Animator?) {
						closedItemClickListener.invoke(id)
					}

					override fun onAnimationEnd(animation: Animator?) {
						combineClosedAnimListener.invoke(id)
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