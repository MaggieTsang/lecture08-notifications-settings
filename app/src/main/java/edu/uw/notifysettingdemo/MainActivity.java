package edu.uw.notifysettingdemo;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private static final String NOTIFICATION_CHANNEL_ID = "my_channel_01"; //channel ID
    private static final int DEMO_PENDING_ID = 1;
    private static final int DEMO_NOTIFICATION_ID = 2;

    private int notifyCount = 0;
    private static final String NOTIFY_COUNT_KEY = "notify_count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences prefs = getSharedPreferences("TestPrefs", MODE_PRIVATE);
        this.notifyCount = prefs.getInt(NOTIFY_COUNT_KEY, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //save some details when app stops
        SharedPreferences prefs = getSharedPreferences("TestPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NOTIFY_COUNT_KEY,notifyCount);
        editor.commit();
    }

    //to handle the launch button
    public void launchActivity(View v){
        startActivity(new Intent(this, SecondActivity.class)); //quick launch
    }

    //to handle the notification button
    public void showNotification(View v){
        Log.d(TAG, "Notify button pressed");

        notifyCount++;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("pref_notify",true)) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_thumb_down)
                    .setContentTitle("You're on notice!")
                    .setContentText("This notice has been generated " + notifyCount + " times")
                    .setChannelId(NOTIFICATION_CHANNEL_ID);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                //Orea support
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Demo channel", NotificationManager.IMPORTANCE_HIGH);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
            } else {
                //everything else!
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.setVibrate(new long[]{0, 500, 500, 5000});
                builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            }

            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(SecondActivity.class);
            stackBuilder.addNextIntent(intent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(DEMO_PENDING_ID,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(DEMO_NOTIFICATION_ID, builder.build()); //post the notification!
        }
        else {
            //notifications turned off!
            Toast.makeText(this, "This notice has been generated "+notifyCount+" times", Toast.LENGTH_LONG).show();
        }
    }


    //to handle the alert button
    public void showAlert(View v){
        Log.d(TAG, "Alert button pressed");

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Alert!")
//                .setMessage("Danger Will Robinson!"); //note chaining
//        builder.setPositiveButton("I see it!", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                Log.v(TAG, "You clicked okay! Good times :)");
//            }
//        });
//        builder.setNegativeButton("Noooo...", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.v(TAG, "You clicked cancel! Sad times :(");
//            }
//        });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();

        AlertDialogFragment.newInstance().show(getSupportFragmentManager(), null);
    }

    public static class AlertDialogFragment extends DialogFragment {

        public static AlertDialogFragment newInstance() {

            Bundle args = new Bundle();
            AlertDialogFragment fragment = new AlertDialogFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Alert!")
                    .setMessage("Danger Will Robinson!"); //note chaining
            builder.setPositiveButton("I see it!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.v(TAG, "You clicked okay! Good times :)");
                }
            });
            builder.setNegativeButton("Noooo...", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.v(TAG, "You clicked cancel! Sad times :(");
                }
            });

            AlertDialog dialog = builder.create();
            return dialog;
        }
    }


    /* Menu handling */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_item_settings:
                Log.d(TAG, "Settings menu pressed");
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
