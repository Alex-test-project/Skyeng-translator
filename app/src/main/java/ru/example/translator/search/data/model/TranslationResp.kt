package ru.example.translator.search.data.model

import com.google.gson.annotations.SerializedName

class MeaningsResp(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("text")
    val text: String?,
    @SerializedName("meanings")
    val meanings: List<MeaningResp>?
)

class MeaningResp(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("partOfSpeechCode")
    val partOfSpeechCode: String?,
    @SerializedName("previewUrl")
    val previewUrl: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("transcription")
    val transcription: String?,
    @SerializedName("soundUrl")
    val soundUrl: String?,
    @SerializedName("translation")
    val translation: TranslationResp?
)

class TranslationResp(
    @SerializedName("text")
    val text: String?,
    @SerializedName("note")
    val note: String?
)