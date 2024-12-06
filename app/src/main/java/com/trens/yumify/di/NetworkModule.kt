package com.trens.yumify.di

import android.content.Context
import android.net.ConnectivityManager

object NetworkModule {

    fun provideConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}