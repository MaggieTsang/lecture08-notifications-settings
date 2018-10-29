package edu.uw.notifysettingdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //to handle the launch button
    fun launchActivity(v: View) {
        startActivity(Intent(this, SecondActivity::class.java)) //quick launch
    }

    //to handle the notification button
    fun showNotification(v: View) {
        Log.d(TAG, "Notify button pressed")


    }


    //to handle the alert button
    fun showAlert(v: View) {
        Log.d(TAG, "Alert button pressed")

    }



    /* Menu handling */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_settings -> {
                Log.d(TAG, "Settings menu pressed")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
