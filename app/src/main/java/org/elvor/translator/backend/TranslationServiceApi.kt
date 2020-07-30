package org.elvor.translator.backend

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslationServiceApi {
    @GET("m.exe")
    fun getTranslations(@Query("s") query: String, @Query("l1") sourceLanguageId: Int, @Query("l2") targetLanguageId: Int): Observable<ResponseBody>
}