package ru.example.translator.search.presentation.adapter.delegates

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.search_item_vh.view.*
import ru.example.core.adapter.AdapterDelegate
import ru.example.core.adapter.BaseViewHolder
import ru.example.translator.R
import ru.example.translator.search.presentation.model.SearchItem

class SearchItemTranslationDelegate(
	private val itemClickListener: (textId: Long, translationId: Long) -> Unit
) : AdapterDelegate<SearchItem> {
	override fun onCreateViewHolder(parent: ViewGroup) = TranslateVH(parent)

	override fun isValidType(viewData: SearchItem) = viewData is SearchItem.SearchItemTranslation

	inner class TranslateVH(parent: ViewGroup) :
		BaseViewHolder<SearchItem>(parent, R.layout.search_item_vh) {
		override fun bind(data: SearchItem) {
			data as SearchItem.SearchItemTranslation
			with(itemView) {
				setText(data)
				loadImage(data)
				initClickListener(data)
			}
		}

		private fun View.setText(data: SearchItem.SearchItemTranslation) {
			tvSearchItemTranslation.text = data.translation
		}

		private fun View.initClickListener(data: SearchItem.SearchItemTranslation) {
			searchItemContainer.setOnClickListener {
				itemClickListener.invoke(
					data.textId,
					data.id
				)
			}
		}

		private fun View.loadImage(data: SearchItem.SearchItemTranslation) {
			ivSearchItemPreview.clipToOutline = true
			Glide.with(context).load(data.previewUrl)
				.error(R.drawable.no_image_available)
				.into(ivSearchItemPreview)
		}
	}
}