package com.github.tei.imamu.view.home

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.home.FavoriteListAdapter
import com.github.tei.imamu.custom.adapter.home.LastViewedCookBookListAdapter
import com.github.tei.imamu.custom.adapter.home.LastViewedRecipeListAdapter
import com.github.tei.imamu.custom.adapter.home.ShortcutAdapter
import com.github.tei.imamu.data.database.entity.cookbook.CookBook
import com.github.tei.imamu.data.database.entity.recipe.Recipe
import com.github.tei.imamu.databinding.FragmentHomeBinding
import com.github.tei.imamu.viewmodel.home.HomeViewModel
import com.github.tei.imamu.wrapper.ShortcutWrapper
import org.koin.android.ext.android.inject
import kotlin.math.abs

class HomeFragment : Fragment()
{
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by inject()
    private lateinit var application: Application
    private lateinit var shortcutAdapter: ShortcutAdapter
    private lateinit var favoriteListAdapter: FavoriteListAdapter
    private lateinit var lastViewedRecipeListAdapter: LastViewedRecipeListAdapter
    private lateinit var lastViewedCookBookListAdapter: LastViewedCookBookListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        init(inflater, container)
        initComponents()
        initObserver()
        initListener()

        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //set lifecycle owner
        binding.lifecycleOwner = viewLifecycleOwner

        //set viewModel in binding
        binding.viewModel = viewModel

        shortcutAdapter = ShortcutAdapter(viewModel)
        binding.shortcuts.adapter = shortcutAdapter

        //set adapter in recyclerview
        favoriteListAdapter = FavoriteListAdapter(viewModel)
        binding.favoritesList.adapter = favoriteListAdapter

        lastViewedRecipeListAdapter = LastViewedRecipeListAdapter(viewModel)
        binding.lastViewedRecipesList.adapter = lastViewedRecipeListAdapter

        lastViewedCookBookListAdapter = LastViewedCookBookListAdapter(viewModel)
        binding.lastViewedCookBooksList.adapter = lastViewedCookBookListAdapter

        val managerShortcuts = GridLayoutManager(activity, 2)
        binding.shortcuts.layoutManager = managerShortcuts
    }

    private fun initComponents()
    {
        val shortcuts: List<ShortcutWrapper> = initShortcutData()
        shortcutAdapter.submitList(shortcuts)

        initViewPager(binding.favoritesList)
        initViewPager(binding.lastViewedRecipesList)
        initViewPager(binding.lastViewedCookBooksList)
    }

    private fun initViewPager(viewPager: ViewPager2)
    {
        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 3

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = (0.95f + r * 0.05f)
        }

        viewPager.setPageTransformer(compositePageTransformer)
    }

    private fun initObserver()
    {
        viewModel.navigateToShortcut.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it.name)
                {
                    "Rezepte"          -> findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavRecipeList())
                    "Kochbücher"       -> findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavCookbook())
                    "Einkaufslisten"   -> findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavShoppingList())
                    "Rezeptsuche"      -> findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavRecipeSuggestion())
                }
            }
        })

        viewModel.favoriteRecipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.size == 0)
                {
                    setLayoutVisibility(View.GONE, binding.textViewCaptionLastViewedRecipes, binding.lastViewedRecipesList, null)
                }
                else
                {
                    setLayoutVisibility(View.VISIBLE, binding.textViewCaptionFavorites, binding.favoritesList, binding.textViewShowAllFavorites)
                    favoriteListAdapter.submitList(it)
                    binding.favoritesList.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                }
            }
        })

        viewModel.lastViewedRecipes.observe(viewLifecycleOwner, Observer {
            it?.let {
                val recipes: MutableList<Recipe> = mutableListOf()
                for (lastViewedRecipe in it)
                {
                    lastViewedRecipe.recipe.target?.let {
                        recipes.add(it)
                    }
                }

                if (recipes.size == 0)
                {
                    setLayoutVisibility(View.GONE, binding.textViewCaptionLastViewedRecipes, binding.lastViewedRecipesList, null)
                }
                else
                {
                    setLayoutVisibility(View.VISIBLE, binding.textViewCaptionLastViewedRecipes, binding.lastViewedRecipesList, null)
                    lastViewedRecipeListAdapter.submitList(recipes)
                    binding.lastViewedRecipesList.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                }
            }
        })

        viewModel.lastViewedCookBooks.observe(viewLifecycleOwner, Observer {
            it?.let {
                val cookBooks: MutableList<CookBook> = mutableListOf()
                for (lastViewedCookBook in it)
                {
                    lastViewedCookBook.cookBook.target?.let {
                        cookBooks.add(lastViewedCookBook.cookBook.target)
                    }
                }

                if (cookBooks.size == 0)
                {
                    setLayoutVisibility(View.GONE, binding.textViewCaptionLastViewedCookBooks, binding.lastViewedCookBooksList, null)
                }
                else
                {
                    setLayoutVisibility(View.VISIBLE, binding.textViewCaptionLastViewedCookBooks, binding.lastViewedCookBooksList, null)
                    lastViewedCookBookListAdapter.submitList(cookBooks)
                    binding.lastViewedRecipesList.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                }
            }
        })

        viewModel.navigateToRecipeDetail.observe(viewLifecycleOwner, Observer { recipe ->
            recipe?.let {
                findNavController().navigate(HomeFragmentDirections.actionNavHomeToRecipeDetailFragment(recipe))
                viewModel.onNavigateToRecipeDetailComplete()
            }
        })

        viewModel.navigateToCookBookDetail.observe(viewLifecycleOwner, Observer { cookBook ->
            cookBook?.let {
                findNavController().navigate(HomeFragmentDirections.actionNavHomeToCookBookDetailFragment(cookBook))
                viewModel.onNavigateToCookBookDetailComplete()
            }
        })
    }

    private fun initListener()
    {
        binding.textViewShowAllFavorites.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToFavoriteRecipesFragment())
        }
    }

    private fun initShortcutData(): MutableList<ShortcutWrapper>
    {
        val shortcutRecipe = ShortcutWrapper("Rezepte", R.mipmap.recipe_colored)
        val shortcutCookBook = ShortcutWrapper("Kochbücher", R.mipmap.cook_book_colored)
        val shortcutShoppingList = ShortcutWrapper("Einkaufslisten", R.mipmap.shopping_list_colored)
        val shortcutRecipeFinder = ShortcutWrapper("Rezeptsuche", R.mipmap.recipe_finder_colored)
        return mutableListOf(shortcutRecipe, shortcutCookBook, shortcutShoppingList, shortcutRecipeFinder)
    }

    private fun setLayoutVisibility(visibility: Int, textView: View, viewPager: View, view: View?)
    {
        textView.visibility = visibility
        viewPager.visibility = visibility

        view?.let {
            it.visibility = visibility
        }
    }

    override fun onResume()
    {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = "Startseite"
    }
}
