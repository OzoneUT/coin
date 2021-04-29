package com.kafleyozone.coin.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.kafleyozone.coin.R
import com.kafleyozone.coin.data.domain.Categories
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val gson: Gson
) {
    companion object {
        const val TAG = "CategoryRepository"
    }

    private var _appCategories: Categories = Categories()
    val appCategories: Categories
        get() = _appCategories

    fun loadDefaultCategoriesFromFile(context: Context) {
        if (!_appCategories.areCategoriesListsEmpty()) {
            Log.i(TAG, "no need for init")
            return
        }
        val jsonString = context
            .resources
            .openRawResource(R.raw.default_categories)
            .bufferedReader()
            .use { it.readText() }
        _appCategories = gson.fromJson(jsonString, Categories::class.java)
        Log.i(TAG, "category init: $appCategories")
    }
}
//https://github.com/coil-kt/coil/issues/393
// You can reference Android assets using a uri in the following format: file:///android_asset/image.jpg.
// Once you have a uri you can call imageView.load(uri). For SVG support check out: https://coil-kt.github.io/coil/svgs/