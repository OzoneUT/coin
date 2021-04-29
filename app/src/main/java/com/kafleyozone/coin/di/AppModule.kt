package com.kafleyozone.coin.di

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kafleyozone.coin.data.network.AccountService
import com.kafleyozone.coin.data.network.AuthenticationService
import com.kafleyozone.coin.data.network.CoinAuthenticator
import com.kafleyozone.coin.data.network.TokenInterceptor
import com.kafleyozone.coin.data.room.AppDatabase
import com.kafleyozone.coin.data.room.UserDao
import com.kafleyozone.coin.utils.BASE_URL
import com.kafleyozone.coin.utils.MOCK_BASE_URL
import com.kafleyozone.coin.utils.MOCK_DEBUG
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "AppDatabase"
        ).build()
    }

    @Singleton
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        tokenInterceptor: TokenInterceptor,
        coinAuthenticator: CoinAuthenticator
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .authenticator(coinAuthenticator)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(if (MOCK_DEBUG) MOCK_BASE_URL else BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthenticationService(retrofit: Retrofit): AuthenticationService =
        retrofit.create(AuthenticationService::class.java)

    @Singleton
    @Provides
    fun provideAccountService(retrofit: Retrofit): AccountService =
        retrofit.create(AccountService::class.java)

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) =
        context.createDataStore(name = "auth_store")

    @Singleton
    @Provides
    fun provideGson(): Gson =
        GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create()
}