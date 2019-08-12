package com.sunthemart.deliverypartner.view.ui.services

import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase
import com.sunthemart.deliverypartner.R

/**
 * This service is used to send delivery guyz update
 */
class DeliveryPartnerTracker : Service() {

    private val tag = "DeliveryPartnerTracker"

    /**
     * binder method
     */
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    /**
     * onCreate of service
     */
    override fun onCreate() {
        super.onCreate()
        buildNotification()
        requestLocationUpdates()
    }


    private fun requestLocationUpdates() {
        val request = LocationRequest()
        request.interval = 10000
        request.fastestInterval = 5000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val client = LocationServices.getFusedLocationProviderClient(this)
        val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    val ref = FirebaseDatabase.getInstance().reference.child("location")
                    val location = locationResult!!.lastLocation
                    if (location != null) {
                        Log.d(tag, "location update $location")
                        ref.setValue(location)
                    }
                }
            }, null)
        }
    }

    private fun buildNotification() {
        val stop = "stop"
        registerReceiver(stopReceiver, IntentFilter(stop))
        val broadcastIntent = PendingIntent.getBroadcast(
                this, 0, Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel("my_service", "My Background Service")
                } else {
                    // If earlier version channel ID is not used
                    // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                    ""
                }
        // Create the persistent notification
        val builder = NotificationCompat.Builder(this, channelId)
                .setContentTitle("SuntheMart - Delivery")
                .setContentText("Delivery Has Started")
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.ic_my_location)
        startForeground(101, builder.build())
    }

    private var stopReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(tag, "received stop broadcast")
            // Stop the service when the notification is tapped
            unregisterReceiver(this)
            stopSelf()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

}