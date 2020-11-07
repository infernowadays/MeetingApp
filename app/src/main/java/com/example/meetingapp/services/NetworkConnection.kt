package com.example.meetingapp.services

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import androidx.lifecycle.LiveData


@Suppress("DEPRECATION")
 class NetworkConnection(private val context: Context) : LiveData<Boolean>() {
    private var connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback


    override fun onActive() {
        super.onActive()
        updateConnection()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ->{
                connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback())
                }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ->{
                lollipopNetworkRequest()
            }
            else ->{
                context.registerReceiver(
                        networkReceiver,
                        IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback())
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun lollipopNetworkRequest() {
        val requestBuilder = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        connectivityManager.registerNetworkCallback(
                requestBuilder.build(),
                connectivityManagerCallback()

        )
    }

    fun isNetworkOnline(context: Context): Boolean {
        var status = false
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var netInfo = cm.getNetworkInfo(0)
            if (netInfo != null && netInfo.state == NetworkInfo.State.CONNECTED) {
                status = true
            } else {
                netInfo = cm.getNetworkInfo(1)
                if (netInfo != null && netInfo.state == NetworkInfo.State.CONNECTED) status = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return status

    }
    private fun connectivityManagerCallback(): ConnectivityManager.NetworkCallback {
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postValue(true)
            }
        }
        return networkCallback
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateConnection()
        }
    }

    private fun updateConnection() {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected == true)
    }


}