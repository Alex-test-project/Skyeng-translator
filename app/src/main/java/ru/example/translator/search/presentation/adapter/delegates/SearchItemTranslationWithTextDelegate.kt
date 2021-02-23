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
import kotlinx.android.synthetic.main.search_item_with_text_vh.view.*
import kotlinx.android.synthetic.main.search_item_with_text_vh.view.searchItemContainer
import ru.example.core.adapter.AdapterDelegate
import ru.example.core.adapter.BaseViewHolder
import ru.example.translator.R
import ru.example.translator.search.presentation.model.SearchItem

class SearchItemTranslationWithTextDelegate(
    private val itemClickListener: (textId: Long, translationId: Long) -> Unit
) : AdapterDelegate<SearchItem> {
    override fun onCreateViewHolder(parent: ViewGroup) = TranslateWithTextVH(parent)

    override fun isValidType(viewData: SearchItem) = viewData is SearchItem.SearchItemTranslationWithText

    inner class TranslateWithTextVH(parent: ViewGroup) :
        BaseViewHolder<SearchItem>(parent, R.layout.search_item_with_text_vh) {
        override fun bind(data: SearchItem) {
            data as SearchItem.SearchItemTranslationWithText
            with(itemView) {
                setTexts(data)
                loadImage(data)
                initClickListener(data)
            }
        }

        private fun View.setTexts(data: SearchItem.SearchItemTranslationWithText) {
            tvSearchItemWithTextText.text = data.text
            tvSearchItemWithTextTranslation.text = data.translation
        }

        private fun View.initClickListener(data: SearchItem.SearchItemTranslationWithText) {
            searchItemContainer.setOnClickListener {
                itemClickListener.invoke(data.textId, data.id)
            }
        }

        private fun View.loadImage(data: SearchItem.SearchItemTranslationWithText) {
            ivSearchItemWithTextPreview.clipToOutline = true
            Glide.with(context).load(data.previewUrl)
                .error(R.drawable.no_image_available)
                .into(ivSearchItemWithTextPreview)
        }
    }
}