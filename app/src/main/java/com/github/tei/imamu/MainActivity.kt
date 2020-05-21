package com.github.tei.imamu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.github.tei.imamu.databinding.ActivityMainBinding
import com.github.tei.imamu.di.boxModule
import com.github.tei.imamu.di.repositoryModule
import com.github.tei.imamu.di.viewModelModule
import com.github.tei.imamu.util.PermissionUtil
import io.objectbox.BoxStore
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class MainActivity : AppCompatActivity()
{
    lateinit var binding: ActivityMainBinding
    lateinit var drawerLayout: DrawerLayout
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout

        setSupportActionBar(binding.toolbar)

        navController = this.findNavController(R.id.nav_host_fragment)
        val topLevelDestinations = setOf(R.id.nav_home, R.id.nav_recipe_list, R.id.nav_cookbook, R.id.nav_shopping_list, R.id.nav_recipe_suggestion, R.id.nav_settings)
        appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
            .setDrawerLayout(drawerLayout)
            .build()

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navView, navController)

        supportActionBar?.setDisplayShowTitleEnabled(true);

        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(listOf(repositoryModule, boxModule, viewModelModule))
        }
    }

    override fun onResume()
    {
        super.onResume()

        //TODO dreckig, anders machen!
        PermissionUtil.grantStoragePermission(this)
    }

    override fun onSupportNavigateUp(): Boolean
    {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onDestroy()
    {
        super.onDestroy()
        stopKoin()
    }
}
