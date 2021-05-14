package com.kafleyozone.coin

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.util.CoilUtils
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient

@HiltAndroidApp
class CoinApplication : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext)
            .crossfade(true)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(applicationContext))
                    .build()
            }
            .componentRegistry {
                add(SvgDecoder(applicationContext))
            }
            .build()
    }
}