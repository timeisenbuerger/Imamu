package com.github.tei.imamu.di

import com.github.tei.imamu.data.database.ObjectBox
import com.github.tei.imamu.data.repository.*
import com.github.tei.imamu.viewmodel.cookbook.AddCookBookViewModel
import com.github.tei.imamu.viewmodel.cookbook.ChooseRecipeViewModel
import com.github.tei.imamu.viewmodel.cookbook.CookBookDetailViewModel
import com.github.tei.imamu.viewmodel.cookbook.CookBookListViewModel
import com.github.tei.imamu.viewmodel.finder.RecipeFinderResultListViewModel
import com.github.tei.imamu.viewmodel.finder.RecipeFinderSearchViewModel
import com.github.tei.imamu.viewmodel.home.HomeViewModel
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
    single { LastViewedRecipeRepository(get()) }
    single { RecipeIngredientRepository(get()) }
    single { IngredientRepository(get()) }
    single { CookBookRepository(get()) }
    single { LastViewedCookBookRepository(get()) }
    single { ShoppingListRepository(get()) }
    single { ShoppingListItemRepository(get()) }
}

val viewModelModule = module {

    viewModel { HomeViewModel(get(), get(), get()) }

    viewModel { RecipeListViewModel(get()) }
    viewModel { RecipeDetailViewModel(get(), get()) }
    viewModel { ImportRecipeViewModel(get(), get()) }
    viewModel { AddRecipeViewModel(get(), get()) }
    viewModel { EditRecipeViewModel(get(), get()) }

    viewModel { CookBookListViewModel(get()) }
    viewModel { CookBookDetailViewModel(get(), get()) }
    viewModel { AddCookBookViewModel(get()) }
    viewModel { ChooseRecipeViewModel(get(), get()) }

    viewModel { ShoppingListViewModel(get()) }
    viewModel { ShoppingListDetailViewModel(get(), get()) }

    viewModel { RecipeFinderSearchViewModel(get(), get()) }
    viewModel { RecipeFinderResultListViewModel(get()) }
}
