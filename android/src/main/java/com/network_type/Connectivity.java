// Copyright 2019 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package com.network_type;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Reports connectivity related information such as connectivity type and wifi information.
 */
class Connectivity {
    private ConnectivityManager connectivityManager;
    private WifiManager wifiManager;

    Connectivity(ConnectivityManager connectivityManager, WifiManager wifiManager) {
        this.connectivityManager = connectivityManager;
        this.wifiManager = wifiManager;
    }

    String getNetworkType() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            if (capabilities == null) {
                return "none";
            }
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return "wifi";
            }
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return "mobile";
            }
        }

        return getNetworkTypeLegacy();
    }

    String getWifiName() {
        WifiInfo wifiInfo = getWifiInfo();
        String ssid = null;
        if (wifiInfo != null) ssid = wifiInfo.getSSID();
        if (ssid != null) ssid = ssid.replaceAll("\"", ""); // Android returns "SSID"
        return ssid;
    }

    String getWifiBSSID() {
        WifiInfo wifiInfo = getWifiInfo();
        String bssid = null;
        if (wifiInfo != null) {
            bssid = wifiInfo.getBSSID();
        }
        return bssid;
    }

    String getWifiIPAddress() {
        WifiInfo wifiInfo = null;
        if (wifiManager != null) wifiInfo = wifiManager.getConnectionInfo();

        String ip = null;
        int i_ip = 0;
        if (wifiInfo != null) i_ip = wifiInfo.getIpAddress();

        if (i_ip != 0)
            ip =
                    String.format(
                            "%d.%d.%d.%d",
                            (i_ip & 0xff), (i_ip >> 8 & 0xff), (i_ip >> 16 & 0xff), (i_ip >> 24 & 0xff));

        return ip;
    }

    private WifiInfo getWifiInfo() {
        return wifiManager == null ? null : wifiManager.getConnectionInfo();
    }

    @SuppressWarnings("deprecation")
    private String getNetworkTypeLegacy() {
        // handle type for Android versions less than Android 9
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return "none";
        }
        int type = info.getType();
        switch (type) {
            case ConnectivityManager.TYPE_ETHERNET:
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_WIMAX:
                return "wifi";
            case ConnectivityManager.TYPE_MOBILE:
            case ConnectivityManager.TYPE_MOBILE_DUN:
            case ConnectivityManager.TYPE_MOBILE_HIPRI:
                return "mobile";
            default:
                return "none";
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    String getNetworkSubtype() {

        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info == null || !info.isConnected()) {
            return "none";
        }

        Log.d("network_type", info.getSubtype() + "");

        switch (info.getSubtype()) {

            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";


            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";


            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";


            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "unknown";

        }

        return info.getSubtype() + "";
    }
}
