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
import android.support.v4.view.PagerAdapter
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import com.example.weather.Fragments.CityListFragment
import com.example.weather.Models.WeatherModels.WeatherModel

class WeatherActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel = WeatherViewModel()

    private lateinit var weatherViewPager: ViewPager

    private var weatherModelsObserver: Observer<List<WeatherModel>> = Observer { newWeatherModels ->
        if (this::weatherViewPager.isInitialized) {
            this.weatherViewPager.adapter?.let {
                if (newWeatherModels!!.count() - it.count > 0) {
                    (it as WeatherViewPager).addFragment(newWeatherModels!!.count() - it.count)
                }
                else if (it.count - newWeatherModels!!.count() > 0) {
                    (it as WeatherViewPager).removeFragment(it.count - newWeatherModels!!.count())
                }
                it.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.weather_drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.city_weather_fragment, CityListFragment.newInstance(this.weatherViewModel))
        transaction.commit()

        this.weatherViewPager = findViewById(R.id.weather_view_pager)
        this.weatherViewPager.adapter = WeatherViewPager(this.supportFragmentManager)

        this.weatherViewModel.weatherModelsStatus.observe(this, this.weatherModelsObserver)
    }

    private inner class WeatherViewPager(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

        private var fragments: MutableList<WeatherFragment> = mutableListOf()

        fun addFragment(amount: Int = 1) {
            for (i in 1..amount){
                this.fragments.add(WeatherFragment.newInstance(this.fragments.count(), weatherViewModel))
            }
        }

        fun removeFragment(amount: Int = 1) {
            for (i in 1..amount){
                this.fragments.removeAt(this.fragments.lastIndex)
            }
        }

        override fun getItem(position: Int): WeatherFragment {
            return this.fragments[position]
        }

        override fun getCount(): Int {
            return this.fragments.count()
        }
    }
}
