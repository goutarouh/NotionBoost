package com.github.goutarouh.notionboost.data

import com.github.goutarouh.notionboost.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.ZonedDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().apply {
            registerTypeAdapter(
                ZonedDateTime::class.java,
                JsonDeserializer { json, _, _ ->
                    ZonedDateTime.parse(json.asJsonPrimitive.asString);
                }
            )
        }.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(ApiHeaderInterceptor(
                authorization = BuildConfig.API_KEY,
                notionVersion = "2022-06-28"
            ))
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.notion.com/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideNotionRemoteApi(
        retrofit: Retrofit
    ): NotionRemoteApi {
        return retrofit.create(NotionRemoteApi::class.java)
    }

}