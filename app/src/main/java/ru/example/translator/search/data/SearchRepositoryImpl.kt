package ru.example.translator.search.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.example.core.network.RetrofitFactory
import ru.example.database.databaseworker.DataBaseWorker
import ru.example.translator.search.domain.SearchRepository
import ru.example.translator.search.domain.model.Text
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
	private val retrofitFactory: RetrofitFactory,
	private val dataBaseWorker: DataBaseWorker,
	private val mapper: MapperResponse
) : SearchRepository {

	private val api by lazy(LazyThreadSafetyMode.NONE) { retrofitFactory.getApi(Api::class.java) }

	override suspend fun fetchMeanings(query: String): List<Text> {
		return api.fetchTranslations(query).let {
			if (it.code() != 200) {
				throw RuntimeException("Code ${it.code()}, message - ${it.message()}")
			} else {
				mapper.mapToDomainModel(it.body())
					.also { meanings ->
						CoroutineScope(Dispatchers.IO).launch {
							dataBaseWorker.saveTexts(mapper.mapMeaningsTextToDbModel(meanings))
							dataBaseWorker.saveTranslations(
								mapper.mapMeaningToTranslationDbModel(meanings)
							)
						}
					}
			}
		}
	}
}