package com.mohitb117.stonks.injection

import com.mohitb117.stonks.endpoints.Api
import com.slack.eithernet.ApiResultCallAdapterFactory
import com.slack.eithernet.ApiResultConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

@Module
@InstallIn(
    ActivityComponent::class,
    ViewModelComponent::class,
    SingletonComponent::class,
)
class NetworkingModule {

    @Provides
    fun provideRetrofitApi(): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(STONKS_BASE_URL)
            .client(client)
            .addConverterFactory(ApiResultConverterFactory)
            .addCallAdapterFactory(ApiResultCallAdapterFactory)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .build()
    }

    @Provides
    fun provideSTONKSApiEndpoints(retrofit: Retrofit): Api =
        retrofit.create()

    companion object {
        const val STONKS_BASE_URL = "https://storage.googleapis.com/cash-homework/cash-stocks-api/"

        const val DETAILS_BASE_URL = "https://api.polygon.io/v1/open-close/{ticker}/2020-10-14?adjusted=true&apiKey={api_key}"
    }
}