package com.github.tei.imamu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.github.tei.imamu.data.ObjectBox
import com.github.tei.imamu.databinding.ActivityMainBinding
import com.github.tei.imamu.util.PermissionUtil

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout

        setSupportActionBar(binding.toolbar)

        navController = this.findNavController(R.id.nav_host_fragment)
        val topLevelDestinations = setOf(R.id.nav_home, R.id.nav_recipe_list, R.id.nav_cookbook, R.id.nav_shopping_list, R.id.nav_recipe_suggestion, R.id.nav_settings)
        appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations).setDrawerLayout(drawerLayout).build()

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navView, navController)

        supportActionBar?.setDisplayShowTitleEnabled(true);

        ObjectBox.init(this)
    }

    override fun onResume()
    {
        super.onResume()

        //TODO dreckig, anders machen!
        PermissionUtil.grantStoragePermission(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}
