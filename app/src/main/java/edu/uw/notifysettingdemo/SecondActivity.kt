package edu.uw.notifysettingdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class SecondActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MESSAGE = "edu.uw.notifysetting.message"
    }

    private val TAG = "SecondActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        //action bar "back"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras?.apply {
            val message = getString(EXTRA_MESSAGE)
            val subtitle = findViewById<TextView>(R.id.txt_second)
            subtitle.text = "Received: " + message
        }
    }
}
