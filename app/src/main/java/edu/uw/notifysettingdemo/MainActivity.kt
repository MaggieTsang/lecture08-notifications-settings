package edu.uw.notifysettingdemo

import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.DialogFragment
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private val NOTIFICATION_CHANNEL_ID = "my_channel_01" //channel ID
    private val NOTIFICATION_REQUEST = 1
    private val DEMO_NOTIFICATION_ID = 2
    private val NOTIFY_COUNT_KEY = "notify_count"

    private var notifyCount = 0

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

        notifyCount++

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_thumb_down)
                .setContentTitle("You're on notice!")
                .setContentText("This notice has been generated $notifyCount times")


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //Oreo support
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Demo channel", NotificationManager.IMPORTANCE_HIGH)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        } else {
            //everything else!
            builder.setPriority(NotificationCompat.PRIORITY_HIGH)
            builder.setVibrate(longArrayOf(0, 500, 500, 5000))
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
        }

        val intent = Intent(this, SecondActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setContentIntent(pendingIntent)

        //val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(DEMO_NOTIFICATION_ID, builder.build()) //post the notification!

    }


    //to handle the alert button
    fun showAlert(v: View) {
        Log.d(TAG, "Alert button pressed")

//        val builder = AlertDialog.Builder(this)
//        builder.apply {
//            setTitle("Alert!")
//            setMessage("Danger Will Robinson!")
//            setPositiveButton("I see it!") { dialog, id -> Log.v(TAG, "You clicked okay! Good times :)") }
//            setNegativeButton("Noooo...") { dialog, which -> Log.v(TAG, "You clicked cancel! Sad times :(") }
//        }
//
//        val dialog = builder.create()
//        dialog.show()

        AlertDialogFragment.newInstance().show(supportFragmentManager, null)
    }

    class AlertDialogFragment : DialogFragment() {

        private val TAG = "AlertDialogFragment"

        companion object {
            fun newInstance(): AlertDialogFragment {
                val args = Bundle()
                val fragment = AlertDialogFragment()
                fragment.arguments = args
                return fragment
            }
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("Alert!")
                        .setMessage("Danger Will Robinson!") //note chaining
                builder.setPositiveButton("I see it!") { dialog, id -> Log.v(TAG, "You clicked okay! Good times :)") }
                builder.setNegativeButton("Noooo...") { dialog, which -> Log.v(TAG, "You clicked cancel! Sad times :(") }

                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
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
