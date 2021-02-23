package ru.example.translator.search.presentation.mapper

import ru.example.translator.search.domain.model.Text
import ru.example.translator.search.domain.model.Translation
import ru.example.translator.search.presentation.model.SearchItem
import javax.inject.Inject

interface MapperViewModel {
	fun mapToViewModel(items: List<Text>): List<SearchItem>
	fun changeItemClosedToOpen(id: Long, searchItems: List<SearchItem>): List<SearchItem>
	fun changeItemOpenToClosed(id: Long, searchItems: List<SearchItem>): List<SearchItem>
	fun removeSearchItemTranslated(
		parentItemId: Long, viewDataItems: List<SearchItem>,
	): List<SearchItem>

	fun addSearchItemTranslated(
		parentItemId: Long, viewDataItems: List<SearchItem>,
	): List<SearchItem>
}

class MapperViewModelImpl @Inject constructor() : MapperViewModel {

	override fun mapToViewModel(items: List<Text>): List<SearchItem> {
		return mutableListOf<SearchItem>().also { adapterTypes ->
			items.forEach { meaningsItem ->
				if (meaningsItem.translations.size > 1) {
					adapterTypes.add(
						SearchItem.SearchItemClosedCombine(
							id = meaningsItem.id,
							text = meaningsItem.text,
							translation = getTranslation(meaningsItem.translations),
							wordsNumber = meaningsItem.translations.size,
							translationItems = getTranslationItems(
								meaningsItem.translations,
								meaningsItem.id
							),
						)
					)
				} else {
					adapterTypes.add(
						SearchItem.SearchItemTranslationWithText(
							id = meaningsItem.translations.first().id,
							text = meaningsItem.text,
							translation = meaningsItem.translations.first().translation,
							previewUrl = meaningsItem.translations.first().previewUrl,
							textId = meaningsItem.id
						)
					)
				}
			}
		}
	}

	override fun removeSearchItemTranslated(
		parentItemId: Long,
		viewDataItems: List<SearchItem>,
	): List<SearchItem> {
		val parentPosition = viewDataItems.indexOfFirst { it.id == parentItemId }
		return viewDataItems.toMutableList()
			.also { searchItems ->
				if ((viewDataItems[parentPosition] !is SearchItem.SearchItemOpenCombine)) return viewDataItems
				searchItems.removeAll(
					(viewDataItems[parentPosition] as SearchItem.SearchItemOpenCombine).translationItems
				)
			}
	}

	override fun addSearchItemTranslated(
		parentItemId: Long,
		viewDataItems: List<SearchItem>,
	): List<SearchItem> {
		val parentPosition = viewDataItems.indexOfFirst { it.id == parentItemId }
		return viewDataItems.toMutableList()
			.also { searchItems ->
				if ((viewDataItems[parentPosition] !is SearchItem.SearchItemClosedCombine) ||
					searchItems.containsAll((viewDataItems[parentPosition] as SearchItem.SearchItemClosedCombine).translationItems)
				) return viewDataItems
				searchItems.addAll(
					parentPosition + 1,
					(viewDataItems[parentPosition] as SearchItem.SearchItemClosedCombine).translationItems
				)
			}
	}

	override fun changeItemClosedToOpen(id: Long, searchItems: List<SearchItem>): List<SearchItem> {
		var indexChangedItem: Int
		val tempSearchItems = mutableListOf<SearchItem>().apply { addAll(searchItems) }
		return searchItems.indexOfFirst { it.id == id }
			.let { index ->
				indexChangedItem = index
				if (searchItems[index] !is SearchItem.SearchItemClosedCombine) return searchItems
				searchItems[index] as SearchItem.SearchItemClosedCombine
			}
			.let { searchItem ->
				SearchItem.SearchItemOpenCombine(
					id = searchItem.id,
					text = searchItem.text,
					translation = searchItem.translation,
					translationItems = searchItem.translationItems,
					wordsNumber = searchItem.wordsNumber,
				)
			}
			.let { newSearchItem ->
				tempSearchItems.apply {
					removeAt(indexChangedItem)
					add(indexChangedItem, newSearchItem)
				}
			}
	}

	override fun changeItemOpenToClosed(id: Long, searchItems: List<SearchItem>): List<SearchItem> {
		var indexChangedItem: Int
		val tempSearchItems = mutableListOf<SearchItem>().apply { addAll(searchItems) }
		return searchItems.indexOfFirst { it.id == id }
			.let { index ->
				indexChangedItem = index
				if (searchItems[index] !is SearchItem.SearchItemOpenCombine) return searchItems
				searchItems[index] as SearchItem.SearchItemOpenCombine
			}
			.let { searchItem ->
				SearchItem.SearchItemClosedCombine(
					id = searchItem.id,
					text = searchItem.text,
					translation = searchItem.translation,
					translationItems = searchItem.translationItems,
					wordsNumber = searchItem.wordsNumber,
				)
			}
			.let { newSearchItem ->
				tempSearchItems.apply {
					removeAt(indexChangedItem)
					add(indexChangedItem, newSearchItem)
				}
			}
	}

	private fun getTranslationItems(meanings: List<Translation>, textId: Long):
			List<SearchItem.SearchItemTranslation> {
		return meanings.map { translation ->
			SearchItem.SearchItemTranslation(
				id = translation.id,
				translation = translation.translation,
				previewUrl = translation.previewUrl,
				textId = textId
			)
		}
	}

	private fun getTranslation(meanings: List<Translation>): String {
		val stringBuilder = StringBuilder()
		meanings.forEach { stringBuilder.append("${it.translation}, ") }
		return stringBuilder.dropLast(2).toString()
	}

}