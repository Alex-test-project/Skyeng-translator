package ru.example.core

import android.widget.ImageView
import androidx.appcompat.widget.SearchView


fun SearchView.clear() {
	this.setQuery("", false)
	this.isIconified = true
	this.clearFocus()
}

fun SearchView.setEnable(isEnable: Boolean) {
	findViewById<ImageView>(androidx.appcompat.R.id.search_button)
		.apply { isEnabled = isEnable }
	findViewById<SearchView.SearchAutoComplete>(androidx.appcompat.R.id.search_src_text)
		.apply { isEnabled = isEnable }
	findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
		.apply { isEnabled = isEnable }
}