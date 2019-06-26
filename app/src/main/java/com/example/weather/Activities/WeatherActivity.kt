package com.example.weather.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.weather.Fragments.WeatherFragment
import com.example.weather.R
import com.example.weather.ViewModels.WeatherViewModel
import android.arch.lifecycle.Observer

class WeatherActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val weatherViewModel: WeatherViewModel = WeatherViewModel()

    private lateinit var mViewPager: ViewPager

//    private var weatherViewModelObserver: Observer<WeatherViewModel> = Observer<WeatherViewModel> {newCityCount ->
//        if (this::mViewPager.isInitialized) {
//            this.mViewPager.adapter?.notifyDataSetChanged()
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.weather_drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        mViewPager = findViewById(R.id.weather_view_pager)
        mViewPager.adapter = WeatherViewPager(this.supportFragmentManager)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.weather_drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private inner class WeatherViewPager(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): WeatherFragment {
            return WeatherFragment.newInstance(position, weatherViewModel)
        }

        override fun getCount(): Int {
            return weatherViewModel.getCityCount()
        }
    }
}
