package com.github.tei.imamu.view.cookbook

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.tei.imamu.MainActivity
import com.github.tei.imamu.R
import com.github.tei.imamu.custom.adapter.cookbook.CookBookListAdapter
import com.github.tei.imamu.databinding.FragmentCookbookListBinding
import com.github.tei.imamu.util.ImportUtil
import com.github.tei.imamu.viewmodel.cookbook.CookBookListViewModel
import org.koin.android.ext.android.inject

class CookBookListFragment : Fragment()
{
    companion object
    {
        const val REQUEST_CODE = 28
    }

    private lateinit var binding: FragmentCookbookListBinding
    private val viewModel: CookBookListViewModel by inject()
    private lateinit var application: Application
    private lateinit var listAdapter: CookBookListAdapter

    private lateinit var fabOpenAnim: Animation
    private lateinit var fabCloseAnim: Animation
    private var isOpen = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as MainActivity).supportActionBar?.title = "Kochbücher"

        init(inflater, container)
        initComponents()
        initListener()
        initObserver()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?)
    {
        //init binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cookbook_list, container, false)

        //init application
        application = requireNotNull(this.activity).application

        //set lifecycle owner
        binding.lifecycleOwner = this

        //set viewModel in binding
        binding.viewModel = viewModel

        //set adapter in recyclerview
        listAdapter = CookBookListAdapter(viewModel, requireContext())
        binding.cookbookList.adapter = listAdapter

        val manager = LinearLayoutManager(activity)
        binding.cookbookList.layoutManager = manager
    }

    private fun initListener()
    {
        binding.fabMain.setOnClickListener {
            if (isOpen)
            {
                binding.createCookBookFab.animation = fabCloseAnim
                binding.importCookBookFab.animation = fabCloseAnim

                binding.textViewCreate.visibility = View.INVISIBLE
                binding.textViewImport.visibility = View.INVISIBLE

                isOpen = false
            }
            else
            {
                binding.createCookBookFab.animation = fabOpenAnim
                binding.importCookBookFab.animation = fabOpenAnim

                binding.textViewCreate.visibility = View.VISIBLE
                binding.textViewImport.visibility = View.VISIBLE

                isOpen = true
            }
        }

        binding.createCookBookFab.setOnClickListener {
            viewModel.onNavigateToAdd()
            isOpen = false
        }

        binding.importCookBookFab.setOnClickListener {

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/json"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)

            startActivityForResult(intent, REQUEST_CODE)

            isOpen = false
        }
    }

    private fun initObserver()
    {
        viewModel.cookBooks.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.allCookBooks = it
                listAdapter.submitList(it)
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(CookBookListFragmentDirections.actionNavCookbookToCookBookDetailFragment(it))
                viewModel.onNavigateToDetailComplete()
            }
        })

        viewModel.navigateToAdd.observe(viewLifecycleOwner, Observer {
            if (it)
            {
                findNavController().navigate(CookBookListFragmentDirections.actionNavCookbookToAddCookBookFragment(null))
                viewModel.onNavigateToAddComplete()
            }
        })
    }

    private fun initComponents()
    {
        fabOpenAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        fabCloseAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode != REQUEST_CODE || resultCode == 0)
        {
            return
        }

        ImportUtil.importCookBook(viewModel, requireContext(), data!!.data)
        viewModel.initCookBooks()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_action_search_view, menu)
        val item = menu.findItem(R.id.action_search)
        item?.let {
            val searchView = it.actionView as SearchView
            searchView.isIconified = false
            val textListener = object : SearchView.OnQueryTextListener
            {
                override fun onQueryTextSubmit(query: String?): Boolean
                {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean
                {
                    listAdapter.filter.filter(newText)
                    return true
                }
            }

            searchView.setOnQueryTextListener(textListener)
        }
    }
}