package com.example.wandy_p2_ap2.di

import android.content.Context
import com.example.wandy_p2_ap2.data.remote.GastosApi
import com.example.wandy_p2_ap2.data.repository.CounterRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Singleton
    @Provides
    fun providesGastosApi(moshi: Moshi): GastosApi {
        return Retrofit.Builder()
            .baseUrl("https://sag-api.azurewebsites.net")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GastosApi::class.java)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule {
        @Singleton
        @Provides
        fun providePreferences(@ApplicationContext context: Context) = CounterRepository(context)
    }
}

