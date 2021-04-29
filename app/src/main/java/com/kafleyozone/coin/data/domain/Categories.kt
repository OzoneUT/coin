package com.kafleyozone.coin.data.domain

data class Categories(
    val incomeCategories: List<Category> = mutableListOf(),
    val expenseCategories: List<Category> = mutableListOf()
) {
    fun areCategoriesListsEmpty(): Boolean =
        incomeCategories.isEmpty() || expenseCategories.isEmpty()
}

data class Category(
    val id: String,
    val categoryName: String,
    val fileName: String
)