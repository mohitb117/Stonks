package com.mohitb117.stonks.injection

import com.mohitb117.stonks.common.DefaultOnDataMismatchAdapter
import com.mohitb117.stonks.datamodels.Portfolio
import com.mohitb117.stonks.endpoints.DetailsApi
import com.mohitb117.stonks.endpoints.ListApi
import com.slack.eithernet.ApiResultCallAdapterFactory
import com.slack.eithernet.ApiResultConverterFactory
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.lang.reflect.Type

@Module
@InstallIn(
    ViewModelComponent::class,
    SingletonComponent::class,
)
class NetworkingModule {

    private val lenientFactory: JsonAdapter.Factory = object : JsonAdapter.Factory {
        override fun create(
            type: Type,
            annotations: MutableSet<out Annotation>,
            moshi: Moshi
        ): JsonAdapter<*> {
            return moshi.nextAdapter<Any>(this, type, annotations).lenient()
        }
    }
    private val defaultOnDataMismatchAdapter = DefaultOnDataMismatchAdapter.newFactory(Portfolio::class.java, Portfolio(emptyList()))

    private fun createRetrofitBuilder(
        okHttpClient: OkHttpClient,
        baseUrl: String
    ): Retrofit.Builder {

        val moshi = Moshi.Builder()
            .add(lenientFactory)
            .add(defaultOnDataMismatchAdapter)
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val factory = MoshiConverterFactory.create(moshi).asLenient()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(ApiResultConverterFactory)
            .addCallAdapterFactory(ApiResultCallAdapterFactory)
            .addConverterFactory(factory)
    }

    @Provides
    fun provideRetrofitApi(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    fun provideSTONKSApiEndpoints(okHttpClient: OkHttpClient): ListApi =
        createRetrofitBuilder(okHttpClient, STONKS_BASE_URL)
            .build()
            .create()

    @Provides
    fun provideDetailsApiEndpoints(okHttpClient: OkHttpClient): DetailsApi =
        createRetrofitBuilder(okHttpClient, DETAILS_BASE_URL)
            .build()
            .create()

    companion object {
        const val STONKS_BASE_URL = "https://storage.googleapis.com/cash-homework/cash-stocks-api/"

        const val DETAILS_BASE_URL = "https://api.polygon.io/v1/open-close/{ticker}/2020-10-14/"
    }
}