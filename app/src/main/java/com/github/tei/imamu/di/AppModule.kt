package com.github.tei.imamu.di

import com.github.tei.imamu.data.ObjectBox
import com.github.tei.imamu.data.repository.*
import com.github.tei.imamu.viewmodel.cookbook.AddCookBookViewModel
import com.github.tei.imamu.viewmodel.cookbook.ChooseRecipeViewModel
import com.github.tei.imamu.viewmodel.cookbook.CookBookDetailViewModel
import com.github.tei.imamu.viewmodel.cookbook.CookBookListViewModel
import com.github.tei.imamu.viewmodel.recipe.*
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListDetailViewModel
import com.github.tei.imamu.viewmodel.shoppinglist.ShoppingListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val boxModule = module {
    single { ObjectBox.init(androidContext()) }
}

val repositoryModule = module {
    single { RecipeRepository(get()) }
    single { RecipeIngredientRepository(get()) }
    single { IngredientRepository(get()) }
    single { CookBookRepository(get()) }
    single { ShoppingListRepository(get()) }
    single { ShoppingListItemRepository(get()) }
}

val viewModelModule = module {
    viewModel { CookBookListViewModel(get()) }
    viewModel { CookBookDetailViewModel(get()) }
    viewModel { AddCookBookViewModel(get()) }
    viewModel { ChooseRecipeViewModel(get(), get()) }

    viewModel { RecipeListViewModel(get()) }
    viewModel { RecipeDetailViewModel(get()) }
    viewModel { ImportRecipeViewModel(get(), get()) }
    viewModel { AddRecipeViewModel(get(), get()) }
    viewModel { EditRecipeViewModel(get(), get()) }

    viewModel { ShoppingListViewModel(get()) }
    viewModel { ShoppingListDetailViewModel(get(), get()) }
}
