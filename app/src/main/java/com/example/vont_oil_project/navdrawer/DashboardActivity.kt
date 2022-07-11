package com.example.vont_oil_project.navdrawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.customnavigationdrawer.HomeFragment
import com.example.customnavigationdrawer.MessageFragment
import com.example.customnavigationdrawer.SettingsFragment
import com.example.vont_oil_project.R
import com.google.android.material.navigation.NavigationView

class DashboardActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        drawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        navView.setNavigationItemSelectedListener {
            it.isChecked = true

            when (it.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment(),it.title.toString())
                }
                R.id.nav_logout -> {
                    Toast.makeText(this, "clicked login", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_message -> {
                    replaceFragment(MessageFragment(),it.title.toString())
                }
                R.id.nav_settings -> {
                    replaceFragment(SettingsFragment(),it.title.toString())
                }
                R.id.nav_rate_review -> {
                    Toast.makeText(this, "clicked rate review", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_share -> {
                    Toast.makeText(this, "clicked  share", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_sync -> {
                    Toast.makeText(this, "clicked sync", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_trash -> {
                    Toast.makeText(this, "clicked trash", Toast.LENGTH_SHORT).show()
                }
            }
            true

        }
    }

    private fun replaceFragment(fragment: Fragment, title: String) {
        val fragmentManager =  supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}