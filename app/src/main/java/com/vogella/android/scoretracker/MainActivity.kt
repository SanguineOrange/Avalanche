package com.vogella.android.scoretracker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.MotionEventCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.vogella.android.scoretracker.ui.page_fragments.nextGameFragment
import com.vogella.android.scoretracker.ui.page_fragments.roster.RosterFragment
import com.vogella.android.scoretracker.ui.page_fragments.schedule.ScheduleFragment
import com.vogella.android.scoretracker.ui.page_fragments.score.ScoreFragment
import com.vogella.android.scoretracker.ui.page_fragments.standings.StandingsFragment
import java.lang.Exception


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //sets the toolbar as the action bar since the default style will disable it
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //this established the drawer layout
        drawer = findViewById(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        //Don't reset displayed fragment when rotated, was causing crashes on score and schedule screens
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    ScheduleFragment()
                ).commit()
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    ScoreFragment()
                ).commit()
        }
    }

   //small function to make back button shut drawer
    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    //handle drawer menu item selection
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_score -> {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        ScoreFragment()
                    ).commit()
            }
            R.id.nav_schedule -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    ScheduleFragment()
                ).commit()
            }
            R.id.nav_standings -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,
                        StandingsFragment()
                    ).commit()
            }
            R.id.nav_roster -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,
                        RosterFragment()
                    ).commit()
            }
            R.id.nav_tickets -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,
                        nextGameFragment()
                    ).commit()
            }

            //facebook intent stuff was modified from code found at: https://gist.github.com/layerlre/163479670ac8e2bc512330193bcefd1c
            R.id.nav_facebook ->{
                var faceIntent : Intent
                //first asks if facebook app is installed, if it is it will attempt to pass intent there
                try{
                    val facebookVersion = packageManager.getPackageInfo("com.facebook.katana",0).versionCode
                    //since facebook changed how katana reads uris, this if statement passes the proper format
                    if(facebookVersion >= 3002850){
                        faceIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/coloradoavalanche/"))
                        faceIntent.setPackage("com.facebook.katana")
                    }
                    else{
                        faceIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("fb://page" + "/coloradoavalanche")
                        )
                        faceIntent.setPackage("com.facebook.katana")
                    }
                }
                //if facebook isn't installed it will catch the error and pass intent to browser
                catch (e: Exception){
                    faceIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://m.facebook.com/coloradoavalanche/"))
                }
                startActivity(faceIntent)
            }

            R.id.nav_twitter -> {
                var twitIntent : Intent
                //Ask package manager if twitter app is installed
                try{
                    //if it is just send the intent there
                    twitIntent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=Avalanche"))
                    twitIntent.setPackage("com.twitter.android")
                }
                catch(e : Exception){
                    //if its not then send the intent to the browser
                    twitIntent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://twitter.com/avalanche"))
                }
                startActivity(twitIntent)
            }

            R.id.nav_insta -> {
                var instaIntent : Intent
                //Ask package manager if instagram app is installed
                try{
                    //if it is, intent goes there same as other two
                    instaIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/coloradoavalanche"))
                    instaIntent.setPackage("com.instagram.android")
                }
                //if its not it sends intent to the browser
                catch(e : Exception){
                    instaIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/coloradoavalanche"))
                }
                startActivity(instaIntent)
            }
        }
        //close drawer on button press
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

}
