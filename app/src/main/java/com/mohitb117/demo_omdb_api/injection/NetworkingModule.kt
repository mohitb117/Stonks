package com.mohitb117.demo_omdb_api.injection

import com.google.gson.GsonBuilder
import com.mohitb117.demo_omdb_api.OmdbApiApp
import com.mohitb117.demo_omdb_api.endpoints.OMDBApi
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
import retrofit2.converter.gson.GsonConverterFactory
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
        val gson = GsonBuilder().disableHtmlEscaping().create()

        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(OMDB_BASE_URL)
            .client(client)
            .addConverterFactory(ApiResultConverterFactory)
            .addCallAdapterFactory(ApiResultCallAdapterFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideOmdbApiEndpoints(retrofit: Retrofit): OMDBApi =
        retrofit.create()

    companion object {
        const val OMDB_BASE_URL = "https://www.omdbapi.com/"
    }
}